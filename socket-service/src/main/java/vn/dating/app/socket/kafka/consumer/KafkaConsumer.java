package vn.dating.app.socket.kafka.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import vn.dating.app.socket.service.UserService;
import vn.dating.common.dto.CreateUserDto;

@Component
@AllArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final ObjectMapper objectMapper;
    private final UserService userService;


//    @KafkaListener(topics = "${app.kafka.message-realtime}")
//    public void receiveOrderCash(String message) {
//        log.info("Receive message realtime {}", message);
//    }

    @KafkaListener(topics = "socket-create-user")
    public void receiveCreateUser(String createUserDtoJson) {
        try {
            Gson gson = new Gson();
            CreateUserDto createUserDto = gson.fromJson(createUserDtoJson, CreateUserDto.class);
            userService.saveUser(createUserDto);
            log.info("Receive create user {}", createUserDto.toString());
        } catch (Exception e) {
            log.error("Error parsing JSON to CreateUserDto: {}", e.getMessage());
        }
    }

}
