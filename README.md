# 🤖 FURIA CS Fan Chat - Experiência Conversacional

Este é um projeto de chat desenvolvido em Java (JDK 21) com Spring Boot e a [Telegram Bot API](https://core.telegram.org/bots/api), voltado para fãs do time de CS da FURIA. O bot oferece uma experiência interativa e imersiva, com funcionalidades que permitem explorar a história, line-up, conquistas, redes sociais e até simular a torcida pelo time.

## ⚙️ Tecnologias Utilizadas

- Java 21
- Spring Boot
- TelegramBots
- Telegram Bot API
- Maven

## 🚀 Funcionalidades do Bot

### 📜 Comando `/start`
Ao iniciar uma conversa com o chat, o usuário recebe uma saudação e um **menu principal interativo**, com botões para explorar as seguintes funcionalidades:

### 1. 📋 **Line-up Atual**
- Exibe as imagens e descrições dos jogadores e do técnico atual da equipe de CS da FURIA.
- As imagens estão armazenadas em `resources/images/`.
- Jogadores: FalleN, KSCERATO, molodoy, YEKINDAR, yuurih.
- Técnico: sidde.

### 2. 🏆 **Principais Conquistas**
- Mostra as principais conquistas do time em competições como:
  - ESL Pro League Season 12 (NA)
  - DreamHack Masters Spring 2020 (NA)
  - Elisa Masters Espoo 2023
  - Arctic Invitational 2019

Cada conquista é acompanhada de uma imagem e uma breve descrição do torneio vencido.

### 3. 📖 **História do Time**
- Apresenta a trajetória da FURIA desde sua fundação até os dias atuais, dividida por épocas:
  - Fundação
  - Início no CS:GO
  - Consolidação internacional
  - Destaque no IEM Rio Major 2022
  - Outras modalidades além do CS
  - Estrutura e centros de treinamento
  - Identidade e cultura da organização

Cada seção vem acompanhada de uma imagem e um texto explicativo.

### 4. 📱 **Redes Sociais**
Botões com links diretos para as principais redes sociais da FURIA:
- [Instagram](https://instagram.com/furiagg)
- [X (Twitter)](https://x.com/FURIA)
- [TikTok](https://tiktok.com/@furiagg)
- [YouTube](https://youtube.com/@FURIAgg)
- [Twitch](https://twitch.tv/team/furia)

### 5. 🙌 **Simulador de Torcida**
- O usuário pode escolher entre diferentes gritos de torcida:
  - 👏👏 UH! É FURIA! 👏👏
  - VAMO FURIA! ⚔️🇧🇷🚀
  - AQUI É BRASIL! 💚🇧🇷💛
  - PAN-TE-RA! 🐆🔥🖤
  - FURIA, FURIA, FURIA! 🔊💣🐾

Após selecionar um grito, o bot responde com a mensagem correspondente e retorna ao menu do simulador.

## 📁 Estrutura do Projeto

```
src/
└── main/
    ├── java/
    │   └── io.github.marcoant07.telegrambot.component/
    │       └── Bot.java         # Classe do bot (usada pelo Spring)
    └── resources/
        └── images/              # Imagens utilizadas nas mensagens (jogadores, conquistas, etc.)
```

## 🔑 Configuração do Bot

As credenciais do bot devem ser definidas no arquivo `application.properties`:

```properties
telegrambots.botToken=SEU_TOKEN_DO_BOT
telegrambots.botUsername=SEU_NOME_DE_USUARIO_DO_BOT
```

> ✅ *Se o projeto já estiver configurado com credenciais válidas, **não é necessário alterá-las** para rodar o bot.*

## 🧠 Lógica de Funcionamento

- A classe `Bot` estende `TelegramLongPollingBot` e é anotada com `@Component`, integrando-se ao Spring Boot.
- O método `onUpdateReceived` trata mensagens e callbacks:
  - `/start` chama `sendWelcomeMessage`.
  - Mensagens inválidas chamam `sendInvalidCommandMessage`.
  - Clique nos botões interativos é tratado via `CallbackQuery`.

## 🐞 Tratamento de Erros

- Exceções da API do Telegram são capturadas com `try-catch` em todas as chamadas de `execute()`.
- Caso uma imagem não seja encontrada no classpath, uma mensagem de erro é exibida no console.

## ▶️ Como Rodar o Projeto

1. **Clone o repositório:**

   ```bash
   git clone https://github.com/MarcoAnt07/Projeto-Furia.git
   cd furia-cs-fan-bot
   ```

2. **Abra o projeto no IntelliJ IDEA.**

3. **(Opcional)**: Configure o arquivo `application.properties` com seu token e nome de usuário:

   ```
   src/main/resources/application.properties
   ```

4. **Execute a classe `TelegramBotApplication` no IntelliJ para iniciar o bot.**

   Ou, se preferir via terminal:

   ```bash
   ./mvnw spring-boot:run
   ```

5. **Acesse seu bot pelo Telegram e envie o comando `/start` para interagir.**

## 🧪 Possíveis Melhorias Futuras

- Adição de mensagens de áudio com gritos de torcida reais.
- Respostas com stickers personalizados.
- Banco de dados para armazenar estatísticas de uso.
- Integração com API da HLTV para dados ao vivo.
