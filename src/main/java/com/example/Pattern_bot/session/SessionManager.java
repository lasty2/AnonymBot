package com.example.Pattern_bot.session;

import com.example.Pattern_bot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SessionManager {

    private final Map<Long, UserSession> sessions = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final UserService userService;

    public SessionManager(UserService userService) {
        this.userService = userService;
        // Очистка устаревших сессий каждые 30 минут
        scheduler.scheduleAtFixedRate(this::cleanupOldSessions, 30, 30, TimeUnit.MINUTES);
    }

    public UserSession getSession(Long chatId) {
        return sessions.get(chatId);
    }

    public UserSession createOrUpdateSession(Long chatId, String username) {
        UserSession session = sessions.get(chatId);
        if (session == null) {
            session = new UserSession(chatId, username);
            sessions.put(chatId, session);

            userService.createOrUpdateUser(chatId, username);

            UserSession finalSession = session;
            userService.getUserByChatId(chatId).ifPresent(user -> {
                if (user.getGender() != null) {
                    finalSession.setGender(user.getGender());}
                if (user.getPreferredGender() != null) {
                    finalSession.setPreferredGender(user.getPreferredGender());}});
        } else {
            session.setUsername(username);
            userService.createOrUpdateUser(chatId, username);
        }
        return session;
    }

    public void updateSession(UserSession session) {
        sessions.put(session.getChatId(), session);

        if (session.getGender() != null) {userService.updateGender(session.getChatId(), session.getGender());}
        if (session.getPreferredGender() != null) {userService.updatePreferredGender(session.getChatId(), session.getPreferredGender());}
    }

    public void removeSession(Long chatId) {
        sessions.remove(chatId);
    }

    private void cleanupOldSessions() {
        int initialSize = sessions.size();
        sessions.entrySet().removeIf(entry ->
                entry.getValue().getSessionStartTime()
                        .isBefore(LocalDateTime.now().minusHours(2))
        );
    }

    public Long findPartner(UserSession currentSession) {
        return sessions.values().stream()
                .filter(session ->
                        !session.getChatId().equals(currentSession.getChatId()) &&
                                session.isReadyForChat() &&
                                session.getPartnerChatId() == null &&
                                session.isSearching() &&
                                isCompatible(currentSession, session)
                )
                .findFirst()
                .map(UserSession::getChatId)
                .orElse(null);
    }

    private boolean isCompatible(UserSession user1, UserSession user2) {
        if (!user1.hasSelectedPreferences() || !user2.hasSelectedPreferences()) {
            return false;
        }

        boolean user1CompatibleWithUser2 = isUserCompatibleWithTarget(user1, user2);
        boolean user2CompatibleWithUser1 = isUserCompatibleWithTarget(user2, user1);

        return user1CompatibleWithUser2 && user2CompatibleWithUser1;
    }

    private boolean isUserCompatibleWithTarget(UserSession user, UserSession target) {
        String preferredGender = user.getPreferredGender();

        if ("ALL".equals(preferredGender)) { return true;}

        return preferredGender != null && preferredGender.equals(target.getGender());
    }
}