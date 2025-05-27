package com.kafka.producer.kafka.producer.service;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StringProducerServiceImpTest {
    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private SendResult<String, String> sendResult;

    @Mock
    private ProducerRecord<String, String> producerRecord;

    @Mock
    private RecordMetadata recordMetadata;

    @InjectMocks
    private StringProducerServiceImp stringProducerService;

    @Test
    void messageSentSuccessfully() {
        String message = "test message";
        when(kafkaTemplate.send(anyString(), anyString())).thenReturn(CompletableFuture.completedFuture(sendResult));
        when(sendResult.getProducerRecord()).thenReturn(producerRecord);
        when(sendResult.getRecordMetadata()).thenReturn(recordMetadata);
        when(producerRecord.value()).thenReturn(message);
        when(recordMetadata.partition()).thenReturn(0);
        when(recordMetadata.offset()).thenReturn(1L);

        stringProducerService.sendMessage(message);

        verify(kafkaTemplate).send("str-topic", message);
    }

    @Test
    void messageFailsToSend() {
        String message = "test message";
        RuntimeException exception = new RuntimeException("Error de envío");
        CompletableFuture<SendResult<String, String>> future = new CompletableFuture<>();
        future.completeExceptionally(exception);

        when(kafkaTemplate.send(anyString(), anyString())).thenReturn(future);

        stringProducerService.sendMessage(message);

        verify(kafkaTemplate).send("str-topic", message);
    }

    @Test
    void emptyMessageSentSuccessfully() {
        String message = "";
        when(kafkaTemplate.send(anyString(), anyString())).thenReturn(CompletableFuture.completedFuture(sendResult));
        when(sendResult.getProducerRecord()).thenReturn(producerRecord);
        when(sendResult.getRecordMetadata()).thenReturn(recordMetadata);
        when(producerRecord.value()).thenReturn(message);

        stringProducerService.sendMessage(message);

        verify(kafkaTemplate).send("str-topic", message);
    }
}