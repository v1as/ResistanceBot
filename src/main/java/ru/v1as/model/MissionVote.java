package ru.v1as.model;

import com.google.common.base.Preconditions;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;

import java.util.Objects;

/**
 * Created by ivlasishen
 * on 17.04.2017.
 */
public class MissionVote {

    private final User user;
    private Boolean vote;

    public MissionVote(User user) {
        this.user = Preconditions.checkNotNull(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MissionVote that = (MissionVote) o;

        if (vote != that.vote) return false;
        return Objects.equals(user.getId(), that.user.getId());
    }

    @Override
    public int hashCode() {
        int result = user.getId().hashCode();
        result = 31 * result + (vote ? 1 : 0);
        return result;
    }

    public User getUser() {
        return user;
    }

    public Boolean getVote() {
        return vote;
    }

    public void setVote(Boolean vote) {
        this.vote = vote;
    }
}
