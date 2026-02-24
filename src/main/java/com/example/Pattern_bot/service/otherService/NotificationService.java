package com.example.Pattern_bot.service.otherService;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InputFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.Pattern_bot.model.UserEntity;
import com.example.Pattern_bot.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final UserRepository userRepository;
    private final TelegramBot telegramBot;

    @Transactional(readOnly = true)
    public void notificationAll(String text, MultipartFile file) {
        List<UserEntity> users = userRepository.findAll();

        try {
            for (UserEntity user : users) {
                long chatId = user.getChatId();
                try {
                    if (file != null && !file.isEmpty()) {
                        InputFile inputFile = new InputFile(file.getInputStream().readAllBytes(),
                                file.getOriginalFilename(),
                                file.getContentType()
                        );

                        SendPhoto sendPhoto = new SendPhoto(
                                chatId, inputFile.getBytes())
                                .caption(text != null && !text.isBlank() ? text : null);

                        telegramBot.execute(sendPhoto);
                    } else if (text != null && !text.isBlank()) {
                        SendMessage sendMessage = new SendMessage(chatId, text);
                        telegramBot.execute(sendMessage);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}