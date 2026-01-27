package com.example.Pattern_bot.command.commandHeap;

import com.example.Pattern_bot.command.abstractCommands.CallbackCommand;
import com.example.Pattern_bot.command.annotation.BotCommand;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

@BotCommand(command = "/help")
public class HelpCommand extends CallbackCommand {

    public HelpCommand(TelegramBot telegramBot) {
        super(telegramBot);
    }

    @Override
    protected void handleCallbackQuery(Update update) {
        long chatId = getChatId(update);

        String helpText = """
            *📖 Помощь по боту*
            
            *Как начать:*
            1. Нажмите "Начать"
            2. Выберите ваш пол
            3. Нажмите "Найти собеседника"
            
            *Команды:*
            🔍 /search_partner - Найти собеседника
            ⏹ /stop_search - Остановить поиск
            ❌ /end_chat - Завершить диалог
            🏠 /start - Главное меню
            
            *Важно:*
            • Все сообщения анонимны
            • Сохраняйте уважение к собеседнику
            • Запрещена реклама и спам
            """;

        sendTextMessage(chatId, helpText);
    }
}