package com.example.Pattern_bot.listener.menus;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatControlMenu {
    private final TelegramBot telegramBot;

    public void sendChatControls(long chatId) {
        InlineKeyboardButton searchButton = new InlineKeyboardButton("🔍 Найти собеседника")
                .callbackData("/search_partner");
        InlineKeyboardButton genderButton = new InlineKeyboardButton("👤 Сменить пол")
                .callbackData("/change_gender");
        InlineKeyboardButton preferencesButton = new InlineKeyboardButton("⚙️ Кого ищу")
                .callbackData("/change_preferences");
        InlineKeyboardButton menuButton = new InlineKeyboardButton("🏠 Главное меню")
                .callbackData("/main_menu");

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{searchButton},
                new InlineKeyboardButton[]{genderButton, preferencesButton},
                new InlineKeyboardButton[]{menuButton}
        );

        telegramBot.execute(new SendMessage(chatId,
                "Управление чатом:")
                .replyMarkup(keyboard));
    }

    public void sendSearchingMenu(long chatId) {
        InlineKeyboardButton stopButton = new InlineKeyboardButton("⏹ Остановить поиск")
                .callbackData("/stop_search");
        InlineKeyboardButton menuButton = new InlineKeyboardButton("🏠 Главное меню")
                .callbackData("/main_menu");

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{stopButton},
                new InlineKeyboardButton[]{menuButton}
        );

        telegramBot.execute(new SendMessage(chatId,
                """
                        🔍 *Идет поиск собеседника...*
                        
                        Вы можете остановить поиск в любой момент.""")
                .replyMarkup(keyboard));
    }

    public void sendChattingMenu(long chatId) {
        InlineKeyboardButton endButton = new InlineKeyboardButton("❌ Завершить диалог")
                .callbackData("/end_chat");
        InlineKeyboardButton menuButton = new InlineKeyboardButton("🏠 Главное меню")
                .callbackData("/main_menu");

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{endButton},
                new InlineKeyboardButton[]{menuButton}
        );

        telegramBot.execute(new SendMessage(chatId,
                """
                        💬 *Вы в диалоге*
                        
                        Отправляйте текстовые сообщения, они будут пересылаться собеседнику.""")
                .replyMarkup(keyboard));
    }
}

