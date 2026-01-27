//package com.example.Pattern_bot.command.example;
//
//import com.example.Pattern_bot.command.abstractCommands.AbstractCommand;
//import com.example.Pattern_bot.command.annotation.BotCommand;
//import com.example.Pattern_bot.listener.menus.MainMenu;
//import com.pengrad.telegrambot.TelegramBot;
//import com.pengrad.telegrambot.model.Update;
//
///**
// * Пример универсальной команды, которая может обрабатывать как текстовые сообщения, так и callback-запросы.
// * Использует аннотацию @BotCommand для автоматической регистрации.
// * Наследуется от AbstractCommand для возможности обработки обоих типов обновлений.
// */
//@BotCommand(command = "/universal")
//public class UniversalCommandExample extends AbstractCommand {
//
//    private final MainMenu menu;
//
//    /**
//     * Конструктор с указанием TelegramBot и MainMenu.
//     *
//     * @param telegramBot экземпляр TelegramBot
//     * @param menu экземпляр MainMenu
//     */
//    public UniversalCommandExample(TelegramBot telegramBot, MainMenu menu) {
//        super(telegramBot);
//        this.menu = menu;
//    }
//
//    /**
//     * Обработка текстового сообщения.
//     * Этот метод вызывается только если update содержит текстовое сообщение.
//     *
//     * @param update объект обновления от Telegram
//     */
//    @Override
//    protected void handleMessage(Update update) {
//        long chatId = update.message().chat().id();
//        String username = update.message().chat().username();
//
//        sendTextMessage(chatId, "Привет, " + username + "! Вы активировали универсальную команду через текстовое сообщение.");
//        menu.sendMessage(chatId);
//    }
//
//    /**
//     * Обработка callback-запроса.
//     * Этот метод вызывается только если update содержит callback-запрос.
//     *
//     * @param update объект обновления от Telegram
//     */
//    @Override
//    protected void handleCallbackQuery(Update update) {
//        long chatId = update.callbackQuery().from().id();
//        String text = "Вы активировали универсальную команду через callback-запрос.";
//
//        sendTextMessage(chatId, text);
//        menu.sendMessage(chatId);
//    }
//
//
//}
