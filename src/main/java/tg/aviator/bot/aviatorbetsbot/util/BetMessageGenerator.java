package tg.aviator.bot.aviatorbetsbot.util;

import tg.aviator.bot.aviatorbetsbot.model.BigWinBetBO;
import tg.aviator.bot.aviatorbetsbot.model.DoubleBetBO;
import tg.aviator.bot.aviatorbetsbot.model.HighRiskBetBO;
import tg.aviator.bot.aviatorbetsbot.model.LowRiskBetBO;
import tg.aviator.bot.aviatorbetsbot.model.MediumRiskBetBO;

/**
 * Bet Message Generator.
 *
 * @author David Sapozhnik
 */
public final class BetMessageGenerator {

    private BetMessageGenerator() {
    }

    public static String generateBetMessage(MediumRiskBetBO bet) {
        var stringBuilder = new StringBuilder();
        stringBuilder.append("Істрія коефіцієнтів: ").append("\n").append(bet.getHistory()).append("\n").append("\n");
        if (bet.isIgnoreBet()) {
            stringBuilder.append("Цей політ краще пропустити.");
        } else {
            stringBuilder.append("Заберіть коефіцієнт: ").append(bet.getGuaranteedCoefficient()).append("x або більше.");
        }
        return stringBuilder.toString();
    }

    public static String generateBetMessage(HighRiskBetBO bet) {
        var stringBuilder = new StringBuilder();
        stringBuilder.append("Істрія коефіцієнтів: ").append("\n").append(bet.getHistory()).append("\n").append("\n");
        if (bet.isIgnoreBet()) {
            stringBuilder.append("Цей політ краще пропустити.");
        } else {
            stringBuilder.append("Заберіть коефіцієнт: ").append(bet.getGuaranteedCoefficient()).append("x або більше.");
        }
        return stringBuilder.toString();
    }

    public static String generateBetMessage(BigWinBetBO bet) {
        var stringBuilder = new StringBuilder();
        stringBuilder.append("Істрія коефіцієнтів: ").append("\n").append(bet.getHistory()).append("\n").append("\n");
        if (bet.isIgnoreBet()) {
            stringBuilder.append("Цей політ краще пропустити.").append("\n");
            if (bet.getMinutesToWait() > 0) {
                stringBuilder.append("Почніть гру через ").append(bet.getMinutesToWait()).append(" хвилин.");
            } else {
                stringBuilder.append("Почніть грати через декілька секунд.");
            }
        } else {
            if (bet.getWinTimeRange() > 0) {
                stringBuilder.append("Грайте! Великий шанс виграшу в наступні ").append(bet.getWinTimeRange()).append(" хвилин.").append("\n");
            } else {
                stringBuilder.append("Великий виграш буде з хвилини на хвилину.").append("\n");
            }
            stringBuilder.append("\n").append("Заберіть коефіцієнт: ").append(bet.getGuaranteedCoefficient()).append("x або більше.");
        }
        return stringBuilder.toString();
    }

    public static String generateBetMessage(DoubleBetBO bet) {
        var stringBuilder = new StringBuilder();
        stringBuilder.append("Істрія коефіцієнтів: ").append("\n").append(bet.getHistory()).append("\n").append("\n");
        if (bet.isIgnoreBet()) {
            stringBuilder.append("Цей політ краще пропустити.").append("\n");
        } else {
            stringBuilder.append("Перша ставка. Заберіть коефіцієнт: ").append(bet.getFirstGuaranteedCoefficient()).append("\n");
            stringBuilder.append("Друга ставка. Заберіть коефіцієнт: ").append(bet.getSecondGuaranteedCoefficient()).append("\n");
        }
        stringBuilder.append("\n").append("Підказка: Перша ставка має бути в два рази більша за другу.");
        return stringBuilder.toString();
    }

    public static String generateBetMessage(LowRiskBetBO bet) {
        var stringBuilder = new StringBuilder();
        stringBuilder.append("Істрія коефіцієнтів: ").append("\n").append(bet.getHistory()).append("\n").append("\n");
        if (bet.isIgnoreBet()) {
            stringBuilder.append("Цей політ краще пропустити.");
        } else {
            stringBuilder.append("Заберіть коефіцієнт: ").append(bet.getGuaranteedCoefficient());
        }
        return stringBuilder.toString();
    }
}
