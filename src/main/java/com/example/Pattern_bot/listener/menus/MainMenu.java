package com.example.Pattern_bot.listener.menus;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MainMenu {
    private final TelegramBot telegramBot;

    public void sendWelcomeMessage(long chatId) {
        String welcomeText = """
            🎭 *Добро пожаловать в Анонимный Чат!*
            
            Правила:
            1. Общайтесь анонимно
            2. Уважайте собеседника
            3. Запрещена реклама и спам
            4. Конфиденциальность гарантирована
            
            Для начала выберите ваш пол.
            """;

        InlineKeyboardButton startButton = new InlineKeyboardButton("🚀 Начать")
                .callbackData("/start_chat");
        InlineKeyboardButton helpButton = new InlineKeyboardButton("❓ Помощь")
                .callbackData("/help");
        InlineKeyboardButton rulesButton = new InlineKeyboardButton("📜 Правила")
                .callbackData("/rules");

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{startButton},
                new InlineKeyboardButton[]{helpButton, rulesButton}
        );

        telegramBot.execute(new SendMessage(chatId, welcomeText)
                .parseMode(ParseMode.valueOf("Markdown"))
                .replyMarkup(keyboard));
    }

    public void sendMainMenuWithControls(long chatId) {
        String welcomeText = """
        🎭 *Главное меню*
        
        Выберите действие:
        """;

        InlineKeyboardButton searchButton = new InlineKeyboardButton("🔍 Найти собеседника")
                .callbackData("/search_partner");
        InlineKeyboardButton genderButton = new InlineKeyboardButton("👤 Сменить пол")
                .callbackData("/change_gender");
        InlineKeyboardButton preferencesButton = new InlineKeyboardButton("⚙️ Кого ищу")
                .callbackData("/change_preferences");
        InlineKeyboardButton helpButton = new InlineKeyboardButton("❓ Помощь")
                .callbackData("/help");
        InlineKeyboardButton rulesButton = new InlineKeyboardButton("📜 Правила")
                .callbackData("/rules");

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{searchButton},
                new InlineKeyboardButton[]{genderButton, preferencesButton},
                new InlineKeyboardButton[]{helpButton, rulesButton}
        );

        telegramBot.execute(new SendMessage(chatId, welcomeText)
                .parseMode(ParseMode.valueOf("Markdown"))
                .replyMarkup(keyboard));
    }
}
