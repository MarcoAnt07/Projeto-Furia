package io.github.marcoant07.telegrambot.configuration;

import io.github.marcoant07.telegrambot.component.Bot;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BotConfig {

    private final Bot bot;

    public BotConfig(Bot bot){
        this.bot = bot;
    }

    @PostConstruct
    public void init() throws Exception {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(bot);
    }
}
