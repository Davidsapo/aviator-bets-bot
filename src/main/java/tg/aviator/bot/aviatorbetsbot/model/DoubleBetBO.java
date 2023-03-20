package tg.aviator.bot.aviatorbetsbot.model;

/**
 * Medium Risk Bet BO.
 *
 * @author David Sapozhnik
 */
public class DoubleBetBO extends BasicBetBO {

    private Double firstGuaranteedCoefficient;
    private Double secondGuaranteedCoefficient;

    public Double getFirstGuaranteedCoefficient() {
        return firstGuaranteedCoefficient;
    }

    public void setFirstGuaranteedCoefficient(Double firstGuaranteedCoefficient) {
        this.firstGuaranteedCoefficient = firstGuaranteedCoefficient;
    }

    public Double getSecondGuaranteedCoefficient() {
        return secondGuaranteedCoefficient;
    }

    public void setSecondGuaranteedCoefficient(Double secondGuaranteedCoefficient) {
        this.secondGuaranteedCoefficient = secondGuaranteedCoefficient;
    }
}
