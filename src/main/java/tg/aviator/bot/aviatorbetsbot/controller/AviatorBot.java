package tg.aviator.bot.aviatorbetsbot.controller;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.aviator.bot.aviatorbetsbot.config.TgBotConfig;
import tg.aviator.bot.aviatorbetsbot.enums.BetLevel;
import tg.aviator.bot.aviatorbetsbot.model.BigWinBetBO;
import tg.aviator.bot.aviatorbetsbot.model.DoubleBetBO;
import tg.aviator.bot.aviatorbetsbot.model.HighRiskBetBO;
import tg.aviator.bot.aviatorbetsbot.model.LowRiskBetBO;
import tg.aviator.bot.aviatorbetsbot.model.MediumRiskBetBO;
import tg.aviator.bot.aviatorbetsbot.service.UserService;
import tg.aviator.bot.aviatorbetsbot.service.bet.BasicBetsService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static tg.aviator.bot.aviatorbetsbot.enums.BetLevel.BIG;
import static tg.aviator.bot.aviatorbetsbot.enums.BetLevel.DOUBLE;
import static tg.aviator.bot.aviatorbetsbot.enums.BetLevel.HIGH;
import static tg.aviator.bot.aviatorbetsbot.enums.BetLevel.LOW;
import static tg.aviator.bot.aviatorbetsbot.enums.BetLevel.MEDIUM;
import static tg.aviator.bot.aviatorbetsbot.util.BetMessageGenerator.generateBetMessage;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.ACCESS_ALREADY_PROVIDED_MESSAGE;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.ACCESS_DENIED_MESSAGE;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.ACCESS_PROVIDED_MESSAGE;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.ACCESS_REQUIRED_MESSAGE;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.BET_COMMAND_BIG_WIN;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.BET_COMMAND_HIGH;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.BET_COMMAND_LOW;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.BET_COMMAND_MEDIUM;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.CHECK_ACCESS_COMMAND;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.COMMAND_UNRECOGNIZED_MESSAGE;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.DOUBLE_BET_COMMAND;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.FORBIDDEN_MESSAGE;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.INIT_ADMIN_BUTTONS_COMMAND;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.INTERNAL_ERROR_MESSAGE;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.NEW_REQUEST_MESSAGE;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.NO_MESSAGE_ERROR;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.PAYMENT_MESSAGE;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.PAYMENT_PROCESSING_MESSAGE;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.PROCESS_PAYMENT_COMMAND;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.REQUEST_ACCESS_COMMAND;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.START_COMMAND;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.STRATEGY_INIT_COMMAND;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.STRATEGY_SELECTED_MESSAGE;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.STRATEGY_SELECTION_COMMAND;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.STRATEGY_SELECTION_MESSAGE;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.USERS_COMMAND;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.USER_DENY_ACCESS_COMMAND;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.USER_PROVIDE_ACCESS_COMMAND;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.USER_REQUESTS;
import static tg.aviator.bot.aviatorbetsbot.util.TextingUtil.WELCOME_MESSAGE;

/**
 * @author David Sapozhnik
 */
@Component
public class AviatorBot extends TelegramLongPollingBot {

    private static final Logger LOG = LoggerFactory.getLogger(AviatorBot.class);

    @Autowired
    private TgBotConfig tgBotConfig;

    @Autowired
    private UserService userService;

    @Autowired
    private List<BasicBetsService> betsServices;

    private final Map<BetLevel, BasicBetsService> betsServicesMap = new HashMap<>();

    @PostConstruct
    public void init() {
        for (var betsService : betsServices) {
            betsServicesMap.put(betsService.getBetLevel(), betsService);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        processUpdate(update);
    }

    @Override
    public String getBotUsername() {
        return tgBotConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return tgBotConfig.getBotToken();
    }

    /* Private methods */

    private void processUpdate(Update update) {
        try {
            var chatId = resolveChatId(update);
            if (!update.hasCallbackQuery()) {
                if (!update.hasMessage()) {
                    execute(createMsg(chatId, NO_MESSAGE_ERROR));
                }
                if (!update.getMessage().hasText()) {
                    execute(createMsg(chatId, COMMAND_UNRECOGNIZED_MESSAGE));
                }
            }
            var from = resolveUser(update);
            var text = resolveText(update);
            if (text.equals(START_COMMAND)) {
                if (!userService.isAccessProvided(from.getId())) {
                    userService.createUser(from.getId(), resolveChatIdLong(update), from.getFirstName(), from.getLastName());
                    var msg = createMsg(chatId, WELCOME_MESSAGE);
                    addRequestAccessButton(msg);
                    execute(msg);
                } else {
                    var msg = createMsg(chatId, WELCOME_MESSAGE);
                    addStrategySelectionButton(msg);
                    execute(msg);
                }
            } else if (text.equals(STRATEGY_SELECTION_COMMAND)) {
                if (userService.isAccessProvided(from.getId())) {
                    sendStrategySelectionMessage(chatId);
                } else {
                    sendAccessNotProvidedMessage(chatId);
                }
            } else if (text.startsWith(STRATEGY_INIT_COMMAND)) {
                if (userService.isAccessProvided(from.getId())) {
                    var strategy = BetLevel.valueOf(resolveStrategy(text));
                    var msg = createMsg(chatId, format(STRATEGY_SELECTED_MESSAGE, strategy));
                    addBetButtons(msg, strategy);
                    execute(msg);
                } else {
                    sendAccessNotProvidedMessage(chatId);
                }
            } else if (text.equals(REQUEST_ACCESS_COMMAND)) {
                if (userService.isAccessProvided(from.getId())) {
                    sendAccessAlreadyProvidedMessage(chatId);
                    sendStrategySelectionMessage(chatId);
                } else {
                    var msg = createMsg(chatId, PAYMENT_MESSAGE);
                    addProcessPaymentButton(msg);
                    execute(msg);
                }
            } else if (text.equals(PROCESS_PAYMENT_COMMAND)) {
                if (userService.isAccessProvided(from.getId())) {
                    sendAccessAlreadyProvidedMessage(chatId);
                    sendStrategySelectionMessage(chatId);
                } else {
                    userService.requestAccess(from.getId());
                    sendPaymentProcessingMessage(chatId);
                    for (var adminChatId : userService.getAdminsAndOwnersChatIds()) {
                        var adminMsg = createMsg(adminChatId.toString(), format(NEW_REQUEST_MESSAGE, from.getFirstName()));
                        execute(adminMsg);
                    }
                }
            } else if (text.equals(CHECK_ACCESS_COMMAND)) {
                if (userService.isAccessProvided(from.getId())) {
                    sendAccessAlreadyProvidedMessage(chatId);
                    sendStrategySelectionMessage(chatId);
                } else if (userService.isAccessDenied(from.getId())) {
                    sendAccessDeniedMessage(chatId);
                } else {
                    sendPaymentProcessingMessage(chatId);
                }
            }

            //Bet commands
            else if (text.equals(BET_COMMAND_MEDIUM)) {
                if (userService.isAccessProvided(from.getId())) {
                    var msg = createMsg(chatId, generateBetMessage((MediumRiskBetBO) (betsServicesMap.get(MEDIUM).calculateBet())));
                    addBetButtons(msg, MEDIUM);
                    execute(msg);
                } else {
                    sendAccessNotProvidedMessage(chatId);
                }
            } else if (text.equals(BET_COMMAND_HIGH)) {
                if (userService.isAccessProvided(from.getId())) {
                    var msg = createMsg(chatId, generateBetMessage((HighRiskBetBO) (betsServicesMap.get(HIGH).calculateBet())));
                    addBetButtons(msg, HIGH);
                    execute(msg);
                } else {
                    sendAccessNotProvidedMessage(chatId);
                }
            } else if (text.equals(BET_COMMAND_BIG_WIN)) {
                if (userService.isAccessProvided(from.getId())) {
                    var msg = createMsg(chatId, generateBetMessage((BigWinBetBO) (betsServicesMap.get(BIG).calculateBet())));
                    addBetButtons(msg, BIG);
                    execute(msg);
                } else {
                    sendAccessNotProvidedMessage(chatId);
                }
            } else if (text.equals(DOUBLE_BET_COMMAND)) {
                if (userService.isAccessProvided(from.getId())) {
                    var msg = createMsg(chatId, generateBetMessage((DoubleBetBO) (betsServicesMap.get(DOUBLE).calculateBet())));
                    addBetButtons(msg, DOUBLE);
                    execute(msg);
                } else {
                    sendAccessNotProvidedMessage(chatId);
                }
            } else if (text.equals(BET_COMMAND_LOW)) {
                if (userService.isAccessProvided(from.getId())) {
                    var msg = createMsg(chatId, generateBetMessage((LowRiskBetBO) (betsServicesMap.get(LOW).calculateBet())));
                    addBetButtons(msg, LOW);
                    execute(msg);
                } else {
                    sendAccessNotProvidedMessage(chatId);
                }
            }
            //Admin access
            else if (text.startsWith(USERS_COMMAND)) {
                if (userService.isOwnerOrAdmin(from.getId())) {
                    var page = resolvePage(text);
                    var users = userService.getUsers(page);
                    if (users.isEmpty()) {
                        var msg = createMsg(chatId, "No users");
                        execute(msg);
                    } else {
                        for (var user : users) {
                            var msg = createMsg(chatId, user.toString());
                            var buttons = new ArrayList<List<InlineKeyboardButton>>();
                            if (user.isAccessRequested() || user.isAccessProvided()) {
                                buttons.add(getRevokeAccessButton(user.getId()));
                                if (user.isAccessRequested()) {
                                    buttons.add(getProvideAccessButton(user.getId()));
                                }
                            }
                            if (user.equals(users.get(users.size() - 1))) {
                                var row = new ArrayList<InlineKeyboardButton>();
                                if (page > 0) {
                                    row.add(getPreviousPageButton(page));
                                }
                                row.add(getNextPageButton(page));
                                buttons.add(row);
                            }
                            if (!buttons.isEmpty()) {
                                var keyboardMarkup = new InlineKeyboardMarkup();
                                keyboardMarkup.setKeyboard(buttons);
                                msg.setReplyMarkup(keyboardMarkup);
                            }
                            execute(msg);
                        }
                    }
                } else {
                    sendForbiddenMessage(chatId);
                }
            } else if (text.startsWith(USER_REQUESTS)) {
                if (userService.isOwnerOrAdmin(from.getId())) {
                    var page = resolvePage(text);
                    var users = userService.getUsersWithAccessRequest(page);
                    if (users.isEmpty()) {
                        var msg = createMsg(chatId, "No users");
                        execute(msg);
                    } else {
                        for (var user : users) {
                            var msg = createMsg(chatId, user.toString());
                            var buttons = new ArrayList<List<InlineKeyboardButton>>();
                            if (user.isAccessRequested() || user.isAccessProvided()) {
                                buttons.add(getRevokeAccessButton(user.getId()));
                                if (user.isAccessRequested()) {
                                    buttons.add(getProvideAccessButton(user.getId()));
                                }
                            }
                            if (user.equals(users.get(users.size() - 1))) {
                                var row = new ArrayList<InlineKeyboardButton>();
                                if (page > 0) {
                                    row.add(getPreviousPageButton(page));
                                }
                                row.add(getNextPageButton(page));
                                buttons.add(row);
                            }
                            if (!buttons.isEmpty()) {
                                var keyboardMarkup = new InlineKeyboardMarkup();
                                keyboardMarkup.setKeyboard(buttons);
                                msg.setReplyMarkup(keyboardMarkup);
                            }
                            execute(msg);
                        }
                    }
                } else {
                    sendForbiddenMessage(chatId);
                }
            } else if (text.startsWith(USER_PROVIDE_ACCESS_COMMAND)) {
                if (userService.isOwnerOrAdmin(from.getId())) {
                    var userId = resolveUserId(text);
                    userService.provideAccess(userId);
                    var msg = createMsg(chatId, "Access provided");
                    execute(msg);
                    var userChatId = userService.getChatIdByUserId(userId).toString();
                    var msg2 = createMsg(userChatId, ACCESS_PROVIDED_MESSAGE);
                    execute(msg2);
                    sendStrategySelectionMessage(userChatId);
                } else {
                    sendForbiddenMessage(chatId);
                }
            } else if (text.startsWith(USER_DENY_ACCESS_COMMAND)) {
                if (userService.isOwnerOrAdmin(from.getId())) {
                    var userId = resolveUserId(text);
                    userService.denyAccess(userId);
                    var msg = createMsg(chatId, "Access denied");
                    execute(msg);
                    sendAccessDeniedMessage(userService.getChatIdByUserId(userId).toString());
                } else {
                    sendForbiddenMessage(chatId);
                }
            } else if (text.equals(INIT_ADMIN_BUTTONS_COMMAND)) {
                if (userService.isOwnerOrAdmin(from.getId())) {
                    var msg = createMsg(chatId, "Admin panel");
                    addAdminButtons(msg);
                    execute(msg);
                } else {
                    sendForbiddenMessage(chatId);
                }
            }
            // Unrecognized command
            else {
                execute(createMsg(chatId, COMMAND_UNRECOGNIZED_MESSAGE));
            }
        } catch (Exception e) {
            LOG.error("Error occurred while processing command. Warn message sent to user. Reason: ", e);
            try {
                var msg = createMsg(resolveChatId(update), INTERNAL_ERROR_MESSAGE);
                addRetryButton(msg);
                execute(msg);
            } catch (TelegramApiException ex) {
                LOG.error("Error occurred while sending error message to user. Reason: ", ex);
            }
        }
    }

    /* Private methods */

    private SendMessage createMsg(String chatId, String s) {
        var sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        return sendMessage;
    }

    private void sendAccessNotProvidedMessage(String chatId) throws TelegramApiException {
        var msg = createMsg(chatId, ACCESS_REQUIRED_MESSAGE);
        addRequestAccessButton(msg);
        execute(msg);
    }

    private void sendAccessDeniedMessage(String chatId) throws TelegramApiException {
        var msg = createMsg(chatId, ACCESS_DENIED_MESSAGE);
        addRequestAccessButton(msg);
        execute(msg);
    }

    private void sendForbiddenMessage(String chatId) throws TelegramApiException {
        var msg = createMsg(chatId, FORBIDDEN_MESSAGE);
        execute(msg);
    }

    private void sendStrategySelectionMessage(String chatId) throws TelegramApiException {
        var msg = createMsg(chatId, STRATEGY_SELECTION_MESSAGE);
        addStrategyButtonsInline(msg);
        execute(msg);
    }

    private void sendAccessAlreadyProvidedMessage(String chatId) throws TelegramApiException {
        var msg = createMsg(chatId, ACCESS_ALREADY_PROVIDED_MESSAGE);
        execute(msg);
    }

    private void sendPaymentProcessingMessage(String chatId) throws TelegramApiException {
        var msg = createMsg(chatId, PAYMENT_PROCESSING_MESSAGE);
        addCheckAccessButton(msg);
        execute(msg);
    }

    private void addAdminButtons(SendMessage sendMessage) {
        var keyboardMarkup = new ReplyKeyboardMarkup();
        var keyboard = new ArrayList<KeyboardRow>();
        var row1 = new KeyboardRow();
        row1.add(USER_REQUESTS + "_0");
        row1.add(USERS_COMMAND + "_0");
        keyboard.add(row1);
        keyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(keyboardMarkup);
    }

    private void addStrategyButtonsInline(SendMessage sendMessage) {
        var keyboardMarkup = new InlineKeyboardMarkup();
        var keyboard = new ArrayList<List<InlineKeyboardButton>>();
        var row1 = new ArrayList<InlineKeyboardButton>();
        var row2 = new ArrayList<InlineKeyboardButton>();
        var row3 = new ArrayList<InlineKeyboardButton>();
        var row4 = new ArrayList<InlineKeyboardButton>();
        var row5 = new ArrayList<InlineKeyboardButton>();

        var button1 = new InlineKeyboardButton();
        button1.setText("Medium");
        button1.setCallbackData(STRATEGY_INIT_COMMAND + "_" + MEDIUM);

        var button2 = new InlineKeyboardButton();
        button2.setText("High");
        button2.setCallbackData(STRATEGY_INIT_COMMAND + "_" + HIGH);

        var button3 = new InlineKeyboardButton();
        button3.setText("Big win");
        button3.setCallbackData(STRATEGY_INIT_COMMAND + "_" + BIG);

        var button4 = new InlineKeyboardButton();
        button4.setText("Double");
        button4.setCallbackData(STRATEGY_INIT_COMMAND + "_" + DOUBLE);

        var button5 = new InlineKeyboardButton();
        button5.setText("Low");
        button5.setCallbackData(STRATEGY_INIT_COMMAND + "_" + LOW);

        row1.add(button1);
        row2.add(button2);
        row3.add(button3);
        row4.add(button4);
        row5.add(button5);
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);
        keyboard.add(row5);
        keyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(keyboardMarkup);
    }

    private void addBetButtons(SendMessage sendMessage, BetLevel betLevel) {
        String betCommand = null;
        switch (betLevel) {
            case MEDIUM:
                betCommand = BET_COMMAND_MEDIUM;
                break;
            case HIGH:
                betCommand = BET_COMMAND_HIGH;
                break;
            case BIG:
                betCommand = BET_COMMAND_BIG_WIN;
                break;
            case DOUBLE:
                betCommand = DOUBLE_BET_COMMAND;
                break;
            case LOW:
                betCommand = BET_COMMAND_LOW;
                break;
        }
        var keyboardMarkup = new InlineKeyboardMarkup();
        var keyboard = new ArrayList<List<InlineKeyboardButton>>();
        var row1 = new ArrayList<InlineKeyboardButton>();
        var row2 = new ArrayList<InlineKeyboardButton>();

        var button1 = new InlineKeyboardButton();
        button1.setText("Bet");
        button1.setCallbackData(betCommand);

        var button2 = new InlineKeyboardButton();
        button2.setText("Select strategy");
        button2.setCallbackData(STRATEGY_SELECTION_COMMAND);

        row1.add(button1);
        row2.add(button2);
        keyboard.add(row1);
        keyboard.add(row2);
        keyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(keyboardMarkup);
    }

    private void addRetryButton(SendMessage sendMessage) {
        var keyboardMarkup = new InlineKeyboardMarkup();
        var keyboard = new ArrayList<List<InlineKeyboardButton>>();
        var row1 = new ArrayList<InlineKeyboardButton>();

        var button1 = new InlineKeyboardButton();
        button1.setText("Retry");
        button1.setCallbackData(START_COMMAND);

        row1.add(button1);
        keyboard.add(row1);
        keyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(keyboardMarkup);
    }

    private void addRequestAccessButton(SendMessage sendMessage) {
        var keyboardMarkup = new InlineKeyboardMarkup();
        var keyboard = new ArrayList<List<InlineKeyboardButton>>();
        var row1 = new ArrayList<InlineKeyboardButton>();

        var button1 = new InlineKeyboardButton();
        button1.setText("Request access");
        button1.setCallbackData(REQUEST_ACCESS_COMMAND);

        row1.add(button1);
        keyboard.add(row1);
        keyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(keyboardMarkup);
    }

    private void addProcessPaymentButton(SendMessage sendMessage) {
        var keyboardMarkup = new InlineKeyboardMarkup();
        var keyboard = new ArrayList<List<InlineKeyboardButton>>();
        var row1 = new ArrayList<InlineKeyboardButton>();

        var button1 = new InlineKeyboardButton();
        button1.setText("Process payment");
        button1.setCallbackData(PROCESS_PAYMENT_COMMAND);

        row1.add(button1);
        keyboard.add(row1);
        keyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(keyboardMarkup);
    }

    public void addCheckAccessButton(SendMessage sendMessage) {
        var keyboardMarkup = new InlineKeyboardMarkup();
        var keyboard = new ArrayList<List<InlineKeyboardButton>>();
        var row1 = new ArrayList<InlineKeyboardButton>();

        var button1 = new InlineKeyboardButton();
        button1.setText("Check access");
        button1.setCallbackData(CHECK_ACCESS_COMMAND);

        row1.add(button1);
        keyboard.add(row1);
        keyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(keyboardMarkup);
    }

    private void addStrategySelectionButton(SendMessage sendMessage) {
        var keyboardMarkup = new InlineKeyboardMarkup();
        var keyboard = new ArrayList<List<InlineKeyboardButton>>();
        var row1 = new ArrayList<InlineKeyboardButton>();

        var button1 = new InlineKeyboardButton();
        button1.setText("Select strategy");
        button1.setCallbackData(STRATEGY_SELECTION_COMMAND);

        row1.add(button1);
        keyboard.add(row1);
        keyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(keyboardMarkup);
    }

    private ArrayList<InlineKeyboardButton> getRevokeAccessButton(Long userId) {
        var revokeAccessButton = new InlineKeyboardButton();
        revokeAccessButton.setText("Revoke access");
        revokeAccessButton.setCallbackData(USER_DENY_ACCESS_COMMAND + "_" + userId);
        var row1 = new ArrayList<InlineKeyboardButton>();
        row1.add(revokeAccessButton);
        return row1;
    }

    private ArrayList<InlineKeyboardButton> getProvideAccessButton(Long userId) {
        var provideAccessButton = new InlineKeyboardButton();
        provideAccessButton.setText("Provide access");
        provideAccessButton.setCallbackData(USER_PROVIDE_ACCESS_COMMAND + "_" + userId);
        var row = new ArrayList<InlineKeyboardButton>();
        row.add(provideAccessButton);
        return row;
    }

    private InlineKeyboardButton getPreviousPageButton(int page) {
        var prevPageButton = new InlineKeyboardButton();
        prevPageButton.setText("Previous page");
        prevPageButton.setCallbackData(USER_REQUESTS + "_" + (page - 1));
        return prevPageButton;
    }

    private InlineKeyboardButton getNextPageButton(int page) {
        var nextPageButton = new InlineKeyboardButton();
        nextPageButton.setText("Next page");
        nextPageButton.setCallbackData(USER_REQUESTS + "_" + (page + 1));
        return nextPageButton;
    }

    private void removeCustomKeyboard(SendMessage sendMessage) {
        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardRemove.setRemoveKeyboard(true);
        sendMessage.setReplyMarkup(replyKeyboardRemove);
    }

    private String resolveChatId(Update update) {
        return update.hasCallbackQuery() ?
                update.getCallbackQuery().getMessage().getChatId().toString() :
                update.getMessage().getChatId().toString();
    }

    private long resolveChatIdLong(Update update) {
        return update.hasCallbackQuery() ?
                update.getCallbackQuery().getMessage().getChatId() :
                update.getMessage().getChatId();
    }

    private User resolveUser(Update update) {
        return update.hasCallbackQuery() ?
                update.getCallbackQuery().getFrom() :
                update.getMessage().getFrom();
    }

    private String resolveText(Update update) {
        return update.hasCallbackQuery() ?
                update.getCallbackQuery().getData() :
                update.getMessage().getText();
    }

    private String resolveStrategy(String text) {
        return text.substring(text.lastIndexOf('_') + 1);
    }

    private int resolvePage(String text) {
        return Integer.parseInt(text.substring(text.lastIndexOf('_') + 1));
    }

    private long resolveUserId(String text) {
        return Long.parseLong(text.substring(text.lastIndexOf('_') + 1));
    }
}
