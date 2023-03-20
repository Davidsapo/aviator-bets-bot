package tg.aviator.bot.aviatorbetsbot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import tg.aviator.bot.aviatorbetsbot.controller.AviatorBot;

/**
 * Initializer class for the bot
 *
 * @author David Sapozhnik
 */
@Component
public class TgBotInitializer {

    @Autowired
    private AviatorBot aviatorBot;

    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        try {
            new TelegramBotsApi(DefaultBotSession.class).registerBot(aviatorBot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
