package tg.aviator.bot.aviatorbetsbot.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author David Sapozhnik
 */
public class UserBO {

    private Long id;
    private String name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "id=" + id + "\n" +
                "name='" + name + '\'' + "\n" +
                "accessRequested=" + accessRequested + "\n" +
                "accessProvided=" + accessProvided + "\n" +
                "accessRequestedAt=" + accessRequestedAt + "\n" +
                "accessProvidedAt=" + accessProvidedAt + "\n" +
                "createdAt=" + createdAt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    }
}
