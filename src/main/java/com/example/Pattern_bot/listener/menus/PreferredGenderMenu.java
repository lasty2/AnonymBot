package com.example.Pattern_bot.listener.menus;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PreferredGenderMenu {
    private final TelegramBot telegramBot;

    public void sendPreferredGenderSelection(long chatId) {
        InlineKeyboardButton maleButton = new InlineKeyboardButton("👨 Мужской")
                .callbackData("/pref_male");
        InlineKeyboardButton femaleButton = new InlineKeyboardButton("👩 Женский")
                .callbackData("/pref_female");
        InlineKeyboardButton allButton = new InlineKeyboardButton("👥 Не имеет значения (все)")
                .callbackData("/pref_all");

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{maleButton},
                new InlineKeyboardButton[]{femaleButton},
                new InlineKeyboardButton[]{allButton}
        );

        telegramBot.execute(new SendMessage(chatId,
                """
                    *🎯 Выбор предпочтений для поиска*
                    
                    Теперь выберите, кого вы хотите искать:
                    
                    👨 Мужчины
                    👩 Женщины
                    👥 Все (без разграничения по полу)
                    
                    *Примечание:* Этот выбор можно будет изменить позже.""")
                .replyMarkup(keyboard));
    }

    public void sendChangePreferencesMenu(long chatId) {
        InlineKeyboardButton changeButton = new InlineKeyboardButton("🔄 Изменить предпочтения")
                .callbackData("/change_preferences");
        InlineKeyboardButton backButton = new InlineKeyboardButton("🔙 Назад")
                .callbackData("/main_menu");

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{changeButton},
                new InlineKeyboardButton[]{backButton}
        );

        telegramBot.execute(new SendMessage(chatId,
                "⚙️ *Управление предпочтениями*\n\nВы можете изменить кого вы ищете в любой момент.")
                .replyMarkup(keyboard));
    }
}
