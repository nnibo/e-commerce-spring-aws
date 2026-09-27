package com.nicolas.orderservice.service;

import com.nicolas.orderservice.dto.event.OrderCreatedEvent;
import com.nicolas.orderservice.dto.event.PaymentProcessedEvent;
import com.nicolas.orderservice.dto.request.OrderRequestDTO;
import com.nicolas.orderservice.dto.response.OrderResponseDTO;
import com.nicolas.orderservice.entity.OrderEntity;
import com.nicolas.orderservice.entity.ProductEntity;
import com.nicolas.orderservice.enums.PaymentStatus;
import com.nicolas.orderservice.repository.IOrderRepository;
import com.nicolas.orderservice.repository.IProductRepository;
import io.awspring.cloud.sns.core.SnsTemplate;
import io.awspring.cloud.sqs.annotation.SqsListener;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final IOrderRepository orderRepository;
    private final IProductRepository productRepository;
    private final SnsTemplate snsTemplate;

    @Value("${aws.sns.order.topic}")
    private String orderTopic;

    @Transactional
    public void createOrder(OrderRequestDTO orderRequestDTO) {
        ProductEntity product = productRepository.findById(orderRequestDTO.productId())
                .orElseThrow(() -> new RuntimeException("Product Not found"));

        if(product.getStock() < orderRequestDTO.quantity()) {
            throw new RuntimeException("We dont have this quantity of product: " + product.getName() + " in our stock");
        }

        Integer newStock = product.getStock() - orderRequestDTO.quantity();
        product.setStock(newStock);

        BigDecimal totalAmount = product.getPrice().multiply(BigDecimal.valueOf(orderRequestDTO.quantity()));

        OrderEntity newOrder = OrderEntity.builder()
                .productId(orderRequestDTO.productId())
                .amount(totalAmount)
                .paymentStatus(PaymentStatus.PENDING)
                .quantity(orderRequestDTO.quantity())
                .paymentType(orderRequestDTO.paymentType())
                .build();

        orderRepository.save(newOrder);
        productRepository.save(product);

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(newOrder.getId(), newOrder.getAmount(), newOrder.getPaymentType());
        snsTemplate.convertAndSend(orderTopic, orderCreatedEvent);
    }

    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(o ->
                new OrderResponseDTO(o.getId(), o.getProductId(), o.getQuantity(), o.getAmount(), o.getOrderTime(), o.getPaymentStatus(), o.getPaymentType()))
                .toList();
    }

    @Transactional
    @SqsListener("${aws.sqs.payment.queue}")
    public void processPaymentResult(PaymentProcessedEvent paymentProcessedEvent) {
        OrderEntity order = orderRepository.findById(paymentProcessedEvent.orderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        ProductEntity product = productRepository.findById(order.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));


        if(paymentProcessedEvent.status() == PaymentStatus.FAILED) {
            Integer productQuantityIfFailed = order.getQuantity() + product.getStock();
            product.setStock(productQuantityIfFailed);
        }

        order.setPaymentStatus(paymentProcessedEvent.status());

        orderRepository.save(order);
        productRepository.save(product);
    }

    public void deleteOrder(UUID id) {
        orderRepository.deleteById(id);
    }

}
