package tg.aviator.bot.aviatorbetsbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.aviator.bot.aviatorbetsbot.entity.BigWin;

/**
 * Big Win Repository.
 *
 * @author David Sapozhnik
 */
public interface BigWinRepository extends JpaRepository<BigWin, Long> {

}
