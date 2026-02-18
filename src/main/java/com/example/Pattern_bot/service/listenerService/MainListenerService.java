package com.example.Pattern_bot.service.listenerService;

import com.example.Pattern_bot.command.needed.CommandContainer;
import com.example.Pattern_bot.session.SessionManager;
import com.example.Pattern_bot.session.UserSession;
import com.example.Pattern_bot.listener.menus.ChatControlMenu;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MainListenerService {

    private final TelegramBot telegramBot;
    private final CommandContainer commandContainer;
    private final SessionManager sessionManager;
    private final String prefix = "/";
    private final ChatControlMenu chatControlMenu;

    public void dontUnderstand(Long userChatId) {
        telegramBot.execute(new SendMessage(
                userChatId,
                "I don't understand for U.\nPlease try write again."
        ));
    }

    public void workWithText(String text, Update update) {
        //if (text.startsWith(prefix)) {
            //commandContainer.process(text, update);
        //} else {
            long chatId = update.message().chat().id();
            UserSession session = sessionManager.getSession(chatId);

            if (session != null && session.getPartnerChatId() != null) {
                try {
                    Long partnerChatId = Long.parseLong(session.getPartnerChatId());
                    String messageToPartner = "<b>💬 Сообщение от собеседника:</b>\n\n" + escapeHtml(text);
                    telegramBot.execute(new SendMessage(partnerChatId, messageToPartner)
                            .parseMode(ParseMode.valueOf("HTML")));
            } catch (NumberFormatException e) {
                    log.error("Error parsing partner chat ID", e);
                    sendTextMessage(chatId, "❌ Ошибка при отправке сообщения.");
                }
            } else {
                sendTextMessage(chatId,
                        "💡 Вы не в диалоге. Для начала общения найдите собеседника через меню.");
                chatControlMenu.sendChatControls(chatId);
            }

            //dontUnderstand(update.message().chat().id());
        //}

    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    public void workWithButton(Update update) {
        String callbackData = update.callbackQuery().data();
        if (callbackData.startsWith(prefix)) {
            commandContainer.process(callbackData, update);
        }
    }

    private void sendTextMessage(long chatId, String text) {
        telegramBot.execute(new SendMessage(chatId, text));
    }
}
