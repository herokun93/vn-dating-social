package vn.dating.app.gateway.kafka.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final ObjectMapper objectMapper;


//    @KafkaListener(topics = "${app.kafka.message-realtime}")
//    public void receiveOrderCash(String message) {
//        log.info("Receive message realtime {}", message);
//    }

}
