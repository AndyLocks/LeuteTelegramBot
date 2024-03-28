package com.leute.leute_telegram_bot.controller;

import com.leute.leute_telegram_bot.controller.command.FindUserCommand;
import com.leute.leute_telegram_bot.controller.command.ListenerAdapter;
import com.leute.leute_telegram_bot.controller.command.StartCommand;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class Bot extends TelegramLongPollingBot {
    private String token;
    private String name;

    private List<ListenerAdapter> commands = new ArrayList<>();
    public Bot() {
        Dotenv config = Dotenv.configure().load();
        this.token = config.get("TELEGRAM_BOT_TOKEN");
        this.name = config.get("TELEGRAM_BOT_NAME");

        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("/start", "start"));
        this.commands.add(new StartCommand("/start", this));

        commands.add(new BotCommand("/find_user", "finds user by nickname"));
        this.commands.add(new FindUserCommand("/find_user", this));

        try {
            this.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return this.name;
    }

    @Override
    public String getBotToken() {
        return this.token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            this.commands.forEach(e -> e.checkCommandAndExecute(update.getMessage().getText(), update));
        }
        catch (NullPointerException ignored) {
        }
    }
}
