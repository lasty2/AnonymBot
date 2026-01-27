package com.example.Pattern_bot.session;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserSession {
    private Long chatId;
    private String username;
    private String gender; // "MALE", "FEMALE", "UNKNOWN"
    private String partnerChatId; // ID партнера в чате
    private LocalDateTime sessionStartTime;
    private boolean isSearching; // Ищет ли пользователь собеседника

    public UserSession(Long chatId, String username) {
        this.chatId = chatId;
        this.username = username;
        this.gender = "UNKNOWN";
        this.isSearching = false;
        this.sessionStartTime = LocalDateTime.now();
    }

    public boolean isReadyForChat() {
        return !"UNKNOWN".equals(gender) && partnerChatId == null;
    }
}