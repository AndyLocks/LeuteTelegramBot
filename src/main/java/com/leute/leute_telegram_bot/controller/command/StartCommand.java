package com.leute.leute_telegram_bot.controller.command;

import com.leute.leute_telegram_bot.controller.Bot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StartCommand extends ListenerAdapter {
    public StartCommand(String command, Bot bot) {
        super(command, bot);
    }

    @Override
    public void execute(Update update, String... args) {
        sendMessageToCurrentChat(update, "Start");
    }
}
