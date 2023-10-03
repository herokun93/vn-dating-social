package vn.dating.app.social.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.dating.app.social.dto.MessageDto;
import vn.dating.app.social.kafka.producer.KafkaProducer;

@Service
@RequiredArgsConstructor
public class KafkaService {

    private final KafkaProducer kafkaProducer;


    public void sendMessage() {
        MessageDto messageDto = MessageDto.builder().name("BuiTuyen").build();
        kafkaProducer.sendMessage("social-comment", null, messageDto);
    }

}
