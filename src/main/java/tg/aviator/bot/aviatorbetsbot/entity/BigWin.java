package tg.aviator.bot.aviatorbetsbot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import tg.aviator.bot.aviatorbetsbot.enums.Role;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Big Win Entity.
 *
 * @author David Sapozhnik
 */
@Entity
@Table(name = "big_win")
public class BigWin {

    @Id
    @Column(nullable = false, unique = true)
    private Long id;
    private double coefficient;
    private LocalDateTime catchTime;
    private long timeSpent;
    private int tries;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public LocalDateTime getCatchTime() {
        return catchTime;
    }

    public void setCatchTime(LocalDateTime catchTime) {
        this.catchTime = catchTime;
    }

    public long getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }
}
