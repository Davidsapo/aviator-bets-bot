package tg.aviator.bot.aviatorbetsbot.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import tg.aviator.bot.aviatorbetsbot.entity.User;
import tg.aviator.bot.aviatorbetsbot.enums.Role;

import java.awt.print.Pageable;
import java.util.Arrays;
import java.util.List;

/**
 * User Repository.
 *
 * @author David Sapozhnik
 */
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByRole(Role role, PageRequest pageable);

    List<User> findAllByRoleAndAccessRequested(Role role, boolean accessRequested, PageRequest pageable);

    boolean existsByAccessRequested(boolean b);

    List<User> findAllByRoleIn(List<Role> admin);
}
