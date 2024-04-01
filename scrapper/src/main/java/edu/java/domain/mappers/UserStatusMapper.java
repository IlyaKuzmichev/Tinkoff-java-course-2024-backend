package edu.java.domain.mappers;

import edu.java.models.User;

public final class UserStatusMapper {
    private static final String BASE_STRING = "base";
    private static final String TRACK_STRING = "track_link";
    private static final String UNTRACK_STRING = "untrack_link";

    private UserStatusMapper() {}

    public static String userStatusToString(User.Status status) {
        return switch (status) {
            case BASE -> BASE_STRING;
            case TRACK_LINK -> TRACK_STRING;
            case UNTRACK_LINK -> UNTRACK_STRING;
        };
    }

    public static User.Status userStatusFromString(String status) {
        return switch (status) {
            case BASE_STRING -> User.Status.BASE;
            case TRACK_STRING -> User.Status.TRACK_LINK;
            case UNTRACK_STRING -> User.Status.UNTRACK_LINK;
            default -> throw new RuntimeException("don't match any User.Status value");
        };
    }
}
