package com.example.Pattern_bot.command.commandHeap;

import com.example.Pattern_bot.command.abstractCommands.CallbackCommand;
import com.example.Pattern_bot.command.annotation.BotCommand;
import com.example.Pattern_bot.listener.menus.ChatControlMenu;
import com.example.Pattern_bot.session.UserSession;
import com.example.Pattern_bot.session.SessionManager;
import com.example.Pattern_bot.listener.menus.GenderMenu;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

@BotCommand(command = "/gender_unknown")
public class GenderUnknownCommand extends CallbackCommand {

    private final SessionManager sessionManager;
    private final ChatControlMenu chatControlMenu;

    public GenderUnknownCommand(TelegramBot telegramBot,
                                SessionManager sessionManager,
                                ChatControlMenu chatControlMenu) {
        super(telegramBot);
        this.sessionManager = sessionManager;
        this.chatControlMenu = chatControlMenu;
    }

    @Override
    protected void handleCallbackQuery(Update update) {
        processGenderSelection(update, "UNKNOWN", "🤷 не указан");
    }

    private void processGenderSelection(Update update, String gender, String genderText) {
        long chatId = getChatId(update);
        String username = getUsername(update);

        UserSession session = sessionManager.createOrUpdateSession(chatId, username);
        session.setGender(gender);
        sessionManager.updateSession(session);

        sendTextMessage(chatId, "✅ Отлично! Ваш пол установлен: " + genderText +
                "\n\nТеперь вы можете начать поиск собеседника.");

        chatControlMenu.sendChatControls(chatId);
    }
}