package tg.aviator.bot.aviatorbetsbot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author David Sapozhnik
 */
@RestController
public class TelegramWebhookController {

    private static final Logger LOG = LoggerFactory.getLogger(TelegramWebhookController.class);

    @PostMapping("/webhook")
    public void handleWebhookUpdate(@RequestBody Update update) {
        LOG.info("Received update: {}", update);
    }
}
