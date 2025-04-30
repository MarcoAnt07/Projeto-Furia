package io.github.marcoant07.telegrambot.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.InputStream;
import java.util.ArrayList;
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

            if ("team".equals(data)) {
                sendLineUp(chatId);
            } else if ("mainAchievements".equals(data)) {
                sendMainAchievements(chatId);
            }

        } else if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if ("/start".equals(messageText)) {
                sendMessage(chatId, "Bem vindo!!");
                sendWelcomeMessage(chatId);
            } else {
                sendMessage(chatId, "Você disse: " + messageText);
            }
        }
    }

    private void sendWelcomeMessage(Long chatId) {

        SendMessage message = new SendMessage();

        message.setChatId(chatId);
        message.setText("Escolha uma opção abaixo:");

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

    private void sendLineUp(Long chatId){
        String[] images = {
               "FalleN.jpg",
                "KSCERATO.jpg",
                "molodoy.jpg",
                "YEKINDAR.jpg",
                "yuurih.jpg",
                "sidde.jpg"
        };

        String[] textsForImages = {
                "Gabriel \"FalleN\" Toledo. Capitão da equipe e considerado por muitos o melhor jogador do mundo de CS. Joga de Rifler.",
                "Kaike \"KSCERATO\" Cerato. Brasileiro jogador profissional de Counter-Strike 2 e ex-jogador de CS:GO. Kauan \"KNCERATO\" irmão de Cerato e Adriano \"WOOD7\" primo de Cerato. Joga de Rifler.",
                "Danil \"molodoy\" Golubenko. Cazaquistanês jogador profissional de Counter-Strike 2 e ex-jogador de CS:GO. Joga de AWPer.",
                "Mareks \"YEKINDAR\" Gaļinskis. Letão jogador profissional de Counter-Strike 2 e ex-jogador de CS:GO. Atualmente é Stand-in.",
                "Yuri \"yuurih\" Boian. Brasileiro jogador profissional de Counter-Strike 2 e ex-jogador de CS:GO. Joga de Rifler.",
                "Sidnei \"sidde\" Macedo. Tecnico brasileiro profissional de Counter-Strike 2 e ex-tecnico de CS:GO."
        };

        for(int i = 0; i < images.length; i++){
            try(InputStream is = getClass().getClassLoader().getResourceAsStream("images/" + images[i])){
                if(is != null){
                    InputFile photo = new InputFile(is, images[i]);

                    SendPhoto sendPhoto = new SendPhoto();
                    sendPhoto.setChatId(chatId.toString());
                    sendPhoto.setPhoto(photo);
                    sendPhoto.setCaption(textsForImages[i]);

                    execute(sendPhoto);

                    Thread.sleep(500);
                }
                else {
                    System.err.println("Imagem não encontrada: " + images[i]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sendWelcomeMessage(chatId);
    }

    private void sendMainAchievements(Long chatId){

        String[] images = {
                "ESLProLeague.jpg",
                "DreamHack Masters Spring North America (2020).jpg",
                "Elisa Masters Espoo (2023).jpg",
                "Arctic Invitational (2019).jpg"
        };

        String[] textsForImages = {
                "FURIA venceu a equipe 100 Thieves na grande final, conquistando o título da ESL Pro League Season 12 na região da América do Norte.",
                "FURIA derrotou a Team Liquid por 3-0 na final, garantindo o título do DreamHack Masters Spring 2020 na América do Norte.",
                "FURIA venceu a equipe Apeks por 3-1 na final, encerrando um jejum de títulos em LANs internacionais desde 2019.",
                "FURIA conquistou o título ao vencer a equipe CR4ZY na final do Arctic Invitational 2019, realizado em Helsinque, Finlândia."
        };

        for(int i = 0; i < images.length; i++){
            try(InputStream is = getClass().getClassLoader().getResourceAsStream("images/" + images[i])){
                if(is != null){
                    InputFile photo = new InputFile(is, images[i]);

                    SendPhoto sendPhoto = new SendPhoto();
                    sendPhoto.setChatId(chatId.toString());
                    sendPhoto.setPhoto(photo);
                    sendPhoto.setCaption(textsForImages[i]);

                    execute(sendPhoto);

                    Thread.sleep(500);
                }
                else {
                    System.err.println("Imagem não encontrada: " + images[i]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sendWelcomeMessage(chatId);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
}
