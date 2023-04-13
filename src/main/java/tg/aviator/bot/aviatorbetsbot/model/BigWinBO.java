package tg.aviator.bot.aviatorbetsbot.model;

import java.time.LocalDateTime;

/**
 * @author David Sapozhnik
 */
public class BigWinBO {

    private double coefficient;
    private LocalDateTime catchTime;
    private long timeSpent;
    private int tries;

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public LocalDateTime getCatchTime() {
        return catchTime;
    }

    public void setCatchTime(LocalDateTime catchTime) {
        this.catchTime = catchTime;
    }

    public long getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(long timeSpent) {
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
        return "BigWinBO{" +
                "coefficient=" + coefficient +
                ", catchTime=" + catchTime +
                ", timeSpent=" + timeSpent +
                ", tries=" + tries +
                '}';
    }
}
