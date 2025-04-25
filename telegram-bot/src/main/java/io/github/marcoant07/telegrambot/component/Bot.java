package io.github.marcoant07.telegrambot.component;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class Bot extends TelegramLongPollingBot {


    public void processNonCommandUpdate(Update update){
        if (update.hasMessage() && update.getMessage().hasText()){
            String chatId = update.getMessage().getChatId().toString();
            String message = update.getMessage().getText();

            SendMessage response = new SendMessage(chatId, "You talk: " +  message);

            try{
                execute(response);
            } catch (TelegramApiException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public String getBotUsername() {
        return "FuriaCS_testBot";
    }
}
