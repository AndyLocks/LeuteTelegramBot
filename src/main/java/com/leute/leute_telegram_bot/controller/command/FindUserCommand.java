package com.leute.leute_telegram_bot.controller.command;

import com.leute.leute_telegram_bot.controller.Bot;
import com.leute.leute_telegram_bot.service.Api;
import com.leute.leute_telegram_bot.service.model.Account;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class FindUserCommand extends ListenerAdapter {
    public FindUserCommand(String command, Bot bot) {
        super(command, bot);
    }

    @Override
    public void execute(Update update, String... args) {
        if (args.length == 0) {
            sendMessageToCurrentChat(update, "There is no argument.\nExample: /find_user <nickname>");
            return;
        }
        Account account = Api.getAccount(args[0]);

        if (account == null) {
            sendMessageToCurrentChat(update, "Not Found");
        }

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        StringBuilder stringBuilder = new StringBuilder(account.getNickname());
        stringBuilder.append("\n").append(account.getDescription());

        if (account.getDiscordUserId() != null) {
            stringBuilder.append("\n\n").append("Discord nickname: ").append(account.getDiscordNickname());
        }

        sendMessage.setText(stringBuilder.toString());

        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(update.getMessage().getChatId());
        try {
            sendPhoto.setPhoto(new InputFile(new URL(account.getImageUrl()).openStream(), "user_logo"));
        }
        catch (MalformedURLException ignored) {
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            bot.execute(sendMessage);
            bot.execute(sendPhoto);
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
