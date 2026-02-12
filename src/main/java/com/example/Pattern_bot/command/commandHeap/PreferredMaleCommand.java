package com.example.Pattern_bot.command.commandHeap;

import com.example.Pattern_bot.command.abstractCommands.CallbackCommand;
import com.example.Pattern_bot.command.annotation.BotCommand;
import com.example.Pattern_bot.listener.menus.ChatControlMenu;
import com.example.Pattern_bot.session.UserSession;
import com.example.Pattern_bot.session.SessionManager;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

@BotCommand(command = "/pref_male")
public class PreferredMaleCommand extends CallbackCommand {

    private final SessionManager sessionManager;
    private final ChatControlMenu chatControlMenu;

    public PreferredMaleCommand(TelegramBot telegramBot,
                                SessionManager sessionManager,
                                ChatControlMenu chatControlMenu) {
        super(telegramBot);
        this.sessionManager = sessionManager;
        this.chatControlMenu = chatControlMenu;
    }

    @Override
    protected void handleCallbackQuery(Update update) {
        long chatId = getChatId(update);
        UserSession session = sessionManager.getSession(chatId);

        if (session == null) {
            sendTextMessage(chatId, "⚠️ Сессия не найдена. Пожалуйста, начните с /start");
            return;
        }

        session.setPreferredGender("MALE");
        sessionManager.updateSession(session);

        String genderText = session.getGender().equals("MALE") ? "👨" :
                (session.getGender().equals("FEMALE") ? "👩" : "🤷");

        sendTextMessage(chatId,
                "✅ *Настройки завершены!*\n\n" +
                        "Ваш пол: " + genderText + " " + getGenderText(session.getGender()) + "\n" +
                        "Ищете: 👨 Мужчин\n\n" +
                        "Теперь вы можете начать поиск собеседника.");

        chatControlMenu.sendChatControls(chatId);
    }

    private String getGenderText(String gender) {
        return switch (gender) {
            case "MALE" -> "Мужской";
            case "FEMALE" -> "Женский";
            default -> "Не указан";
        };
    }
}