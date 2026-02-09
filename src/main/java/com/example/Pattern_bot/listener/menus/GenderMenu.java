package com.example.Pattern_bot.listener.menus;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenderMenu {
    private final TelegramBot telegramBot;

    public void sendGenderSelection(long chatId) {
        InlineKeyboardButton maleButton = new InlineKeyboardButton("👨 Мужской")
                .callbackData("/gender_male");
        InlineKeyboardButton femaleButton = new InlineKeyboardButton("👩 Женский")
                .callbackData("/gender_female");
        InlineKeyboardButton skipButton = new InlineKeyboardButton("🤷 Не указывать")
                .callbackData("/gender_unknown");

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{maleButton},
                new InlineKeyboardButton[]{femaleButton},
                new InlineKeyboardButton[]{skipButton}
        );

        telegramBot.execute(new SendMessage(chatId,
                """
                    *Примечание:*
                    👨 Мужчины будут искать женщин
                    👩 Женщины будут искать мужчин
                    🤷 Те, кто не указал пол, будут искать таких же
                    Пожалуйста, выберите ваш пол для лучшего подбора собеседника:""")
                .replyMarkup(keyboard));
    }
}
