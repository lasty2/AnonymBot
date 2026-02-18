package com.example.Pattern_bot.command.commandHeap;

import com.example.Pattern_bot.command.abstractCommands.CallbackCommand;
import com.example.Pattern_bot.command.annotation.BotCommand;
import com.example.Pattern_bot.listener.menus.ChatControlMenu;
import com.example.Pattern_bot.listener.menus.PreferredGenderMenu;
import com.example.Pattern_bot.session.UserSession;
import com.example.Pattern_bot.session.SessionManager;
import com.example.Pattern_bot.listener.menus.GenderMenu;
import com.example.Pattern_bot.service.UserService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;

@BotCommand(command = "/search_partner")
public class SearchPartnerCommand extends CallbackCommand {

    private final SessionManager sessionManager;
    private final GenderMenu genderMenu;
    private final ChatControlMenu chatControlMenu;
    private final PreferredGenderMenu preferredGenderMenu;
    private final UserService userService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final ConcurrentHashMap<Long, ScheduledFuture<?>> searchTasks = new ConcurrentHashMap<>();

    public SearchPartnerCommand(TelegramBot telegramBot,
                                SessionManager sessionManager,
                                GenderMenu genderMenu,
                                ChatControlMenu chatControlMenu,
                                PreferredGenderMenu preferredGenderMenu,
                                UserService userService) {
        super(telegramBot);
        this.sessionManager = sessionManager;
        this.genderMenu = genderMenu;
        this.chatControlMenu = chatControlMenu;
        this.preferredGenderMenu = preferredGenderMenu;
        this.userService = userService;
    }

    @Override
    protected void handleCallbackQuery(Update update) {
        long chatId = getChatId(update);
        UserSession session = sessionManager.getSession(chatId);

        if (session == null) {
            sendTextMessage(chatId, "⚠️ Сначала выберите ваш пол!");
            genderMenu.sendGenderSelection(chatId);
            return;
        }

        if (!session.hasSelectedPreferences()) {
            sendTextMessage(chatId, "⚠️ Сначала выберите, кого вы хотите искать!");
            preferredGenderMenu.sendPreferredGenderSelection(chatId);
            return;
        }

        if (session.getPartnerChatId() != null) {
            sendTextMessage(chatId, "❌ Вы уже в диалоге! Завершите текущий диалог перед поиском нового собеседника.");
            return;
        }

        userService.incrementCountUses(chatId);

        // Отменяем предыдущие задачи поиска для этого пользователя
        cancelSearchTask(chatId);

        session.setSearching(true);
        sessionManager.updateSession(session);

        chatControlMenu.sendSearchingMenu(chatId);

        // Запускаем поиск собеседника
        ScheduledFuture<?> future = scheduler.schedule(() -> findPartnerForUser(chatId, session), 2, TimeUnit.SECONDS);
        searchTasks.put(chatId, future);
    }

    private void findPartnerForUser(long chatId, UserSession session) {
        // Проверяем, что пользователь все еще ищет
        UserSession currentSession = sessionManager.getSession(chatId);
        if (currentSession == null || !currentSession.isSearching()) {
            searchTasks.remove(chatId);
            return;
        }

        Long partnerChatId = sessionManager.findPartner(session);

        if (partnerChatId != null) {
            // Нашли собеседника
            UserSession partnerSession = sessionManager.getSession(partnerChatId);

            // Проверяем, что собеседник все еще ищет
            if (partnerSession == null || !partnerSession.isSearching()) {
                // Собеседник уже прекратил поиск, пробуем снова через 5 секунд
                ScheduledFuture<?> future = scheduler.schedule(() -> findPartnerForUser(chatId, session), 5, TimeUnit.SECONDS);
                searchTasks.put(chatId, future);
                return;
            }

            // Отменяем поисковые задачи для обоих пользователей
            cancelSearchTask(chatId);
            cancelSearchTask(partnerChatId);

            // Связываем пользователей
            session.setPartnerChatId(partnerChatId.toString());
            session.setSearching(false);

            partnerSession.setPartnerChatId(String.valueOf(chatId));
            partnerSession.setSearching(false);

            sessionManager.updateSession(session);
            sessionManager.updateSession(partnerSession);

            // Уведомляем обоих пользователей
            sendTextMessage(chatId, """
                    ✅ Собеседник найден! Начинайте общение.'""");
            chatControlMenu.sendChattingMenu(chatId);

            sendTextMessage(partnerChatId, """
                    ✅ Собеседник найден! Начинайте общение.""");
            chatControlMenu.sendChattingMenu(partnerChatId);


        } else {
            sendTextMessage(chatId, "😔 Пока нет доступных собеседников.\n" +
                    "Бот продолжит поиск. Вы можете остановить поиск в любое время.");

            // Планируем повторный поиск через 10 секунд
            ScheduledFuture<?> future = scheduler.schedule(() -> findPartnerForUser(chatId, session), 10, TimeUnit.SECONDS);
            searchTasks.put(chatId, future);
        }
    }

    private void cancelSearchTask(long chatId) {
        ScheduledFuture<?> task = searchTasks.remove(chatId);
        if (task != null && !task.isDone()) {
            task.cancel(false);
        }
    }
}
