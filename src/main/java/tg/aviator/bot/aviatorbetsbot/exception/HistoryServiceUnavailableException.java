package tg.aviator.bot.aviatorbetsbot.exception;

/**
 * History Service Unavailable Exception
 *
 * @author David Sapozhnik
 */
public class HistoryServiceUnavailableException extends Exception {

    public HistoryServiceUnavailableException() {
        super("History service is not initialized");
    }
}
