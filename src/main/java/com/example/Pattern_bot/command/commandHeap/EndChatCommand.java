package com.example.Pattern_bot.command.commandHeap;

import com.example.Pattern_bot.command.abstractCommands.CallbackCommand;
import com.example.Pattern_bot.command.annotation.BotCommand;
import com.example.Pattern_bot.session.UserSession;
import com.example.Pattern_bot.session.SessionManager;
import com.example.Pattern_bot.listener.menus.ChatControlMenu;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

@BotCommand(command = "/end_chat")
public class EndChatCommand extends CallbackCommand {

    private final SessionManager sessionManager;
    private final ChatControlMenu chatControlMenu;

    public EndChatCommand(TelegramBot telegramBot,
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

        if (session != null && session.getPartnerChatId() != null) {
            // Уведомляем собеседника о завершении чата
            long partnerChatId = Long.parseLong(session.getPartnerChatId());
            sendTextMessage(partnerChatId, "❌ Собеседник завершил диалог.");
            chatControlMenu.sendChatControls(partnerChatId);

            // Очищаем данные о партнере у собеседника
            UserSession partnerSession = sessionManager.getSession(partnerChatId);
            if (partnerSession != null) {
                partnerSession.setPartnerChatId(null);
                sessionManager.updateSession(partnerSession);
            }

            // Очищаем данные о партнере у текущего пользователя
            session.setPartnerChatId(null);
            sessionManager.updateSession(session);

            sendTextMessage(chatId, "✅ Диалог завершен. Вы можете найти нового собеседника.");
            chatControlMenu.sendChatControls(chatId);
        } else {
            sendTextMessage(chatId, "❌ Вы не находитесь в диалоге.");
            chatControlMenu.sendChatControls(chatId);
        }
    }
}