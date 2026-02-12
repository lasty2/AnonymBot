package com.example.Pattern_bot.command.commandHeap;

import com.example.Pattern_bot.command.abstractCommands.MessageCommand;
import com.example.Pattern_bot.command.annotation.BotCommand;
import com.example.Pattern_bot.listener.menus.ChatControlMenu;
import com.example.Pattern_bot.listener.menus.MainMenu;
import com.example.Pattern_bot.model.UserEntity;
import com.example.Pattern_bot.service.UserService;
import com.example.Pattern_bot.session.UserSession;
import com.example.Pattern_bot.session.SessionManager;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@BotCommand(command = "/start")
public class StartCommand extends MessageCommand {

    private final MainMenu mainMenu;
    private final UserService userService;
    private final SessionManager sessionManager;
    private final ChatControlMenu chatControlMenu;

    public StartCommand(TelegramBot telegramBot,
                        MainMenu mainMenu,
                        UserService userService,
                        SessionManager sessionManager,
                        ChatControlMenu chatControlMenu) {
        super(telegramBot);
        this.mainMenu = mainMenu;
        this.userService = userService;
        this.sessionManager = sessionManager;
        this.chatControlMenu = chatControlMenu;
    }

    @Override
    protected void handleMessage(Update update) {
        long chatId = getChatId(update);
        String username = getUsername(update);

        userService.createOrUpdateUser(chatId, username);
        UserSession session = loadOrCreateSession(chatId, username);

        String greeting = String.format("""
            Привет, %s! 👋
            
            Добро пожаловать в анонимный чат!
            Здесь вы можете общаться с незнакомцами, сохраняя полную анонимность.
            """, username != null ? username : "друг");

        sendTextMessage(chatId, greeting);

        if (session.hasSelectedPreferences()) {
            chatControlMenu.sendChatControls(chatId);
        } else {
            mainMenu.sendWelcomeMessage(chatId);}

    }

    private UserSession loadOrCreateSession(Long chatId, String username) {
        UserSession session = sessionManager.getSession(chatId);

        if (session != null) {
            log.info("Session already exists for chatId: {}", chatId);
            return session;}

        Optional<UserEntity> userOpt = userService.getUserByChatId(chatId);

            if (userOpt.isPresent()) {
                UserEntity user = userOpt.get();
                log.info("Loading existing user from DB for chatId: {} with gender: {}, pref: {}",
                        chatId, user.getGender(), user.getPreferredGender());
                session = new UserSession(chatId, username);
                if (user.getGender() != null) {session.setGender(user.getGender());}
                if (user.getPreferredGender() != null) {session.setPreferredGender(user.getPreferredGender());}
                sessionManager.updateSession(session);
                log.info("Loaded existing user preferences for chatId: {} - gender: {}, pref: {}",
                        chatId, user.getGender(), user.getPreferredGender());
            } else {session = sessionManager.createOrUpdateSession(chatId, username);}

        return session;
    }
}