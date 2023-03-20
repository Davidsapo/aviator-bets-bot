package tg.aviator.bot.aviatorbetsbot.service;

import jakarta.annotation.PostConstruct;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tg.aviator.bot.aviatorbetsbot.config.TgBotConfig;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;
import static java.util.Arrays.stream;

/**
 * Coefficients History Service.
 *
 * @author David Sapozhnik
 */
@Component
public class HistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(HistoryService.class);

    private static final long PAGE_LOAD_DELAY = 3;
    private static final int MAX_RETRIES = 3;
    private static final String DEMO_BUTTON_XPATH = "//*[text()='Демо']";
    private static final String HISTORY_BUTTON_XPATH = "//div[@class='dropdown-toggle button']";
    private static final String IFRAME_XPATH = "iframe[src='https://demo.spribe.io/launch/aviator?lang=uk&currency=USD&returnurl=https%3A%2F%2Fdemo.spribe.io%2Fgame-browser%2F&onlyGame=true']";
    private static final String PAYOUTS_BLOCK_XPATH = "//app-stats-dropdown[@class='dropdown-menu show']//div[@class='payouts-block']";
    private static final String X_CHAR = "x";
    private static final String NEW_LINE_CHAR = "\n";

    private ChromeDriver driver;
    private boolean initialized;
    private int retries = 0;
    private final List<Double> coefficients = new ArrayList<>();

    @Autowired
    private TgBotConfig tgBotConfig;

    @PostConstruct
    public void init() {
        if (tgBotConfig.isDemoMode()) {
            LOG.info("Demo mode is enabled, skipping history service initialization");
            return;
        }
        try {
            LOG.info("Initializing history service");
            //System.setProperty(tgBotConfig.getChromeDriverProperty(), tgBotConfig.getChromeDriverPath());
            var options = new ChromeOptions();
            options.addArguments("--headless");
            //options.setBinary(tgBotConfig.getChromeBinaryPath());
            options.addArguments("--no-sandbox");
            //options.addArguments("--remote-allow-origins=*");
            //options.addArguments("--mute-audio");
            //options.addArguments("--disable-gpu");
            options.addArguments("--disable-dev-shm-usage");
            //options.setExperimentalOption("useAutomationExtension", false);
            //options.addArguments("start-maximized");
            //options.addArguments("disable-infobars");
            //options.addArguments("--disable-extensions");
            driver = new ChromeDriver(options);
            driver.get(tgBotConfig.getAviatorUrl());
            var wait = new WebDriverWait(driver, Duration.ofMinutes(PAGE_LOAD_DELAY));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(IFRAME_XPATH)));
            driver.switchTo().frame(driver.findElement(By.cssSelector(IFRAME_XPATH)));
            var historyButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(HISTORY_BUTTON_XPATH)));
            historyButton.click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(PAYOUTS_BLOCK_XPATH)));
            initialized = true;
            LOG.info("History service initialized");
        } catch (Exception e) {
            LOG.error("Error while initializing history service due to: {}", e.getMessage());
            System.exit(1);
        }
    }

    @Scheduled(fixedRate = 4000L)
    public void parseWebPage() {
        if (initialized) {
            try {
                var coffDiv = driver.findElement(By.xpath(PAYOUTS_BLOCK_XPATH)).getText();
                var refreshedCoefficients = stream(coffDiv.split(NEW_LINE_CHAR))
                        .map(this::convertCoefficient)
                        .toList();

                coefficients.clear();
                coefficients.addAll(refreshedCoefficients);
                LOG.info("Coefficients updated: {}", coefficients);
            } catch (StaleElementReferenceException e) {
                LOG.error("Content modified while parsing coefficients");
            } catch (Exception e) {
                LOG.error("Error while parsing coefficients due to: {}", e.getMessage());
                driver.quit();
                initialized = false;
                if (retries < MAX_RETRIES) {
                    retries++;
                    init();
                } else {
                    LOG.error("Max retries reached, exiting");
                    System.exit(1);
                }
            }
        }
    }

    public List<Double> getCoefficients() {
        return new ArrayList<>(coefficients);
    }

    public boolean isInitialized() {
        return initialized;
    }

    /* Private methods */

    private Double convertCoefficient(String coefficient) {
        return parseDouble(coefficient.replace(X_CHAR, ""));
    }
}
