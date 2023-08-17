package vn.dating.telegram.configs.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import vn.dating.telegram.mapper.UserMapper;
import vn.dating.telegram.model.Couple;
import vn.dating.telegram.model.CoupleStatus;
import vn.dating.telegram.model.User;
import vn.dating.telegram.services.CoupleService;
import vn.dating.telegram.services.UserService;


import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private UserService userService;

    @Autowired
    private CoupleService coupleService;


    final BotConfig config;

    static final String HELP_TEXT = "This bot is created to demonstrate Spring capabilities.\n\n" +
            "You can execute commands from the main menu on the left or by typing a command:\n\n" +
            "Type /start to see a welcome message\n\n" +
            "Type /mydata to see data stored about yourself\n\n" +
            "Type /help to see this message again";

    static final String YES_BUTTON = "YES_BUTTON";
    static final String NO_BUTTON = "NO_BUTTON";

    static final String ERROR_TEXT = "Error occurred: ";

    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> listofCommands = new ArrayList<>();
        listofCommands.add(new BotCommand("/start", "get a welcome message"));
        listofCommands.add(new BotCommand("/chat", "Start dating"));
        listofCommands.add(new BotCommand("/help", "info how to use this bot"));
        listofCommands.add(new BotCommand("/settings", "set your preferences"));
        try {
            this.execute(new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        long chatId = update.getMessage().getChatId();

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();


            if(messageText.contains("/start")){
                Optional<User> user = userService.findByChatId(chatId);
                if(user.isEmpty()){
                    String firstName = update.getMessage().getChat().getFirstName();
                    String lastName = update.getMessage().getChat().getLastName();
                    String username = update.getMessage().getChat().getUserName();
                    User newUser = new User();
                    newUser.setChatId(chatId);
                    newUser.setFirstName(firstName);
                    newUser.setLastName(lastName);
                    newUser.setUsername(username);
                    newUser.setCreatedAt(Instant.now());
                    newUser.setUpdatedAt(Instant.now());

                    newUser = userService.save(newUser);


                    prepareAndSendMessage(chatId, UserMapper.userDto(newUser).toString());
                }
                else{

                    prepareAndSendMessage(chatId,UserMapper.userDto(user.get()).toString());
                }
            }
            else if (messageText.contains("/chat")){

                Optional<User> currentUser = userService.findByChatId(chatId);
                if(currentUser.isEmpty()){
                    String firstName = update.getMessage().getChat().getFirstName();
                    String lastName = update.getMessage().getChat().getLastName();
                    String username = update.getMessage().getChat().getUserName();
                    User newUser = new User();
                    newUser.setChatId(chatId);
                    newUser.setFirstName(firstName);
                    newUser.setLastName(lastName);
                    newUser.setUsername(username);
                    newUser.setCreatedAt(Instant.now());
                    newUser.setUpdatedAt(Instant.now());

                    newUser = userService.save(newUser);

                }else{

                    boolean isUserJoined = coupleService.isUserJoined(currentUser.get());
                    boolean isUserPending = coupleService.checkIsUserPending(currentUser.get());


                    if(isUserPending ==false && isUserJoined == false){
                        Couple registerCouple = new Couple();

                        registerCouple.setUser1(currentUser.get());
                        registerCouple.setUser2(null);
                        registerCouple.setCreatedAt(Instant.now());
                        registerCouple.setUpdatedAt(Instant.now());
                        registerCouple.setStatus(CoupleStatus.PENDING);
                        registerCouple = coupleService.save(registerCouple);

                        log.info("add to pending ");
                        if(registerCouple==null){
                            log.info("Cannot add register couple ");
                        }else{
                            prepareAndSendMessage(chatId,"Waiting another people");
                            createCoupe(currentUser.get());
                        }
                    };
                    if(isUserPending){
                        prepareAndSendMessage(chatId,"Waiting another people");
                    }

                    if(isUserJoined){
                        prepareAndSendMessage(chatId,"Chatting, please enter '/finish' to finish chat");
                    }
                }

            }else{
                Optional<User> user = userService.findByChatId(chatId);
                if(user.isEmpty()){

                }else {
                    User isUserJoinedACoupe = coupleService.isUserJoinedACoupe(user.get());
                    if(isUserJoinedACoupe !=null){
                        log.info("sent a message to {}",isUserJoinedACoupe.getChatId());
                        prepareAndSendMessage(isUserJoinedACoupe.getChatId(),messageText);
                    }

                }


            }
        }
        else if (update.hasMessage() && update.getMessage().hasPhoto()) {

            Message message = update.getMessage();

            // get the last photo - it seems to be the bigger one
            List<PhotoSize> photos = message.getPhoto();
            PhotoSize photo = photos.get(photos.size() - 1);
            String id = photo.getFileId();
            try {
                GetFile getFile = new GetFile();
                getFile.setFileId(id);
                String filePath = execute(getFile).getFilePath();
                log.info("id filed {}",id);
                log.info("FilePath {}",filePath);

                File file = downloadFile(filePath);
                log.info("File out path {}",file.getPath());

                SendPhoto sendPhoto = new SendPhoto();

                //  sendPhotoMessage(chatId, id, caption);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if (update.hasMessage() && update.getMessage().hasVideo()) {
            String fileId = update.getMessage().getVideo().getFileId();
            GetFile getVide = new GetFile(fileId);
            log.info("file video id {}",getVide);
        }
    }

    private boolean createCoupe(User user){
        return coupleService.createChat(user);
    }

    private void prepareAndSendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        executeMessage(message);
    }
    private void executeMessage(SendMessage message){
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(ERROR_TEXT + e.getMessage());
        }
    }
}
