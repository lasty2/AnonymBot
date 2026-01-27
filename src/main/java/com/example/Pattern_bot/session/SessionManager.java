package com.example.Pattern_bot.session;

import com.example.Pattern_bot.session.UserSession;
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

    public SessionManager() {
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
            log.info("Created new session for chatId: {}", chatId);
        }
        return session;
    }

    public void updateSession(UserSession session) {
        sessions.put(session.getChatId(), session);
    }

    public void removeSession(Long chatId) {
        sessions.remove(chatId);
        log.info("Removed session for chatId: {}", chatId);
    }

    private void cleanupOldSessions() {
        int initialSize = sessions.size();
        sessions.entrySet().removeIf(entry ->
                entry.getValue().getSessionStartTime()
                        .isBefore(LocalDateTime.now().minusHours(2))
        );
        log.info("Cleaned up {} old sessions", initialSize - sessions.size());
    }

    public Long findPartner(UserSession currentSession) {
        return sessions.values().stream()
                .filter(session ->
                        !session.getChatId().equals(currentSession.getChatId()) &&
                                session.isReadyForChat() &&
                                session.getPartnerChatId() == null &&
                                session.isSearching()
                )
                .findFirst()
                .map(UserSession::getChatId)
                .orElse(null);
    }
}