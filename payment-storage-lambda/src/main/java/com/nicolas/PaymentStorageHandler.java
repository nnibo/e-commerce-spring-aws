package com.nicolas;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicolas.event.PaymentProcessedEvent;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class PaymentStorageHandler implements RequestHandler<SQSEvent, Void> {

    private static final String BUCKET_NAME = System.getenv("PAYMENT_BUCKET_NAME");
    private final S3Client s3Client = S3Client.create();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        for (SQSEvent.SQSMessage message : event.getRecords()) {
            try {
                String body = message.getBody();

                System.out.println("Mensagem recebida:");
                System.out.println(body);

                PaymentProcessedEvent payment = objectMapper.readValue(body, PaymentProcessedEvent.class);

                String fileName = "payments/" + payment.orderId() + ".json";

                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(BUCKET_NAME)
                        .key(fileName)
                        .contentType("application/json")
                        .build();

                s3Client.putObject(
                        putObjectRequest,
                        RequestBody.fromString(body)
                );

                System.out.println(
                        "Pagamento do pedido " + payment.orderId() + " salvo no S3."
                );

            } catch (Exception e) {
                System.err.println("Erro ao processar pagamento: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}