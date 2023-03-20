package tg.aviator.bot.aviatorbetsbot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import tg.aviator.bot.aviatorbetsbot.entity.User;
import tg.aviator.bot.aviatorbetsbot.enums.Role;
import tg.aviator.bot.aviatorbetsbot.model.UserBO;
import tg.aviator.bot.aviatorbetsbot.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static tg.aviator.bot.aviatorbetsbot.enums.Role.USER;

/**
 * User Service.
 *
 * @author David Sapozhnik
 */
@Component
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private static final int PAGE_SIZE = 10;
    private static final String SORT_FIELD = "createdAt";

    @Autowired
    private UserRepository userRepository;

    public void createUser(Long id, Long chatId, String firstNme, String lastName) {
        if (!userRepository.existsById(id)) {
            var user = new User();
            user.setId(id);
            user.setChatId(chatId);
            user.setName(firstNme + " " + lastName);
            user.setRole(USER);
            user.setCreatedAt(LocalDateTime.now());
            userRepository.save(user);
            LOG.info("User {} created", user);
        }
    }

    public void requestAccess(Long id) {
        userRepository.findById(id)
                .ifPresent(user -> {
                    user.setAccessRequested(true);
                    user.setAccessRequestedAt(LocalDateTime.now());
                    userRepository.save(user);
                    LOG.info("Access requested by user {}", user);
                });
    }

    public boolean isAccessProvided(Long id) {
        return userRepository.findById(id)
                .map(User::isAccessProvided)
                .orElse(false);
    }

    public boolean isAccessRequested(Long id) {
        return userRepository.findById(id)
                .map(User::isAccessRequested)
                .orElse(false);
    }

    public boolean isAccessDenied(Long id) {
        return userRepository.findById(id)
                .map(user -> !user.isAccessProvided() && !user.isAccessRequested())
                .orElse(false);
    }

    public void provideAccess(Long id) {
        userRepository.findById(id)
                .ifPresent(user -> {
                    user.setAccessProvided(true);
                    user.setAccessRequested(false);
                    user.setAccessProvidedAt(LocalDateTime.now());
                    userRepository.save(user);
                    LOG.info("Access provided to user {}", user);
                });
    }

    public void denyAccess(Long id) {
        userRepository.findById(id)
                .ifPresent(user -> {
                    user.setAccessProvided(false);
                    user.setAccessRequested(false);
                    userRepository.save(user);
                    LOG.info("Access denied to user {}", user);
                });
    }

    public boolean isOwner(Long id) {
        return userRepository.findById(id)
                .map(user -> user.getRole() == Role.OWNER)
                .orElse(false);
    }

    public boolean isAdmin(Long id) {
        return userRepository.findById(id)
                .map(user -> user.getRole() == Role.ADMIN)
                .orElse(false);
    }

    public boolean isOwnerOrAdmin(Long id) {
        return userRepository.findById(id)
                .map(user -> user.getRole() == Role.OWNER || user.getRole() == Role.ADMIN)
                .orElse(false);
    }

    public List<UserBO> getUsers(int page) {
        var pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(SORT_FIELD).descending());
        return userRepository.findAllByRole(USER, pageable).stream()
                .map(user -> {
                    var userBO = new UserBO();
                    userBO.setId(user.getId());
                    userBO.setName(user.getName());
                    userBO.setAccessRequested(user.isAccessRequested());
                    userBO.setAccessProvided(user.isAccessProvided());
                    userBO.setAccessRequestedAt(user.getAccessRequestedAt());
                    userBO.setAccessProvidedAt(user.getAccessProvidedAt());
                    userBO.setCreatedAt(user.getCreatedAt());
                    return userBO;
                })
                .toList();
    }

    public List<UserBO> getUsersWithAccessRequest(int page) {
        var pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(SORT_FIELD).descending());
        return userRepository.findAllByRoleAndAccessRequested(USER, true, pageable).stream()
                .map(user -> {
                    var userBO = new UserBO();
                    userBO.setId(user.getId());
                    userBO.setName(user.getName());
                    userBO.setAccessRequested(user.isAccessRequested());
                    userBO.setAccessProvided(user.isAccessProvided());
                    userBO.setAccessRequestedAt(user.getAccessRequestedAt());
                    userBO.setAccessProvidedAt(user.getAccessProvidedAt());
                    userBO.setCreatedAt(user.getCreatedAt());
                    return userBO;
                })
                .toList();
    }

    public List<Long> getAdminsAndOwnersChatIds() {
        return userRepository.findAllByRoleIn(List.of(Role.ADMIN, Role.OWNER)).stream()
                .map(User::getChatId)
                .toList();
    }

    public Long getChatIdByUserId(Long id) {
        return userRepository.findById(id)
                .map(User::getChatId)
                .orElse(null);
    }
}
