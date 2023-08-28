package vn.dating.app.social.config.socket;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

//@Component
//public class WebSocketSessionRegistry {
//    private final Map<String, Set<String>> userSessions = new ConcurrentHashMap<>();
//
//    public void registerSessionId(String username, String sessionId) {
//        userSessions.computeIfAbsent(username, key -> new HashSet<>()).add(sessionId);
//    }
//
//    public void unregisterSessionId(String username, String sessionId) {
//        Set<String> sessions = userSessions.get(username);
//        if (sessions != null) {
//            sessions.remove(sessionId);
//            if (sessions.isEmpty()) {
//                userSessions.remove(username);
//            }
//        }
//    }
//
//    public boolean isUserOnline(String username) {
//        Set<String> sessions = userSessions.get(username);
//        return sessions != null && !sessions.isEmpty();
//    }
//
//    public List<String> getUsernames() {
//
//        List<String> usernames = new ArrayList<>();
//        for (Map.Entry<String, Set<String>> entry : userSessions.entrySet()) {
//            String username = entry.getKey();
//            Set<String> sessionIds = entry.getValue();
//            if (!sessionIds.isEmpty()) {
//                usernames.add(username);
//            }
//        }
//        return usernames;
//    }
//
//    public Set<String> getSessionIds(String username) {
//        return userSessions.get(username);
//    }
//
//    public Set<String> getAllSessionIds() {
//        Set<String> allSessionIds = new HashSet<>();
//        userSessions.values().forEach(allSessionIds::addAll);
//        return allSessionIds;
//    }
//}

