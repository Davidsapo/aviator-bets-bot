package tg.aviator.bot.aviatorbetsbot.util;

/**
 * Texting Util.
 *
 * @author David Sapozhnik
 */
public interface TextingUtil {

    // Error messages
    String NO_MESSAGE_ERROR = "Update has no message";

    // Commands
    String START_COMMAND = "/start";
    String BET_COMMAND_MEDIUM = "/bet_medium";
    String BET_COMMAND_HIGH = "/bet_high";
    String BET_COMMAND_BIG_WIN = "/bet_big_win";
    String DOUBLE_BET_COMMAND = "/double_bet";
    String BET_COMMAND_LOW = "/bet_low";
    String USERS_COMMAND = "/users";
    String USER_REQUESTS = "/user_requests";
    String USER_PROVIDE_ACCESS_COMMAND = "/user_provide_access";
    String USER_DENY_ACCESS_COMMAND = "/user_deny_access";
    String INIT_ADMIN_BUTTONS_COMMAND = "/init_admin_buttons";
    String STRATEGY_SELECTION_COMMAND = "/strategy_selection";
    String STRATEGY_INIT_COMMAND = "/strategy_init";
    String REQUEST_ACCESS_COMMAND = "/request_access";
    String PROCESS_PAYMENT_COMMAND = "/process_payment";
    String CHECK_ACCESS_COMMAND = "/check_access";

    // Messages
    String COMMAND_UNRECOGNIZED_MESSAGE = "Command not recognized";
    String WELCOME_MESSAGE = "Welcome to Aviator Bets Bot.Some description.Have fun!";
    String PAYMENT_MESSAGE = "Please, pay for access to the bot";
    String STRATEGY_SELECTION_MESSAGE = "Please, select strategy";
    String ACCESS_DENIED_MESSAGE = "Your access is denied, please request access";
    String ACCESS_PROVIDED_MESSAGE = "Your access is provided. Have fun!";
    String ACCESS_REQUIRED_MESSAGE = "Access is required to use this bot. Please, request access";
    String ACCESS_ALREADY_PROVIDED_MESSAGE = "Your access is already provided";
    String FORBIDDEN_MESSAGE = "You are not allowed to use this command";
    String INTERNAL_ERROR_MESSAGE = "Something went wrong. Our team is already working on it.";
    String STRATEGY_SELECTED_MESSAGE = "Strategy %s selected. Have fun!";
    String PAYMENT_PROCESSING_MESSAGE = "Payment processing. You will be notified when your request will be processed";
    String NEW_REQUEST_MESSAGE = "New request from %s";
}
