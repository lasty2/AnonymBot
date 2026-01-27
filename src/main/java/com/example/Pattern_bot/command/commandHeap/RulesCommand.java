package com.example.Pattern_bot.command.commandHeap;

import com.example.Pattern_bot.command.abstractCommands.CallbackCommand;
import com.example.Pattern_bot.command.annotation.BotCommand;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

@BotCommand(command = "/rules")
public class RulesCommand extends CallbackCommand {

    public RulesCommand(TelegramBot telegramBot) {
        super(telegramBot);
    }

    @Override
    protected void handleCallbackQuery(Update update) {
        long chatId = getChatId(update);

        String rulesText = """
            *📜 Правила Анонимного Чата*
            
            1. *Уважение* - Общайтесь вежливо, не оскорбляйте собеседников
            2. *Конфиденциальность* - Не раскрывайте личные данные
            3. *Без спама* - Запрещена реклама и массовая рассылка
            4. *Законность* - Запрещены противоправные темы
            5. *Анонимность* - Не пытайтесь раскрыть личность собеседника
            
            *Нарушение правил приведет к:*
            ⚠️ Предупреждение
            🔇 Временная блокировка
            ❌ Перманентный бан
            
            *Ваши права:*
            ✅ Вы можете завершить диалог в любой момент
            ✅ Вы можете пожаловаться на собеседника
            ✅ Ваша анонимность защищена
            """;

        sendTextMessage(chatId, rulesText);
    }
}