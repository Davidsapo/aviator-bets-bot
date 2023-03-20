package tg.aviator.bot.aviatorbetsbot.service.bet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tg.aviator.bot.aviatorbetsbot.enums.BetLevel;
import tg.aviator.bot.aviatorbetsbot.exception.HistoryServiceUnavailableException;
import tg.aviator.bot.aviatorbetsbot.model.BasicBetBO;
import tg.aviator.bot.aviatorbetsbot.service.HistoryService;

/**
 * Bets Service.
 *
 * @author David Sapozhnik
 */
@Component
public abstract class BasicBetsService {

    @Autowired
    protected HistoryService historyService;

    public abstract BasicBetBO calculateBet() throws HistoryServiceUnavailableException;

    public abstract BetLevel getBetLevel();
}

