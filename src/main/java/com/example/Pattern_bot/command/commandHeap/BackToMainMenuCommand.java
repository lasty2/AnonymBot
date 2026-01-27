package com.example.Pattern_bot.command.commandHeap;

import com.example.Pattern_bot.command.abstractCommands.CallbackCommand;
import com.example.Pattern_bot.command.annotation.BotCommand;
import com.example.Pattern_bot.listener.menus.MainMenu;
import com.example.Pattern_bot.session.UserSession;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.example.Pattern_bot.session.SessionManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@BotCommand(command = "/main_menu")
public class BackToMainMenuCommand extends CallbackCommand {

    private final MainMenu mainMenu;
    private final SessionManager sessionManager;

    public BackToMainMenuCommand(TelegramBot telegramBot,
                                 MainMenu mainMenu,
                                 SessionManager sessionManager) {
        super(telegramBot);
        this.mainMenu = mainMenu;
        this.sessionManager = sessionManager;
    }

    @Override
    protected void handleCallbackQuery(Update update) {

        long chatId = getChatId(update);

        // Если пользователь в диалоге, предупреждаем его
        UserSession session = sessionManager.getSession(chatId);
        if (session != null && session.getPartnerChatId() != null) {
            sendTextMessage(chatId, "⚠️ Вы находитесь в активном диалоге!\n" +
                    "Завершите текущий диалог перед возвратом в главное меню.");
            return;
        }

        mainMenu.sendWelcomeMessage(chatId);
    }
}