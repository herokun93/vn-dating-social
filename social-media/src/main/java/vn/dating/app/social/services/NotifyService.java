package vn.dating.app.social.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.dating.app.social.models.Notify;
import vn.dating.app.social.models.NotifyEntity;
import vn.dating.app.social.models.User;
import vn.dating.app.social.repositories.NotifyRepository;
import vn.dating.common.utils.AppConstants;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class NotifyService {

    @Autowired
    private NotifyRepository notifyRepository;

    @Autowired
    private EntityManager entityManager;

    public Notify saveNotify(User user, NotifyEntity notifyEntity){
        Notify notify = new Notify();
        notify.setUser(user);
        notify.setNotifyEntity(notifyEntity);
        notify.setUpdatedAt(Instant.now());
        notify.setCreatedAt(Instant.now());
        notify = notifyRepository.save(notify);
        if(notify==null) return null;
        else return notify;
    }

    public Notify saveNotify(Notify notify){
        notify = notifyRepository.save(notify);
        if(notify==null) return null;
        else return notify;
    }

    public boolean hasUserReceivedNotification(User user, Long notifyEntityId) {
        return notifyRepository.existsByUserIdAndNotifyEntity_Id(user.getId(), notifyEntityId);
    }



    public Notify findByUserIdAndNotifyEntity(String userId,  Long notifyEntityId){
        Optional<Notify> notify = notifyRepository.findByUserIdAndNotifyEntityId(userId,notifyEntityId);
        if(notify.isEmpty()) return null;
        return notify.get();
    }

    public Page<Notify> findNotifiesByNotifyEntityAndUserIsOnline(NotifyEntity notifyEntity, boolean isOnline, int pageNo, int pageSize) {
        TypedQuery<Notify> query = entityManager.createQuery(
                "SELECT n FROM Notify n " +
                        "WHERE n.notifyEntity = :notifyEntity " +
                        "AND n.user.online = :isOnline " +
                        "ORDER BY n.createdAt DESC",
                Notify.class
        );
        query.setParameter("notifyEntity", notifyEntity);
        query.setParameter("isOnline", isOnline);
        query.setFirstResult((pageNo - 1) * pageSize);
        query.setMaxResults(pageSize);
        List<Notify> notifies = query.getResultList();
        long total = countNotifiesByNotifyEntityAndUserIsOnline(notifyEntity, isOnline);
        return new PageImpl<>(notifies, PageRequest.of(pageNo - 1, pageSize), total);
    }

    public long countNotifiesByNotifyEntityAndUserIsOnline(NotifyEntity notifyEntity, boolean isOnline) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(n) FROM Notify n " +
                        "WHERE n.notifyEntity = :notifyEntity " +
                        "AND n.user.online = :isOnline",
                Long.class
        );
        query.setParameter("notifyEntity", notifyEntity);
        query.setParameter("isOnline", isOnline);
        return query.getSingleResult();
    }

    public List<String> findOnlineUsersByNotifyEntityAndPage(NotifyEntity notifyEntity) {
        List<String> onlineUserIds = new ArrayList<>();

        int pageNumber = 1;
        int pageSize = AppConstants.DEFAULT_PAGE_SIZE_I;
        boolean hasMore = true;
        while (hasMore) {
            Page<Notify> notifyPage = findNotifiesByNotifyEntityAndUserIsOnline(notifyEntity,true, pageNumber, pageSize);
            for (Notify notify : notifyPage.getContent()) {
                onlineUserIds.add(notify.getUser().getId());
            }
            hasMore = notifyPage.isLast();
            pageNumber = pageNumber+1;
        }
        return onlineUserIds;
    }






}
