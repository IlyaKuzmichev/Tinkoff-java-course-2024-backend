package edu.java.domain.jooq;

import edu.java.domain.jooqcodegen.enums.UserStatusEnum;
import edu.java.domain.jooqcodegen.tables.UserTrackedLinks;
import edu.java.domain.jooqcodegen.tables.Users;
import edu.java.domain.jooqcodegen.tables.records.UserTrackedLinksRecord;
import edu.java.domain.mappers.UserStatusMapper;
import edu.java.exception.AttemptDoubleRegistrationException;
import edu.java.exception.UserIdNotFoundException;
import edu.java.models.Link;
import edu.java.models.User;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JooqUserRepository {

    private final DSLContext dslContext;

    @Autowired
    public JooqUserRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Transactional
    public void addUser(User user) {
        try {
            dslContext
                .insertInto(Users.USERS)
                .set(Users.USERS.ID, user.getUserId())
                .execute();
        } catch (DuplicateKeyException e) {
            throw new AttemptDoubleRegistrationException(
                String.format("User with ID %d already exists", user.getUserId())
            );
        }
    }

    @Transactional
    public void updateUser(User user) {
        int rowsAffected = dslContext
            .update(Users.USERS)
            .set(Users.USERS.USER_STATUS, UserStatusEnum.valueOf(
                UserStatusMapper.userStatusToString(user.getStatus())))
            .where(Users.USERS.ID.eq(user.getUserId()))
            .execute();
        if (rowsAffected == 0) {
            throw new UserIdNotFoundException(user.getUserId());
        }
    }

    @Transactional
    public void removeUser(Long userId) {
        int rowsAffected = dslContext
            .deleteFrom(Users.USERS)
            .where(Users.USERS.ID.eq(userId))
            .execute();
        if (rowsAffected == 0) {
            throw new UserIdNotFoundException(userId);
        }
    }

    @Transactional
    public Optional<User> findUser(Long userId) {
        return dslContext
            .selectFrom(Users.USERS)
            .where(Users.USERS.ID.eq(userId))
            .fetchOptional()
            .map(rec -> new User(rec.getId(), UserStatusMapper.userStatusFromString(
                Objects.requireNonNull(rec.getUserStatus()).toString())));
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return dslContext
            .selectFrom(Users.USERS)
            .fetch()
            .map(rec -> new User(rec.getId(), UserStatusMapper.userStatusFromString(
                Objects.requireNonNull(rec.getUserStatus()).toString())));
    }

    @Transactional(readOnly = true)
    public List<Long> findUsersTrackLink(Link link) {
        return dslContext
            .selectFrom(UserTrackedLinks.USER_TRACKED_LINKS)
            .where(UserTrackedLinks.USER_TRACKED_LINKS.LINK_ID.eq(link.getId()))
            .fetchStream()
            .map(UserTrackedLinksRecord::getUserId)
            .collect(Collectors.toList());
    }
}
