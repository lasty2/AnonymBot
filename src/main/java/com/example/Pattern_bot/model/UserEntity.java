package com.example.Pattern_bot.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "bot_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id", nullable = false, unique = true)
    private Long chatId;

    @Column(name = "username")
    private String username;

    @Column(name = "gender")
    private String gender;

    @Column(name = "preferred_gender")
    private String preferredGender;

    @Column(name = "count_uses", nullable = false)
    private Integer countUses = 0;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "last_activity_date")
    private LocalDateTime lastActivityDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        lastActivityDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastActivityDate = LocalDateTime.now();
    }
}