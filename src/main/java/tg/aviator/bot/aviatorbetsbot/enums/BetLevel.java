package tg.aviator.bot.aviatorbetsbot.enums;

/**
 * Bet Level.
 *
 * @author David Sapozhnik
 */
public enum BetLevel {

    LOW("Без ризиків"),
    MEDIUM("Середній ризик - середній коефіцієнт"),
    HIGH("Високий ризик - високий кокфіціент"),
    BIG("Великий виграш 100х"),
    DOUBLE("Подвійна ставка");

    private final String name;

    BetLevel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
