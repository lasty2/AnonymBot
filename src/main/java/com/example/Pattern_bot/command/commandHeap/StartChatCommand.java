package com.example.Pattern_bot.command.commandHeap;

import com.example.Pattern_bot.command.abstractCommands.CallbackCommand;
import com.example.Pattern_bot.command.annotation.BotCommand;
import com.example.Pattern_bot.listener.menus.GenderMenu;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

@BotCommand(command = "/start_chat")
public class StartChatCommand extends CallbackCommand {

    private final GenderMenu genderMenu;

    public StartChatCommand(TelegramBot telegramBot, GenderMenu genderMenu) {
        super(telegramBot);
        this.genderMenu = genderMenu;
    }

    @Override
    protected void handleCallbackQuery(Update update) {
        long chatId = getChatId(update);
        genderMenu.sendGenderSelection(chatId);
    }
}
