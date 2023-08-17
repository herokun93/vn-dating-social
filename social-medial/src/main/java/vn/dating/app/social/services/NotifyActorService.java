package vn.dating.app.social.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.dating.app.social.models.NotifyActor;
import vn.dating.app.social.models.NotifyEntity;
import vn.dating.app.social.models.User;
import vn.dating.app.social.repositories.NotifyActorRepository;

import java.util.Optional;

@Service
@Slf4j
public class NotifyActorService {

    @Autowired
    private NotifyActorRepository notifyActorRepository;

    public NotifyActor saveNotifyActor(User toUser, NotifyEntity notifyEntity){
        NotifyActor notifyActor = new NotifyActor();
        notifyActor.setSource(toUser);
        notifyActor.setNotifyEntity(notifyEntity);
        return notifyActorRepository.save(notifyActor);
    }
    public NotifyActor findBySourceIdAndNotifyEntity(String sourceId, Long notifyEntityId){
        Optional<NotifyActor> notifyActor = notifyActorRepository.findBySourceIdAndNotifyEntityId(sourceId,notifyEntityId);
        if(notifyActor.isEmpty()) return null;
        return notifyActor.get();
    }
}
