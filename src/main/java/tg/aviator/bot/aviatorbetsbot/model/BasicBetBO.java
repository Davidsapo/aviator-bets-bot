package tg.aviator.bot.aviatorbetsbot.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic Bet BO.
 *
 * @author David Sapozhnik
 */
public abstract class BasicBetBO {

    protected final List<Double> history = new ArrayList<>();
    protected boolean ignoreBet;

    public List<Double> getHistory() {
        return history;
    }

    public boolean isIgnoreBet() {
        return ignoreBet;
    }

    public void setIgnoreBet(boolean ignoreBet) {
        this.ignoreBet = ignoreBet;
    }
}
