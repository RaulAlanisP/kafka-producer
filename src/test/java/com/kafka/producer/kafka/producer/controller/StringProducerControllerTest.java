package com.kafka.producer.kafka.producer.controller;

import com.kafka.producer.kafka.producer.service.StringProducerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StringProducerControllerTest {
    @Mock
    private StringProducerService stringProducerService;

    @InjectMocks
    private StringProducerController stringProducerController;

    @Test
    void sendMessageReturnsCreatedStatus() {
        String message = "test message";

        ResponseEntity<?> response = stringProducerController.sendMessage(message);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(stringProducerService).sendMessage(message);
    }

    @Test
    void emptyMessageReturnsCreatedStatus() {
        String message = "";

        ResponseEntity<?> response = stringProducerController.sendMessage(message);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(stringProducerService).sendMessage(message);
    }

    @Test
    void nullMessageReturnsCreatedStatus() {
        String message = null;

        ResponseEntity<?> response = stringProducerController.sendMessage(message);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(stringProducerService).sendMessage(message);
    }

    @Test
    void whenServiceThrowsExceptionPropagateException() {
        String message = "test message";
        doThrow(new RuntimeException("Error")).when(stringProducerService).sendMessage(anyString());

        assertThrows(RuntimeException.class, () -> stringProducerController.sendMessage(message));
        verify(stringProducerService).sendMessage(message);
    }
}