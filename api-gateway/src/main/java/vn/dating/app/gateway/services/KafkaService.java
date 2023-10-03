package vn.dating.app.gateway.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.dating.app.gateway.kafka.producer.KafkaProducer;
import vn.dating.common.dto.CreateUserDto;

@Service
@RequiredArgsConstructor
public class KafkaService {

    private final KafkaProducer kafkaProducer;

    public void createUserSocial(CreateUserDto createUserDto) {
        kafkaProducer.sendMessage("social-create-user", null, createUserDto);
    }

    public void createUserSocket(CreateUserDto createUserDto) {
        kafkaProducer.sendMessage("socket-create-user", null, createUserDto);
    }

    public void createUserMessage(CreateUserDto createUserDto) {
        kafkaProducer.sendMessage("message-create-user", null, createUserDto);
    }

}
