package tg.aviator.bot.aviatorbetsbot.model;

/**
 * Medium Risk Bet BO.
 *
 * @author David Sapozhnik
 */
public class MediumRiskBetBO extends BasicBetBO {

    private Double guaranteedCoefficient;

    public Double getGuaranteedCoefficient() {
        return guaranteedCoefficient;
    }

    public void setGuaranteedCoefficient(Double guaranteedCoefficient) {
        this.guaranteedCoefficient = guaranteedCoefficient;
    }
}
