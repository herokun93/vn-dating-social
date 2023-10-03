package vn.dating.app.socket.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, String key, Object payload) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String value = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(topic, key, value).addCallback(
                    s -> log.info("Send to kafka success {}", payload),
                    e -> log.error("Send to kafka error ", e));
        } catch (Exception e) {
            log.error("Send to kafka error ", e);
        }
    }

    public void sendMessage(String topic, String key, Object payload, ListenableFutureCallback<Object> callback) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String value = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(topic, key, value).addCallback(callback);
        } catch (Exception e) {
            log.error("Send to kafka error ", e);
        }
    }
}
