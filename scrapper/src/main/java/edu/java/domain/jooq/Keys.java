/*
 * This file is generated by jOOQ.
 */

package edu.java.domain.jooq;

import edu.java.domain.jooq.tables.GithubLinks;
import edu.java.domain.jooq.tables.Links;
import edu.java.domain.jooq.tables.StackoverflowLinks;
import edu.java.domain.jooq.tables.UserTrackedLinks;
import edu.java.domain.jooq.tables.Users;
import edu.java.domain.jooq.tables.records.GithubLinksRecord;
import edu.java.domain.jooq.tables.records.LinksRecord;
import edu.java.domain.jooq.tables.records.StackoverflowLinksRecord;
import edu.java.domain.jooq.tables.records.UserTrackedLinksRecord;
import edu.java.domain.jooq.tables.records.UsersRecord;
import javax.annotation.processing.Generated;
import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;

/**
 * A class modelling foreign key relationships and constraints of tables in the
 * default schema.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes", "this-escape"})
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<GithubLinksRecord> CONSTRAINT_3 = Internal.createUniqueKey(GithubLinks.GITHUB_LINKS,
        DSL.name("CONSTRAINT_3"),
        new TableField[] {GithubLinks.GITHUB_LINKS.LINK_ID},
        true
    );
    public static final UniqueKey<LinksRecord> CONSTRAINT_45 =
        Internal.createUniqueKey(Links.LINKS, DSL.name("CONSTRAINT_45"), new TableField[] {Links.LINKS.ID}, true);
    public static final UniqueKey<LinksRecord> CONSTRAINT_451 =
        Internal.createUniqueKey(Links.LINKS, DSL.name("CONSTRAINT_451"), new TableField[] {Links.LINKS.URL}, true);
    public static final UniqueKey<StackoverflowLinksRecord> CONSTRAINT_1 =
        Internal.createUniqueKey(StackoverflowLinks.STACKOVERFLOW_LINKS,
            DSL.name("CONSTRAINT_1"),
            new TableField[] {StackoverflowLinks.STACKOVERFLOW_LINKS.LINK_ID},
            true
        );
    public static final UniqueKey<UserTrackedLinksRecord> CONSTRAINT_6BE =
        Internal.createUniqueKey(UserTrackedLinks.USER_TRACKED_LINKS,
            DSL.name("CONSTRAINT_6BE"),
            new TableField[] {UserTrackedLinks.USER_TRACKED_LINKS.USER_ID, UserTrackedLinks.USER_TRACKED_LINKS.LINK_ID},
            true
        );
    public static final UniqueKey<UsersRecord> CONSTRAINT_4 =
        Internal.createUniqueKey(Users.USERS, DSL.name("CONSTRAINT_4"), new TableField[] {Users.USERS.ID}, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<GithubLinksRecord, LinksRecord> CONSTRAINT_3A =
        Internal.createForeignKey(GithubLinks.GITHUB_LINKS,
            DSL.name("CONSTRAINT_3A"),
            new TableField[] {GithubLinks.GITHUB_LINKS.LINK_ID},
            Keys.CONSTRAINT_45,
            new TableField[] {Links.LINKS.ID},
            true
        );
    public static final ForeignKey<StackoverflowLinksRecord, LinksRecord> CONSTRAINT_1C = Internal.createForeignKey(
        StackoverflowLinks.STACKOVERFLOW_LINKS,
        DSL.name("CONSTRAINT_1C"),
        new TableField[] {StackoverflowLinks.STACKOVERFLOW_LINKS.LINK_ID},
        Keys.CONSTRAINT_45,
        new TableField[] {Links.LINKS.ID},
        true
    );
    public static final ForeignKey<UserTrackedLinksRecord, UsersRecord> CONSTRAINT_6 = Internal.createForeignKey(
        UserTrackedLinks.USER_TRACKED_LINKS,
        DSL.name("CONSTRAINT_6"),
        new TableField[] {UserTrackedLinks.USER_TRACKED_LINKS.USER_ID},
        Keys.CONSTRAINT_4,
        new TableField[] {Users.USERS.ID},
        true
    );
    public static final ForeignKey<UserTrackedLinksRecord, LinksRecord> CONSTRAINT_6B = Internal.createForeignKey(
        UserTrackedLinks.USER_TRACKED_LINKS,
        DSL.name("CONSTRAINT_6B"),
        new TableField[] {UserTrackedLinks.USER_TRACKED_LINKS.LINK_ID},
        Keys.CONSTRAINT_45,
        new TableField[] {Links.LINKS.ID},
        true
    );
}
