package vn.dating.telegram.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.dating.telegram.model.Couple;
import vn.dating.telegram.model.CoupleStatus;
import vn.dating.telegram.model.User;
import vn.dating.telegram.repositories.CoupleRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CoupleService {

    @Autowired
    private CoupleRepository coupleRepository;


    public boolean checkIsUserPending(User user) {
        return coupleRepository.existsByUser1OrUser2AndStatus(user, user, CoupleStatus.PENDING);
    }

    public boolean isUserJoined(User user) {
        return coupleRepository.existsByUser1OrUser2AndStatus(user, user, CoupleStatus.JOINED);
    }

    public boolean createChat(User newRequest){

        boolean isUserJoined  = isUserJoined(newRequest);
        boolean checkIsUserPending  = checkIsUserPending(newRequest);
        if(isUserJoined || checkIsUserPending){
            return false;
        }

        List<Couple>  listCouplePending = listCouplePending();

        if(listCouplePending.size()>0){
            Couple couple = listCouplePending.get(0);
            couple.setUser2(newRequest);
            couple.setStatus(CoupleStatus.JOINED);
            coupleRepository.save(couple);

            return true;
            }

       return false;
    }

    public Optional<Couple> isUserPending(User user) {
        return coupleRepository.findByUser1OrUser2AndStatus(user, null, CoupleStatus.PENDING);
    }


    public User isUserJoinedACoupe(User user){

        Couple coupleJoined = coupleRepository.findByUser1OrUser2AndStatus(user,user,CoupleStatus.JOINED).orElse(null);
        if(coupleJoined==null){
            return null;
        }else{
            int comparisonResult = user.getChatId().compareTo(coupleJoined.getUser1().getChatId());

            if(comparisonResult ==0){
//                log.info(user.getChatId().toString());
//                log.info(coupleJoined.getUser1().getChatId().toString());
//                log.info(coupleJoined.getUser2().getChatId().toString());
                log.info("ChatWith {}", coupleJoined.getUser2().getChatId());
                return coupleJoined.getUser2();
            }

            log.info("ChatWith {}", coupleJoined.getUser1().getChatId());
            return coupleJoined.getUser1();
        }
    }



//    public List<User> listUserPending(){
//        return coupleRepository.findPendingUsers();
//    }

    public List<Couple> listCouplePending(){
        return coupleRepository.findByStatus(CoupleStatus.PENDING);
    }

    public boolean isUserFinish(User user) {
        return coupleRepository.existsByUser1OrUser2AndStatus(user, user, CoupleStatus.FINISH);
    }



    public Couple save(Couple couple){
        return  coupleRepository.save(couple);
    }
}
