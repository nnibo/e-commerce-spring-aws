package com.nicolas.paymentservice.service;

import com.nicolas.paymentservice.enums.PaymentStatus;
import com.nicolas.paymentservice.events.OrderCreatedEvent;
import com.nicolas.paymentservice.events.PaymentProcessedEvent;
import io.awspring.cloud.sns.core.SnsTemplate;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final SnsTemplate snsTemplate;

    @Value("${aws.sns.payment.topic}")
    private String paymentTopic;

    @SqsListener("${aws.sqs.order.queue}")
    public void processPayment(OrderCreatedEvent orderCreatedEvent) {
        log.info("Recebendo pedido {} para pagamento via {}", orderCreatedEvent.orderId(), orderCreatedEvent.paymentType());

        PaymentStatus status;
        String message;

        // Simulando a logica de um limite
        if(orderCreatedEvent.amount().doubleValue() > 10000) {
            status = PaymentStatus.FAILED;
            message = "Pagamento recusado: Limite de transação excedido.";
            log.warn("Pedido {} recusado. Valor: {}", orderCreatedEvent.orderId(), orderCreatedEvent.amount());
        } else {
            status = PaymentStatus.APPROVED;
            message = "Pagamento aprovado com sucesso.";
            log.info("Pedido {} aprovado. Valor: {}", orderCreatedEvent.orderId(), orderCreatedEvent.amount());
        }

        PaymentProcessedEvent paymentProcessedEvent = new PaymentProcessedEvent(orderCreatedEvent.orderId(), status, message);

        snsTemplate.convertAndSend(paymentTopic, paymentProcessedEvent);

        log.info("Resultado do pagamento publicado no SNS com sucesso.");
    }
}
