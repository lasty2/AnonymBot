package com.example.Pattern_bot.command.commandHeap;

import com.example.Pattern_bot.command.abstractCommands.CallbackCommand;
import com.example.Pattern_bot.command.annotation.BotCommand;
import com.example.Pattern_bot.listener.menus.PreferredGenderMenu;
import com.example.Pattern_bot.session.UserSession;
import com.example.Pattern_bot.session.SessionManager;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

@BotCommand(command = "/gender_male")
public class GenderMaleCommand extends CallbackCommand {

    private final SessionManager sessionManager;
    private final PreferredGenderMenu preferredGenderMenu;

    public GenderMaleCommand(TelegramBot telegramBot,
                             SessionManager sessionManager,
                             PreferredGenderMenu preferredGenderMenu) {
        super(telegramBot);
        this.sessionManager = sessionManager;
        this.preferredGenderMenu = preferredGenderMenu;
    }

    @Override
    protected void handleCallbackQuery(Update update) {
        processGenderSelection(update, "MALE", "👨 мужской");
    }

    private void processGenderSelection(Update update, String gender, String genderText) {
        long chatId = getChatId(update);
        String username = getUsername(update);

        UserSession session = sessionManager.createOrUpdateSession(chatId, username);
        session.setGender(gender);
        session.setPreferredGender(null);
        sessionManager.updateSession(session);

        sendTextMessage(chatId, "✅ Отлично! Ваш пол установлен: " + genderText +
                "\n\nТеперь вы можете начать поиск собеседника.");

        preferredGenderMenu.sendPreferredGenderSelection(chatId);
    }
}