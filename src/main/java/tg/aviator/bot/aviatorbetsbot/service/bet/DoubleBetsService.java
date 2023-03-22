package tg.aviator.bot.aviatorbetsbot.service.bet;

import org.springframework.stereotype.Component;
import tg.aviator.bot.aviatorbetsbot.enums.BetLevel;
import tg.aviator.bot.aviatorbetsbot.exception.HistoryServiceUnavailableException;
import tg.aviator.bot.aviatorbetsbot.model.DoubleBetBO;

import java.util.List;

/**
 * Medium Risk Bets Service.
 *
 * @author David Sapozhnik
 */
@Component
public class DoubleBetsService extends BasicBetsService {

    private static final double FIRST_GUARANTEED_COEFFICIENT = 1.5;
    private static final double SECOND_GUARANTEED_COEFFICIENT = 1.7;
    private static final double INDICATOR_COEFFICIENT = 2.0;
    private static final int ELEMENTS_TO_CHECK = 3;

    public DoubleBetBO calculateBet() throws HistoryServiceUnavailableException {
        if (historyService.isInitialized()) {
            var coefficients = historyService.getCoefficients();

            var bet = new DoubleBetBO();
            bet.getHistory().addAll(coefficients.subList(0, 5));
            if (isNextBetGuaranteed(coefficients)) {
                bet.setFirstGuaranteedCoefficient(FIRST_GUARANTEED_COEFFICIENT);
                bet.setSecondGuaranteedCoefficient(SECOND_GUARANTEED_COEFFICIENT);
            } else {
                bet.setIgnoreBet(true);
            }
            return bet;
        }
        throw new HistoryServiceUnavailableException();
    }

    @Override
    public BetLevel getBetLevel() {
        return BetLevel.DOUBLE;
    }

    /* Private methods */

    private boolean isNextBetGuaranteed(List<Double> coefficients) {
        return coefficients.subList(0, ELEMENTS_TO_CHECK).stream().allMatch(c -> c < INDICATOR_COEFFICIENT);
    }
}

