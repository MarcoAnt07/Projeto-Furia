package io.github.marcoant07.telegrambot.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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
        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();

            if ("botao_clicado".equals(data)) {
                sendMessage(chatId, "Botão clicado");
            }

        } else if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if ("/start".equals(messageText)) {
                sendWelcomeMessage(chatId);
            } else {
                sendMessage(chatId, "Você disse: " + messageText);
            }
        }
    }

    private void sendWelcomeMessage(Long chatId) {

        SendMessage message = new SendMessage();

        message.setChatId(chatId);
        message.setText("Bem vindo!");

        InlineKeyboardButton team = new InlineKeyboardButton("Line up atual");
        team.setCallbackData("team");

        InlineKeyboardButton history = new InlineKeyboardButton("História do time de CS");
        history.setCallbackData("history");

        InlineKeyboardButton mainAchievements = new InlineKeyboardButton("Principais Conquistas");
        mainAchievements.setCallbackData("mainAchievements");

        InlineKeyboardButton socialMedia = new InlineKeyboardButton("Redes Sociais");
        socialMedia.setCallbackData("socialMedia");

        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(team);
        rowsInLine.add(row1);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(history);
        rowsInLine.add(row2);

        List<InlineKeyboardButton> row3 = new ArrayList<>();
        row3.add(mainAchievements);
        rowsInLine.add(row3);

        List<InlineKeyboardButton> row4 = new ArrayList<>();
        row4.add(socialMedia);
        rowsInLine.add(row4);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rowsInLine);

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
