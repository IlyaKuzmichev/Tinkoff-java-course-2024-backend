package edu.java.models;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class User {
    public enum Status {
        BASE,
        TRACK_LINK,
        UNTRACK_LINK
    }

    private Long userId;
    private Status status;

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(userId, user.userId) && status == user.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, status);
    }
}
