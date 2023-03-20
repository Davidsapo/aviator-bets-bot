package tg.aviator.bot.aviatorbetsbot.service.bet;

import org.springframework.stereotype.Component;
import tg.aviator.bot.aviatorbetsbot.enums.BetLevel;
import tg.aviator.bot.aviatorbetsbot.exception.HistoryServiceUnavailableException;
import tg.aviator.bot.aviatorbetsbot.model.LowRiskBetBO;
import tg.aviator.bot.aviatorbetsbot.model.MediumRiskBetBO;

import java.util.List;

/**
 * Medium Risk Bets Service.
 *
 * @author David Sapozhnik
 */
@Component
public class LowRiskBetsService extends BasicBetsService {

    private static final double GUARANTEED_COEFFICIENT = 1.15;
    private static final double INDICATOR_COEFFICIENT = 1.15;
    private static final int SAFE_ELEMENT = 5;

    public LowRiskBetBO calculateBet() throws HistoryServiceUnavailableException {
        if (historyService.isInitialized()) {
            var coefficients = historyService.getCoefficients();

            var bet = new LowRiskBetBO();
            bet.getHistory().addAll(coefficients.subList(0, 5));
            if (isNextBetGuaranteed(coefficients)) {
                bet.setGuaranteedCoefficient(GUARANTEED_COEFFICIENT);
            } else {
                bet.setIgnoreBet(true);
            }
            return bet;
        }
        throw new HistoryServiceUnavailableException();
    }

    @Override
    public BetLevel getBetLevel() {
        return BetLevel.LOW;
    }

    /* Private methods */

    private boolean isNextBetGuaranteed(List<Double> coefficients) {
        var elementFound = false;
        var index = 0;
        for (int i = 0; i < coefficients.size(); i++) {
            if (coefficients.get(i) < INDICATOR_COEFFICIENT) {
                index = i;
                elementFound = true;
                break;
            }
        }
        return elementFound && (index) < SAFE_ELEMENT;
    }
}

