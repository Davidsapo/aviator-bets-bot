package tg.aviator.bot.aviatorbetsbot.model;

/**
 * Big Win Bet BO.
 *
 * @author David Sapozhnik
 */
public class BigWinBetBO extends BasicBetBO {

    private Double guaranteedCoefficient;
    private long minutesToWait;
    private long winTimeRange;

    public Double getGuaranteedCoefficient() {
        return guaranteedCoefficient;
    }

    public void setGuaranteedCoefficient(Double guaranteedCoefficient) {
        this.guaranteedCoefficient = guaranteedCoefficient;
    }

    public long getMinutesToWait() {
        return minutesToWait;
    }

    public void setMinutesToWait(long minutesToWait) {
        this.minutesToWait = minutesToWait;
    }

    public long getWinTimeRange() {
        return winTimeRange;
    }

    public void setWinTimeRange(long winTimeRange) {
        this.winTimeRange = winTimeRange;
    }
}
