package io.github.marcoant07.telegrambot.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;


@Component
public class Bot extends TelegramLongPollingBot {

    private final String botToken;
    private final String botUsername;

    public Bot(
            @Value("${telegrambots.botToken}") String botToken,
            @Value("${telegrambots.botUsername}") String botUsername
    ) {
        super(botToken);
        this.botToken = botToken;
        this.botUsername = botUsername;
    }

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
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if(messageText.equals("/start")){
                sendWelcomeMessage(chatId);
            } else if (update.hasCallbackQuery()) {
                String data = update.getCallbackQuery().getData();
                chatId = update.getCallbackQuery().getMessage().getChatId();

                if("botao_clicado".equals(data)){
                    sendMessage(chatId, "Botão clicado");
                }
            }
        }
    }

    private void sendWelcomeMessage(Long chatId) {

        SendMessage message = new SendMessage();

        message.setChatId(chatId);
        message.setText("Bem vindo!");

        InlineKeyboardButton button = new InlineKeyboardButton("Clique aqui");
        button.setCallbackData("botao_clicado");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(Collections.singletonList(Collections.singletonList(button)));

        message.setReplyMarkup(markup);

        try{
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage(chatId.toString(), text);
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
}
