package tg.aviator.bot.aviatorbetsbot.model;

/**
 * Low Risk Bet BO.
 *
 * @author David Sapozhnik
 */
public class LowRiskBetBO extends BasicBetBO {

    private Double guaranteedCoefficient;

    public Double getGuaranteedCoefficient() {
        return guaranteedCoefficient;
    }

    public void setGuaranteedCoefficient(Double guaranteedCoefficient) {
        this.guaranteedCoefficient = guaranteedCoefficient;
    }
}
