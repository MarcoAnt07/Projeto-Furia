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
            } else if ("socialMedia".equals(data)) {
                sendSocialMedia(chatId);
            } else if ("history".equals(data)) {
                sendHistory(chatId);
            } else if ("cheerleadingSimulator".equals(data)) {
                sendCheerleadingSimulator(chatId);
            } else if (data.startsWith("fanChant")) {
                handleCheer(chatId, data);
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

        InlineKeyboardButton cheerleadingSimulator = new InlineKeyboardButton("Simulador de Torcida");
        cheerleadingSimulator.setCallbackData("cheerleadingSimulator");

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

        List<InlineKeyboardButton> row5 = new ArrayList<>();
        row5.add(cheerleadingSimulator);
        rowsInLine.add(row5);

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
        message.setParseMode("HTML");
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
                "DreamHackMastersSpringNorthAmerica2020.jpg",
                "ElisaMastersEspoo2023.jpg",
                "ArcticInvitational2019.jpg"
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

    private void sendSocialMedia(Long chatId){

        String[] linksSocialMedias = {
                "Instagram: instagram.com/furiagg",
                "X: x.com/FURIA",
                "Tik Tok: tiktok.com/@furiagg",
                "YouTube: youtube.com/@FURIAgg",
                "Twitch: twitch.tv/team/furia"
        };

        for(int i = 0; i < linksSocialMedias.length; i++){
            sendMessage(chatId, linksSocialMedias[i]);
        }

        sendWelcomeMessage(chatId);
    }

    private void sendHistory(Long chatId){

        String[] historyDividedByEpochs = {
            "A FURIA foi fundada em 2017 como uma organização de eSports brasileira, com foco em desenvolver talentos locais.\nA line-up de CS:GO começou a ganhar destaque com a contratação de KSCERATO e yuurih, dois dos jogadores mais constantes da organização até hoje.",
                "A FURIA entrou no cenário competitivo de Counter-Strike: Global Offensive ainda em 2017, mas só começou a ganhar destaque internacional em 2019, quando foi vice-campeã da DreamHack Open Rio e campeã do Arctic Invitational.\nCom um time jovem e promissor, a equipe ficou conhecida pelo estilo agressivo e criativo de jogo, liderado por jogadores como yuurih, KSCERATO e arT.",
                "Em 2020, durante a pandemia, a FURIA se destacou na região da América do Norte, vencendo torneios como: ESL Pro League Season 12: North America e DreamHack Masters Spring NA\nA equipe chegou ao Top 3 no ranking mundial da HLTV, algo raro para times sul-americanos.",
                "A FURIA chegou à semifinal do IEM Rio Major, o primeiro Major de CS no Brasil.\nJogando com o apoio massivo da torcida, a campanha foi histórica e consolidou a equipe como a maior representante brasileira no CS naquele momento.",
                "Além do CS:GO/CS2, a FURIA expandiu para outras modalidades: League of Legends (LoL), VALORANT, Apex Legends, Chess (Xadrez), com o GM Krikor Mekhitarian, FIFA, além também da Kings League.",
                "A FURIA conta com centros de treinamento nos EUA e no Brasil, além de iniciativas como a FURIA Academy, voltada à formação de novos talentos.",
                "A organização é reconhecida por seu marketing ousado, visual marcante (com a pantera negra) e por buscar impacto além do servidor, com foco em educação, performance e mentalidade vencedora."
        };

        String[] epochs = {
                "<b>Fundação</b>",
                "<b>Início no CS:GO</b>",
                "<b>Internacionalização</b>",
                "<b>Momento Histórico: IEM Rio Major (2022)</b>",
                "<b>Outras Modalidades</b>",
                "<b>Infraestrutura</b>",
                "<b>Identidade</b>"
        };

        String[] images = {
                "FundacaoFuria.jpg",
                "PrimeiraLineUp.jpg",
                "Top3HLTV.jpg",
                "IEMRioMajor2022.jpg",
                "FuriaKingsLeague.jpg",
                "InfraFuria.jpg",
                "LogoFuria.png"
        };

        for(int i = 0; i < images.length; i++){
            try(InputStream is = getClass().getClassLoader().getResourceAsStream("images/" + images[i])){
                if(is != null){
                    sendMessage(chatId, epochs[i]);

                    InputFile photo = new InputFile(is, images[i]);

                    SendPhoto sendPhoto = new SendPhoto();
                    sendPhoto.setChatId(chatId.toString());
                    sendPhoto.setPhoto(photo);
                    sendPhoto.setCaption(historyDividedByEpochs[i]);

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

    private void sendCheerleadingSimulator(Long chatId){

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Bem vindo a torcida da FURIA!! Vamos torcer juntos!!\nEscolha seu grito de torcida:");

        InlineKeyboardButton fanChant1 = new InlineKeyboardButton("\uD83D\uDC4F\uD83D\uDC4F UH! É FURIA! \uD83D\uDC4F\uD83D\uDC4F");
        fanChant1.setCallbackData("fanChant1");

        InlineKeyboardButton fanChant2 = new InlineKeyboardButton("VAMO FURIA! ⚔\uFE0F\uD83C\uDDE7\uD83C\uDDF7\uD83D\uDE80");
        fanChant2.setCallbackData("fanChant2");

        InlineKeyboardButton fanChant3 = new InlineKeyboardButton("AQUI É BRASIL! \uD83D\uDC9A\uD83C\uDDE7\uD83C\uDDF7\uD83D\uDC9B");
        fanChant3.setCallbackData("fanChant3");

        InlineKeyboardButton fanChant4 = new InlineKeyboardButton("PAN-TE-RA! PAN-TE-RA! \uD83D\uDC06\uD83D\uDD25\uD83D\uDDA4");
        fanChant4.setCallbackData("fanChant4");

        InlineKeyboardButton fanChant5 = new InlineKeyboardButton("FURIA, FURIA, FURIA! \uD83D\uDD0A\uD83D\uDCA3\uD83D\uDC3E");
        fanChant5.setCallbackData("fanChant5");

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        rows.add(List.of(fanChant1));
        rows.add(List.of(fanChant2));
        rows.add(List.of(fanChant3));
        rows.add(List.of(fanChant4));
        rows.add(List.of(fanChant5));

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);

        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleCheer(Long chatId, String action){
        switch (action){
            case "fanChant1":
                sendMessage(chatId, "\uD83D\uDC4F\uD83D\uDC4F UH! É FURIA! \uD83D\uDC4F\uD83D\uDC4F");
                break;

            case "fanChant2":
                sendMessage(chatId, "VAMO FURIA! ⚔\uFE0F\uD83C\uDDE7\uD83C\uDDF7\uD83D\uDE80");
                break;

            case "fanChant3":
                sendMessage(chatId, "AQUI É BRASIL! \uD83D\uDC9A\uD83C\uDDE7\uD83C\uDDF7\uD83D\uDC9B");
                break;

            case "fanChant4":
                sendMessage(chatId, "PAN-TE-RA! PAN-TE-RA! \uD83D\uDC06\uD83D\uDD25\uD83D\uDDA4");
                break;

            case "fanChant5":
                sendMessage(chatId, "FURIA, FURIA, FURIA! \uD83D\uDD0A\uD83D\uDCA3\uD83D\uDC3E");
                break;
        }

        sendCheerleadingSimulator(chatId);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
}
