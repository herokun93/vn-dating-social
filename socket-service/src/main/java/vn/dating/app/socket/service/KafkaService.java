package vn.dating.app.socket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.dating.app.socket.dto.MessageDto;
import vn.dating.app.socket.kafka.producer.KafkaProducer;

@Service
@RequiredArgsConstructor
public class KafkaService {

    private final KafkaProducer kafkaProducer;


    public void sendMessage() {
        MessageDto messageDto = MessageDto.builder().name("BuiTuyen").build();
        kafkaProducer.sendMessage("message-realtime", null, messageDto);
    }

}
