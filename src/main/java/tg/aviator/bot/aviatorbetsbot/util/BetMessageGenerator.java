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
        stringBuilder.append("Coefficients history: ").append(bet.getHistory()).append("\n").append("\n");
        if (bet.isIgnoreBet()) {
            stringBuilder.append("Ignore this bet.");
        } else {
            stringBuilder.append("Take profit: ").append(bet.getGuaranteedCoefficient()).append("x or more.");
        }
        return stringBuilder.toString();
    }

    public static String generateBetMessage(HighRiskBetBO bet) {
        var stringBuilder = new StringBuilder();
        stringBuilder.append("Coefficients history: ").append(bet.getHistory()).append("\n").append("\n");
        if (bet.isIgnoreBet()) {
            stringBuilder.append("Ignore this bet.");
        } else {
            stringBuilder.append("Take profit: ").append(bet.getGuaranteedCoefficient()).append("x or more.");
        }
        return stringBuilder.toString();
    }

    public static String generateBetMessage(BigWinBetBO bet) {
        var stringBuilder = new StringBuilder();
        stringBuilder.append("Coefficients history: ").append(bet.getHistory()).append("\n").append("\n");
        if (bet.isIgnoreBet()) {
            stringBuilder.append("Ignore this bet.").append("\n");
            if (bet.getMinutesToWait() > 0) {
                stringBuilder.append("Start beting in ").append(bet.getMinutesToWait()).append(" minutes.");
            } else {
                stringBuilder.append("Start in a few seconds.");
            }
        } else {
            if (bet.getWinTimeRange() > 0) {
                stringBuilder.append("High chance of winning during next ").append(bet.getWinTimeRange()).append(" minutes.").append("\n");
            } else {
                stringBuilder.append("High coefficient is coming soon.").append("\n");
            }
            stringBuilder.append("Take profit: ").append(bet.getGuaranteedCoefficient()).append("x or more.");
        }
        return stringBuilder.toString();
    }

    public static String generateBetMessage(DoubleBetBO bet) {
        var stringBuilder = new StringBuilder();
        stringBuilder.append("Coefficients history: ").append(bet.getHistory()).append("\n").append("\n");
        if (bet.isIgnoreBet()) {
            stringBuilder.append("Ignore this bet.").append("\n");
        } else {
            stringBuilder.append("First bet takes profit: ").append(bet.getFirstGuaranteedCoefficient()).append("\n");
            stringBuilder.append("Second bet takes profit: ").append(bet.getSecondGuaranteedCoefficient()).append("\n");
        }
        stringBuilder.append("Remainder! First bet must be x2 of second bet.");
        return stringBuilder.toString();
    }

    public static String generateBetMessage(LowRiskBetBO bet) {
        var stringBuilder = new StringBuilder();
        stringBuilder.append("Coefficients history: ").append(bet.getHistory()).append("\n").append("\n");
        if (bet.isIgnoreBet()) {
            stringBuilder.append("Ignore this bet.");
        } else {
            stringBuilder.append("Take profit: ").append(bet.getGuaranteedCoefficient());
        }
        return stringBuilder.toString();
    }
}
