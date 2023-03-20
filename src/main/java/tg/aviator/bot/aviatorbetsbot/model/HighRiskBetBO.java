package tg.aviator.bot.aviatorbetsbot.model;

/**
 * High Risk Bet BO.
 *
 * @author David Sapozhnik
 */
public class HighRiskBetBO extends BasicBetBO {

    private Double guaranteedCoefficient;

    public Double getGuaranteedCoefficient() {
        return guaranteedCoefficient;
    }

    public void setGuaranteedCoefficient(Double guaranteedCoefficient) {
        this.guaranteedCoefficient = guaranteedCoefficient;
    }
}
