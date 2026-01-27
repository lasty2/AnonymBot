//package com.example.Pattern_bot.command.example;
//
//import com.example.Pattern_bot.command.annotation.BotCommand;
//import com.example.Pattern_bot.command.needed.Command;
//import com.example.Pattern_bot.listener.menus.MainMenu;
//import com.pengrad.telegrambot.TelegramBot;
//import com.pengrad.telegrambot.model.Update;
//import com.pengrad.telegrambot.request.SendMessage;
//import lombok.RequiredArgsConstructor;
//
//@BotCommand(command = "/custom_command")
//@RequiredArgsConstructor
//public class CustomBlankCommandExample implements Command {
//
//    private final TelegramBot telegramBot;
//    private final MainMenu mainMenu;
//
//    @Override
//    public void execute(Update update) {
//        String message =
//                "that is custom command";
//
//        Long chatId = update.callbackQuery().from().id();
//
//        telegramBot.execute(new SendMessage(
//                chatId,
//                message
//        ));
//
//        mainMenu.sendMessage(chatId);
//    }
//}
