package com.example.Pattern_bot.command.commandHeap;

import com.example.Pattern_bot.command.abstractCommands.CallbackCommand;
import com.example.Pattern_bot.command.annotation.BotCommand;
import com.example.Pattern_bot.listener.menus.GenderMenu;
import com.example.Pattern_bot.session.UserSession;
import com.example.Pattern_bot.session.SessionManager;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

@BotCommand(command = "/change_gender")
public class ChangeGenderCommand extends CallbackCommand {

    private final SessionManager sessionManager;
    private final GenderMenu genderMenu;

    public ChangeGenderCommand(TelegramBot telegramBot,
                               SessionManager sessionManager,
                               GenderMenu genderMenu) {
        super(telegramBot);
        this.sessionManager = sessionManager;
        this.genderMenu = genderMenu;
    }

    @Override
    protected void handleCallbackQuery(Update update) {
        long chatId = getChatId(update);
        UserSession session = sessionManager.getSession(chatId);

        if (session == null) {
            sendTextMessage(chatId, "⚠️ Сначала начните работу с ботом через /start");
            return;
        }

        // Если пользователь в диалоге, предупреждаем его
        if (session.getPartnerChatId() != null) {
            sendTextMessage(chatId, "⚠️ Вы находитесь в активном диалоге!\n" +
                    "Завершите текущий диалог перед сменой пола.");
            return;
        }

        // Если пользователь в поиске, останавливаем поиск
        if (session.isSearching()) {
            session.setSearching(false);
            sessionManager.updateSession(session);
        }

        // Сбрасываем предпочтения при смене пола
        session.setPreferredGender(null);
        sessionManager.updateSession(session);

        sendTextMessage(chatId, """
                🔄 *Смена пола*
                
                Выберите ваш новый пол.
                
                *Примечание:* При смене пола предпочтения для поиска будут сброшены.
                """);

        genderMenu.sendGenderSelection(chatId);
    }
}