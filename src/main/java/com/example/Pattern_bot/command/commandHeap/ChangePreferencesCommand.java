package com.example.Pattern_bot.command.commandHeap;

import com.example.Pattern_bot.command.abstractCommands.CallbackCommand;
import com.example.Pattern_bot.command.annotation.BotCommand;
import com.example.Pattern_bot.listener.menus.PreferredGenderMenu;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

@BotCommand(command = "/change_preferences")
public class ChangePreferencesCommand extends CallbackCommand {

    private final PreferredGenderMenu preferredGenderMenu;

    public ChangePreferencesCommand(TelegramBot telegramBot,
                                    PreferredGenderMenu preferredGenderMenu) {
        super(telegramBot);
        this.preferredGenderMenu = preferredGenderMenu;
    }

    @Override
    protected void handleCallbackQuery(Update update) {
        long chatId = getChatId(update);

        sendTextMessage(chatId, "🔄 Изменение предпочтений для поиска:");
        preferredGenderMenu.sendPreferredGenderSelection(chatId);
    }
}