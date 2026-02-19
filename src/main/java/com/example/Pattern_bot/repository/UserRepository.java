package com.example.Pattern_bot.repository;

import com.example.Pattern_bot.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByChatId(Long chatId);

    boolean existsByChatId(Long chatId);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.countUses = u.countUses + 1 WHERE u.chatId = :chatId")
    void incrementCountUses(@Param("chatId") Long chatId);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.gender = :gender WHERE u.chatId = :chatId")
    void updateGender(@Param("chatId") Long chatId, @Param("gender") String gender);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.preferredGender = :preferredGender WHERE u.chatId = :chatId")
    void updatePreferredGender(@Param("chatId") Long chatId, @Param("preferredGender") String preferredGender);
}