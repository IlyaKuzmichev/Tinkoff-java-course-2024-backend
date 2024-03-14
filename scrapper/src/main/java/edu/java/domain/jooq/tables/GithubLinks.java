/*
 * This file is generated by jOOQ.
 */

package edu.java.domain.jooq.tables;

import edu.java.domain.jooq.DefaultSchema;
import edu.java.domain.jooq.Keys;
import edu.java.domain.jooq.tables.records.GithubLinksRecord;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function4;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row4;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

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
public class GithubLinks extends TableImpl<GithubLinksRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>GITHUB_LINKS</code>
     */
    public static final GithubLinks GITHUB_LINKS = new GithubLinks();

    /**
     * The class holding records for this type
     */
    @Override
    @NotNull
    public Class<GithubLinksRecord> getRecordType() {
        return GithubLinksRecord.class;
    }

    /**
     * The column <code>GITHUB_LINKS.LINK_ID</code>.
     */
    public final TableField<GithubLinksRecord, Long> LINK_ID =
        createField(DSL.name("LINK_ID"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>GITHUB_LINKS.LAST_UPDATE</code>.
     */
    public final TableField<GithubLinksRecord, OffsetDateTime> LAST_UPDATE =
        createField(DSL.name("LAST_UPDATE"), SQLDataType.TIMESTAMPWITHTIMEZONE(6), this, "");

    /**
     * The column <code>GITHUB_LINKS.LAST_PUSH</code>.
     */
    public final TableField<GithubLinksRecord, OffsetDateTime> LAST_PUSH =
        createField(DSL.name("LAST_PUSH"), SQLDataType.TIMESTAMPWITHTIMEZONE(6), this, "");

    /**
     * The column <code>GITHUB_LINKS.PULL_REQUESTS_COUNT</code>.
     */
    public final TableField<GithubLinksRecord, Integer> PULL_REQUESTS_COUNT =
        createField(DSL.name("PULL_REQUESTS_COUNT"),
            SQLDataType.INTEGER.nullable(false).defaultValue(DSL.field(DSL.raw("0"), SQLDataType.INTEGER)),
            this,
            ""
        );

    private GithubLinks(Name alias, Table<GithubLinksRecord> aliased) {
        this(alias, aliased, null);
    }

    private GithubLinks(Name alias, Table<GithubLinksRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>GITHUB_LINKS</code> table reference
     */
    public GithubLinks(String alias) {
        this(DSL.name(alias), GITHUB_LINKS);
    }

    /**
     * Create an aliased <code>GITHUB_LINKS</code> table reference
     */
    public GithubLinks(Name alias) {
        this(alias, GITHUB_LINKS);
    }

    /**
     * Create a <code>GITHUB_LINKS</code> table reference
     */
    public GithubLinks() {
        this(DSL.name("GITHUB_LINKS"), null);
    }

    public <O extends Record> GithubLinks(Table<O> child, ForeignKey<O, GithubLinksRecord> key) {
        super(child, key, GITHUB_LINKS);
    }

    @Override
    @Nullable
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    @NotNull
    public UniqueKey<GithubLinksRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_3;
    }

    @Override
    @NotNull
    public List<ForeignKey<GithubLinksRecord, ?>> getReferences() {
        return Arrays.asList(Keys.CONSTRAINT_3A);
    }

    private transient Links _links;

    /**
     * Get the implicit join path to the <code>PUBLIC.LINKS</code> table.
     */
    public Links links() {
        if (_links == null) {
            _links = new Links(this, Keys.CONSTRAINT_3A);
        }

        return _links;
    }

    @Override
    @NotNull
    public GithubLinks as(String alias) {
        return new GithubLinks(DSL.name(alias), this);
    }

    @Override
    @NotNull
    public GithubLinks as(Name alias) {
        return new GithubLinks(alias, this);
    }

    @Override
    @NotNull
    public GithubLinks as(Table<?> alias) {
        return new GithubLinks(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public GithubLinks rename(String name) {
        return new GithubLinks(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public GithubLinks rename(Name name) {
        return new GithubLinks(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public GithubLinks rename(Table<?> name) {
        return new GithubLinks(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row4<Long, OffsetDateTime, OffsetDateTime, Integer> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function4<? super Long, ? super OffsetDateTime, ? super OffsetDateTime, ? super Integer, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(
        Class<U> toType,
        Function4<? super Long, ? super OffsetDateTime, ? super OffsetDateTime, ? super Integer, ? extends U> from
    ) {
        return convertFrom(toType, Records.mapping(from));
    }
}
