/*
 * This file is generated by jOOQ.
 */

package edu.java.domain.jooqcodegen.tables;

import edu.java.domain.jooqcodegen.DefaultSchema;
import edu.java.domain.jooqcodegen.Keys;
import edu.java.domain.jooqcodegen.enums.LinkTypeEnum;
import edu.java.domain.jooqcodegen.tables.records.LinksRecord;
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
import org.jooq.Identity;
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
public class Links extends TableImpl<LinksRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>LINKS</code>
     */
    public static final Links LINKS = new Links();

    /**
     * The class holding records for this type
     */
    @Override
    @NotNull
    public Class<LinksRecord> getRecordType() {
        return LinksRecord.class;
    }

    /**
     * The column <code>LINKS.ID</code>.
     */
    public final TableField<LinksRecord, Long> ID =
        createField(DSL.name("ID"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>LINKS.LINK_TYPE</code>.
     */
    public final TableField<LinksRecord, LinkTypeEnum> LINK_TYPE = createField(DSL.name("LINK_TYPE"),
            SQLDataType.VARCHAR.nullable(false).asEnumDataType(edu.java.domain.jooqcodegen.enums.LinkTypeEnum.class),
        this,
        ""
    );

    /**
     * The column <code>LINKS.URL</code>.
     */
    public final TableField<LinksRecord, String> URL =
        createField(DSL.name("URL"), SQLDataType.VARCHAR(1000000000).nullable(false), this, "");

    /**
     * The column <code>LINKS.LAST_CHECK</code>.
     */
    public final TableField<LinksRecord, OffsetDateTime> LAST_CHECK =
        createField(DSL.name("LAST_CHECK"), SQLDataType.TIMESTAMPWITHTIMEZONE(6), this, "");

    private Links(Name alias, Table<LinksRecord> aliased) {
        this(alias, aliased, null);
    }

    private Links(Name alias, Table<LinksRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>LINKS</code> table reference
     */
    public Links(String alias) {
        this(DSL.name(alias), LINKS);
    }

    /**
     * Create an aliased <code>LINKS</code> table reference
     */
    public Links(Name alias) {
        this(alias, LINKS);
    }

    /**
     * Create a <code>LINKS</code> table reference
     */
    public Links() {
        this(DSL.name("LINKS"), null);
    }

    public <O extends Record> Links(Table<O> child, ForeignKey<O, LinksRecord> key) {
        super(child, key, LINKS);
    }

    @Override
    @Nullable
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    @NotNull
    public Identity<LinksRecord, Long> getIdentity() {
        return (Identity<LinksRecord, Long>) super.getIdentity();
    }

    @Override
    @NotNull
    public UniqueKey<LinksRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_45;
    }

    @Override
    @NotNull
    public List<UniqueKey<LinksRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.CONSTRAINT_451);
    }

    @Override
    @NotNull
    public Links as(String alias) {
        return new Links(DSL.name(alias), this);
    }

    @Override
    @NotNull
    public Links as(Name alias) {
        return new Links(alias, this);
    }

    @Override
    @NotNull
    public Links as(Table<?> alias) {
        return new Links(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public Links rename(String name) {
        return new Links(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public Links rename(Name name) {
        return new Links(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public Links rename(Table<?> name) {
        return new Links(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row4<Long, LinkTypeEnum, String, OffsetDateTime> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function4<? super Long, ? super LinkTypeEnum, ? super String, ? super OffsetDateTime, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(
        Class<U> toType,
        Function4<? super Long, ? super LinkTypeEnum, ? super String, ? super OffsetDateTime, ? extends U> from
    ) {
        return convertFrom(toType, Records.mapping(from));
    }
}
