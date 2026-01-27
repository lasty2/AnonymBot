package com.example.Pattern_bot.command.commandHeap;

import com.example.Pattern_bot.command.abstractCommands.MessageCommand;
import com.example.Pattern_bot.command.annotation.BotCommand;
import com.example.Pattern_bot.listener.menus.MainMenu;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

@BotCommand(command = "/start")
public class StartCommand extends MessageCommand {

    private final MainMenu mainMenu;

    public StartCommand(TelegramBot telegramBot, MainMenu mainMenu) {
        super(telegramBot);
        this.mainMenu = mainMenu;
    }

    @Override
    protected void handleMessage(Update update) {
        long chatId = getChatId(update);
        String username = getUsername(update);

        String greeting = String.format("""
            Привет, %s! 👋
            
            Добро пожаловать в анонимный чат!
            Здесь вы можете общаться с незнакомцами, сохраняя полную анонимность.
            """, username != null ? username : "друг");

        sendTextMessage(chatId, greeting);
        mainMenu.sendWelcomeMessage(chatId);
    }
}