package vn.dating.app.social.kafka.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import vn.dating.common.dto.CreateUserDto;

@Component
@AllArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final ObjectMapper objectMapper;


//    @KafkaListener(topics = "${app.kafka.order-cash-topic}")
    @KafkaListener(topics = "social-comment")
    public void receiveOrderCash(String message) {
        log.info("Receive order cash {}", message);
        log.info("Store {}", message);

    }

    @KafkaListener(topics = "social-create-user")
    public void receiveCreateUser(String createUserDtoJson) {
        try {
            Gson gson = new Gson();
            CreateUserDto createUserDto = gson.fromJson(createUserDtoJson, CreateUserDto.class);
            log.info("Receive create user {}", createUserDto.toString());
        } catch (Exception e) {
            log.error("Error parsing JSON to CreateUserDto: {}", e.getMessage());
        }
    }
    //    @KafkaListener(topics = "${app.kafka.order-cash-topic}")
//    @KafkaListener(topics = "social-comment")
//    public void receiveOrderCash(String message) {
//        log.info("Receive order cash {}", message);
//        log.info("Store {}", message);
//
//    }

}
