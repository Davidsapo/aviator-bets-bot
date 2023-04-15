package tg.aviator.bot.aviatorbetsbot.service.bet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tg.aviator.bot.aviatorbetsbot.entity.BigWin;
import tg.aviator.bot.aviatorbetsbot.enums.BetLevel;
import tg.aviator.bot.aviatorbetsbot.exception.HistoryServiceUnavailableException;
import tg.aviator.bot.aviatorbetsbot.model.BigWinBO;
import tg.aviator.bot.aviatorbetsbot.model.BigWinBetBO;
import tg.aviator.bot.aviatorbetsbot.repository.BigWinRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MINUTES;

/**
 * Big Win Bets Service.
 *
 * @author David Sapozhnik
 */
@Component
public class BigWinBetsService extends BasicBetsService {

    private static final int PAGE_SIZE = 20;
    private static final String SORT_FIELD = "catchTime";

    private static final Logger LOG = LoggerFactory.getLogger(BigWinBetsService.class);
    private static final double GUARANTEED_COEFFICIENT = 100.0;
    private static final double INDICATOR_COEFFICIENT = 100.0;
    private static final int BIG_WIN_TIME_RANGE = 60;
    private static final int BIG_WIN_CHANCE_TIME_RANGE = 30;

    private boolean lastBigWinFound;
    private Double lastBigWinCoefficient;
    private Double lastCoefficientFound;
    private LocalDateTime lastBigWinFoundTime = now();
    private int tries = 0;

    @Autowired
    private BigWinRepository bigWinRepository;


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

    public List<BigWinBO> getBigWins(int page) {
        var pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(SORT_FIELD).descending());
        return bigWinRepository.findAll(pageable).stream()
                .map(entity -> {
                    var bo = new BigWinBO();
                    bo.setCoefficient(entity.getCoefficient());
                    bo.setCatchTime(entity.getCatchTime().format(DateTimeFormatter.ofPattern("dd HH:mm")));
                    bo.setTries(entity.getTries());
                    bo.setTimeSpent(String.format("%dh %dm", entity.getTimeSpent() / 60, entity.getTimeSpent() % 60));
                    return bo;
                })
                .toList();
    }

    @Override
    public BetLevel getBetLevel() {
        return BetLevel.BIG;
    }

    @Scheduled(fixedRate = 10000L)
    protected void update() {
        if (historyService.isInitialized()) {
            var coefficients = historyService.getCoefficients();
            coefficients.stream()
                    .filter(foundedCoefficient -> foundedCoefficient >= INDICATOR_COEFFICIENT)
                    .findFirst()
                    .ifPresent(foundedCoefficient -> {
                        if (lastBigWinFound) {
                            if (!foundedCoefficient.equals(lastBigWinCoefficient)) {
                                var bigWin = new BigWin();
                                bigWin.setCoefficient(foundedCoefficient);
                                bigWin.setCatchTime(now());
                                bigWin.setTries(tries);
                                bigWin.setTimeSpent(lastBigWinFoundTime.until(now(), MINUTES));
                                bigWinRepository.save(bigWin);
                                tries = 0;
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
            if (lastBigWinFound && !Objects.equals(coefficients.get(0), lastBigWinCoefficient)) {
                int lastCoefficientFoundIndex = coefficients.indexOf(lastCoefficientFound);
                if (lastCoefficientFoundIndex != -1) {
                    tries = tries + lastCoefficientFoundIndex;
                }
                lastCoefficientFound = coefficients.get(0);
            }
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

