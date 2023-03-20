package tg.aviator.bot.aviatorbetsbot.service.bet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tg.aviator.bot.aviatorbetsbot.enums.BetLevel;
import tg.aviator.bot.aviatorbetsbot.exception.HistoryServiceUnavailableException;
import tg.aviator.bot.aviatorbetsbot.model.BigWinBetBO;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MINUTES;

/**
 * Big Win Bets Service.
 *
 * @author David Sapozhnik
 */
@Component
public class BigWinBetsService extends BasicBetsService {

    private static final Logger LOG = LoggerFactory.getLogger(BigWinBetsService.class);
    private static final double GUARANTEED_COEFFICIENT = 100.0;
    private static final double INDICATOR_COEFFICIENT = 100.0;
    private static final int BIG_WIN_TIME_RANGE = 60;
    private static final int BIG_WIN_CHANCE_TIME_RANGE = 30;

    private boolean lastBigWinFound;
    private Double lastBigWinCoefficient;
    private LocalDateTime lastBigWinFoundTime = now();


    public BigWinBetBO calculateBet() throws HistoryServiceUnavailableException {
        if (historyService.isInitialized()) {
            var coefficients = historyService.getCoefficients();

            var bet = new BigWinBetBO();
            bet.getHistory().addAll(coefficients.subList(0, 5));
            if (MINUTES.between(lastBigWinFoundTime, now()) < BIG_WIN_TIME_RANGE) {
                bet.setIgnoreBet(true);
                bet.setMinutesToWait(getMinutesToWait());
            } else {
                bet.setWinTimeRange(getMinutesToWaitForBigWinChance());
                bet.setGuaranteedCoefficient(GUARANTEED_COEFFICIENT);
            }

            return bet;
        }
        throw new HistoryServiceUnavailableException();
    }

    @Override
    public BetLevel getBetLevel() {
        return BetLevel.BIG;
    }

    @Scheduled(fixedRate = 10000L)
    protected void update() {
        if (historyService.isInitialized()) {
            historyService.getCoefficients().stream()
                    .filter(foundedCoefficient -> foundedCoefficient >= INDICATOR_COEFFICIENT)
                    .findFirst()
                    .ifPresent(foundedCoefficient -> {
                        if (lastBigWinFound) {
                            if (!foundedCoefficient.equals(lastBigWinCoefficient)) {
                                lastBigWinCoefficient = foundedCoefficient;
                                lastBigWinFoundTime = now();
                                LOG.info("Big win found: {}", foundedCoefficient);
                            }
                        } else {
                            lastBigWinCoefficient = foundedCoefficient;
                            lastBigWinFoundTime = now();
                            lastBigWinFound = true;
                            LOG.info("Big win found: {}", foundedCoefficient);
                        }
                    });
        }
    }

    /* Private methods */

    private long getMinutesToWait() {
        var minutes = now().until(lastBigWinFoundTime.plusMinutes(BIG_WIN_TIME_RANGE), MINUTES);
        return minutes > 0 ? minutes : 0;
    }

    private long getMinutesToWaitForBigWinChance() {
        var minutes = now().until(lastBigWinFoundTime.plusMinutes(BIG_WIN_TIME_RANGE + BIG_WIN_CHANCE_TIME_RANGE), MINUTES);
        return minutes > 0 ? minutes : 0;
    }
}

