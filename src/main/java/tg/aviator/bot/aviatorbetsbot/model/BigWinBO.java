package tg.aviator.bot.aviatorbetsbot.model;

import java.time.LocalDateTime;

/**
 * @author David Sapozhnik
 */
public class BigWinBO {

    private double coefficient;
    private String catchTime;
    private String  timeSpent;
    private int tries;

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public String getCatchTime() {
        return catchTime;
    }

    public void setCatchTime(String catchTime) {
        this.catchTime = catchTime;
    }

    public String getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(String timeSpent) {
        this.timeSpent = timeSpent;
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }

    @Override
    public String toString() {
        return "coefficient=" + coefficient + '\n' +
                "catchTime=" + catchTime + '\n' +
                "timeSpent=" + timeSpent + '\n' +
                "tries=" + tries;
    }
}
