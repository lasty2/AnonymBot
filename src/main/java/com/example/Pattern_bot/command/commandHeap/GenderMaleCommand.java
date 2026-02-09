package com.example.Pattern_bot.command.commandHeap;

import com.example.Pattern_bot.command.abstractCommands.CallbackCommand;
import com.example.Pattern_bot.command.annotation.BotCommand;
import com.example.Pattern_bot.listener.menus.ChatControlMenu;
import com.example.Pattern_bot.session.UserSession;
import com.example.Pattern_bot.session.SessionManager;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

@BotCommand(command = "/gender_male")
public class GenderMaleCommand extends CallbackCommand {

    private final SessionManager sessionManager;
    private final ChatControlMenu chatControlMenu;

    public GenderMaleCommand(TelegramBot telegramBot,
                             SessionManager sessionManager,
                             ChatControlMenu chatControlMenu) {
        super(telegramBot);
        this.sessionManager = sessionManager;
        this.chatControlMenu = chatControlMenu;
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
        sessionManager.updateSession(session);

        String genderSpecificMessage = "";
        if ("FEMALE".equals(gender)) {
            genderSpecificMessage = "\n\n✨ Вы будете искать собеседников *мужского пола*.";
        } else if ("MALE".equals(gender)) {
            genderSpecificMessage = "\n\n✨ Вы будете искать собеседников *женского пола*.";
        } else {
            genderSpecificMessage = "\n\n✨ Вы будете искать собеседников, которые также *не указали пол*.";
        }

        sendTextMessage(chatId, "✅ Отлично! Ваш пол установлен: " + genderText + genderSpecificMessage +
                "\n\nТеперь вы можете начать поиск собеседника.");

        chatControlMenu.sendChatControls(chatId);
    }
}