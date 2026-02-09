package com.example.Pattern_bot.command.commandHeap;

import com.example.Pattern_bot.command.abstractCommands.CallbackCommand;
import com.example.Pattern_bot.command.annotation.BotCommand;
import com.example.Pattern_bot.listener.menus.ChatControlMenu;
import com.example.Pattern_bot.session.UserSession;
import com.example.Pattern_bot.session.SessionManager;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@BotCommand(command = "/stop_search")
public class StopSearchCommand extends CallbackCommand {

    private final SessionManager sessionManager;
    private final ChatControlMenu chatControlMenu;

    public StopSearchCommand(TelegramBot telegramBot,
                             SessionManager sessionManager,
                             ChatControlMenu chatControlMenu,
                             SearchPartnerCommand searchPartnerCommand) { // Добавляем в конструктор
        super(telegramBot);
        this.sessionManager = sessionManager;
        this.chatControlMenu = chatControlMenu;
        // Добавляем зависимость
    }

    @Override
    protected void handleCallbackQuery(Update update) {
        long chatId = getChatId(update);
        UserSession session = sessionManager.getSession(chatId);

        if (session == null) {
            sendTextMessage(chatId, "⚠️ Сессия не найдена. Начните с команды /start");
            return;
        }

        if (!session.isSearching()) {
            sendTextMessage(chatId, "ℹ️ Вы не находитесь в поиске собеседника.");
            chatControlMenu.sendChatControls(chatId);
            return;
        }

        // Останавливаем поиск
        session.setSearching(false);
        sessionManager.updateSession(session);

        // Отменяем все запланированные задачи поиска для этого пользователя
        // Это сработает, если SearchPartnerCommand имеет публичный метод для отмены
        // Вместо этого можно использовать механизм событий Spring
        sendTextMessage(chatId, "🛑 Поиск собеседника остановлен.\n" +
                "Вы можете начать поиск снова в любое время.");

        chatControlMenu.sendChatControls(chatId);
    }
}