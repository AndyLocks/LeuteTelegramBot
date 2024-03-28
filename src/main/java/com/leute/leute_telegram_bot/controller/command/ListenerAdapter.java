package com.leute.leute_telegram_bot.controller.command;

import com.leute.leute_telegram_bot.controller.Bot;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public abstract class ListenerAdapter {
    protected Bot bot;
    protected String command;

    public ListenerAdapter(String command, Bot bot) {
        this.command = command;
        this.bot = bot;
    }

    public void checkCommandAndExecute(String command, Update update) {
        if(command.startsWith(this.command)) {
            String[] argumenst = command.split(" ");
            String[] result = new String[argumenst.length-1];

            for (int i = 1; i < argumenst.length; i++) {
                result[i-1] = argumenst[i];
            }

            this.execute(update, result);
        }
    }

    public abstract void execute(Update update, String... args);

    protected void sendMessageToCurrentChat(Update update, String message) {
        try {
            bot.execute(new SendMessage(Long.toString(update.getMessage().getChatId()), message));
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
