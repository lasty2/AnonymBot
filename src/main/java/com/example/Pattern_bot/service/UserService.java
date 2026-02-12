package com.example.Pattern_bot.service;

import com.example.Pattern_bot.model.UserEntity;
import com.example.Pattern_bot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserEntity createOrUpdateUser(Long chatId, String username) {
        Optional<UserEntity> userOptional = userRepository.findByChatId(chatId);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            user.setUsername(username);
            user.setLastActivityDate(LocalDateTime.now());
            return userRepository.save(user);
        } else {
            UserEntity newUser = new UserEntity();
            newUser.setChatId(chatId);
            newUser.setUsername(username);
            newUser.setCountUses(0);
            return userRepository.save(newUser);
        }
    }

    @Transactional
    public void updateGender(Long chatId, String gender) {
        userRepository.updateGender(chatId, gender);
        log.info("Updated gender for chatId: {} to {}", chatId, gender);
    }

    @Transactional
    public void updatePreferredGender(Long chatId, String preferredGender) {
        userRepository.updatePreferredGender(chatId, preferredGender);
        log.info("Updated preferred gender for chatId: {} to {}", chatId, preferredGender);
    }

    @Transactional
    public void incrementCountUses(Long chatId) {
        if (userRepository.existsByChatId(chatId)) {
            userRepository.incrementCountUses(chatId);
            log.info("Incremented count_uses for chatId: {}", chatId);
        }
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> getUserByChatId(Long chatId) {
        return userRepository.findByChatId(chatId);
    }

    @Transactional(readOnly = true)
    public Integer getUserCountUses(Long chatId) {
        return userRepository.findByChatId(chatId)
                .map(UserEntity::getCountUses)
                .orElse(0);
    }

    @Transactional(readOnly = true)
    public String getUserGender(Long chatId) {
        return userRepository.findByChatId(chatId)
                .map(UserEntity::getGender)
                .orElse("UNKNOWN");
    }

    @Transactional(readOnly = true)
    public String getUserPreferredGender(Long chatId) {
        return userRepository.findByChatId(chatId)
                .map(UserEntity::getPreferredGender)
                .orElse(null);
    }
}