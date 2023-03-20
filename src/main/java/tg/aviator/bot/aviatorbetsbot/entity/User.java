package tg.aviator.bot.aviatorbetsbot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import tg.aviator.bot.aviatorbetsbot.enums.BetLevel;
import tg.aviator.bot.aviatorbetsbot.enums.Role;

import java.time.LocalDateTime;

/**
 * User Entity.
 *
 * @author David Sapozhnik
 */
@Entity
public class User {

    @Id
    @Column(nullable = false, unique = true)
    private Long id;
    private Long chatId;
    private String name;
    private Role role;
    private BetLevel betLevel;
    private boolean accessRequested;
    private boolean accessProvided;
    private LocalDateTime accessRequestedAt;
    private LocalDateTime accessProvidedAt;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public BetLevel getBetLevel() {
        return betLevel;
    }

    public void setBetLevel(BetLevel betLevel) {
        this.betLevel = betLevel;
    }

    public boolean isAccessRequested() {
        return accessRequested;
    }

    public void setAccessRequested(boolean accessRequested) {
        this.accessRequested = accessRequested;
    }

    public boolean isAccessProvided() {
        return accessProvided;
    }

    public void setAccessProvided(boolean accessProvided) {
        this.accessProvided = accessProvided;
    }

    public LocalDateTime getAccessRequestedAt() {
        return accessRequestedAt;
    }

    public void setAccessRequestedAt(LocalDateTime accessRequestedAt) {
        this.accessRequestedAt = accessRequestedAt;
    }

    public LocalDateTime getAccessProvidedAt() {
        return accessProvidedAt;
    }

    public void setAccessProvidedAt(LocalDateTime accessProvidedAt) {
        this.accessProvidedAt = accessProvidedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime created) {
        this.createdAt = created;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", name='" + name + '\'' +
                ", created=" + createdAt +
                '}';
    }
}
