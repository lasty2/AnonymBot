//package com.example.Pattern_bot.command.example;
//
//import com.example.Pattern_bot.command.abstractCommands.CallbackCommand;
//import com.example.Pattern_bot.command.annotation.BotCommand;
//import com.example.Pattern_bot.listener.menus.MainMenu;
//import com.pengrad.telegrambot.TelegramBot;
//import com.pengrad.telegrambot.model.Update;
//import org.springframework.beans.factory.annotation.Autowired;
//
///**
// * Пример команды, обрабатывающей callback-запросы.
// * Использует аннотацию @BotCommand для автоматической регистрации.
// * Наследуется от CallbackCommand для упрощения обработки callback-запросов.
// */
//@BotCommand(command = "/help")
//public class HelpCommandExample extends CallbackCommand {
//
//    private MainMenu mainMenu;
//
//    /**
//     * Конструктор с указанием TelegramBot.
//     *
//     * @param telegramBot экземпляр TelegramBot
//     */
//    public HelpCommandExample(TelegramBot telegramBot) {
//        super(telegramBot);
//    }
//
//    @Autowired
//    public void setMainMenu(MainMenu mainMenu) {
//        this.mainMenu = mainMenu;
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
//        // Получаем ID чата
//        long chatId = getChatId(update);
//
//        // Отправляем сообщение с информацией о боте
//        sendTextMessage(chatId, "Здесь основная информация по боту:\n" +
//                "1. Используйте /start для начала работы\n" +
//                "2. Выберите тему викторины в главном меню\n" +
//                "3. Отвечайте на вопросы и получайте баллы");
//
//        mainMenu.sendMessage(chatId);
//
//    }
//}