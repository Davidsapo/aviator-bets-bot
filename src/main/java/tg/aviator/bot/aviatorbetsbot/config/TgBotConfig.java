package tg.aviator.bot.aviatorbetsbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Configuration class for the bot
 *
 * @author David Sapozhnik
 */
@Configuration
@EnableScheduling
@PropertySource("application.properties")
public class TgBotConfig {

    @Value("${telegram.bot.name}")
    private String botName;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${aviator.url}")
    private String aviatorUrl;

    @Value("${chrome.driver.path}")
    private String chromeDriverPath;

    @Value("${chrome.binary.path}")
    private String chromeBinaryPath;

    @Value("${chrome.driver.property}")
    private String chromeDriverProperty;

    @Value("${demo-mode}")
    private boolean demoMode;

    public String getBotName() {
        return botName;
    }

    public String getBotToken() {
        return botToken;
    }

    public String getAviatorUrl() {
        return aviatorUrl;
    }

    public String getChromeDriverPath() {
        return chromeDriverPath;
    }

    public String getChromeBinaryPath() {
        return chromeBinaryPath;
    }

    public String getChromeDriverProperty() {
        return chromeDriverProperty;
    }

    public boolean isDemoMode() {
        return demoMode;
    }
}
