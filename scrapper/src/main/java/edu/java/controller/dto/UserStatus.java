package edu.java.controller.dto;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import edu.java.exception.NotUserStatusException;
import edu.java.models.User;
import java.io.IOException;
import lombok.Getter;
import lombok.Setter;

@JsonDeserialize(using = UserStatus.UserStatusDeserializer.class)
public class UserStatus {
    private static final String BASE_STRING = "base";
    private static final String TRACK_STRING = "track_link";
    private static final String UNTRACK_STRING = "untrack_link";

    @Getter
    @Setter
    private User.Status status;

    public UserStatus(User.Status status) {
        this.status = status;
    }

    public static class UserStatusDeserializer extends StdDeserializer<UserStatus> {
        private UserStatusDeserializer() {
            super(UserStatus.class);
        }

        @Override
        public UserStatus deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
            String stringifiedStatus = jsonParser.getValueAsString();
            User.Status status = switch (stringifiedStatus) {
                case BASE_STRING -> User.Status.BASE;
                case TRACK_STRING -> User.Status.TRACK_LINK_WAITING;
                case UNTRACK_STRING -> User.Status.UNTRACK_LINK_WAITING;
                default -> throw new NotUserStatusException(stringifiedStatus);
            };
            return new UserStatus(status);
        }
    }
}
