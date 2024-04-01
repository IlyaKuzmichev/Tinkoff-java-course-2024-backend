/*
 * This file is generated by jOOQ.
 */

package edu.java.domain.jooqcodegen.tables.pojos;

import edu.java.domain.jooqcodegen.enums.UserStatusEnum;
import java.beans.ConstructorProperties;
import java.io.Serializable;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes", "this-escape"})
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private UserStatusEnum userStatus;

    public Users() {
    }

    public Users(Users value) {
        this.id = value.id;
        this.userStatus = value.userStatus;
    }

    @ConstructorProperties({"id", "userStatus"})
    public Users(
        @NotNull Long id,
        @Nullable UserStatusEnum userStatus
    ) {
        this.id = id;
        this.userStatus = userStatus;
    }

    /**
     * Getter for <code>USERS.ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Long getId() {
        return this.id;
    }

    /**
     * Setter for <code>USERS.ID</code>.
     */
    public void setId(@NotNull Long id) {
        this.id = id;
    }

    /**
     * Getter for <code>USERS.USER_STATUS</code>.
     */
    @Nullable
    public UserStatusEnum getUserStatus() {
        return this.userStatus;
    }

    /**
     * Setter for <code>USERS.USER_STATUS</code>.
     */
    public void setUserStatus(@Nullable UserStatusEnum userStatus) {
        this.userStatus = userStatus;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Users other = (Users) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        if (this.userStatus == null) {
            if (other.userStatus != null) {
                return false;
            }
        } else if (!this.userStatus.equals(other.userStatus)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.userStatus == null) ? 0 : this.userStatus.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Users (");

        sb.append(id);
        sb.append(", ").append(userStatus);

        sb.append(")");
        return sb.toString();
    }
}
