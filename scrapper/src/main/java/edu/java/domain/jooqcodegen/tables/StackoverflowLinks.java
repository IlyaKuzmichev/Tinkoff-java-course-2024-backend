/*
 * This file is generated by jOOQ.
 */

package edu.java.domain.jooqcodegen.tables;

import edu.java.domain.jooqcodegen.DefaultSchema;
import edu.java.domain.jooqcodegen.Keys;
import edu.java.domain.jooqcodegen.tables.records.StackoverflowLinksRecord;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function3;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row3;
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
public class StackoverflowLinks extends TableImpl<StackoverflowLinksRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>STACKOVERFLOW_LINKS</code>
     */
    public static final StackoverflowLinks STACKOVERFLOW_LINKS = new StackoverflowLinks();

    /**
     * The class holding records for this type
     */
    @Override
    @NotNull
    public Class<StackoverflowLinksRecord> getRecordType() {
        return StackoverflowLinksRecord.class;
    }

    /**
     * The column <code>STACKOVERFLOW_LINKS.LINK_ID</code>.
     */
    public final TableField<StackoverflowLinksRecord, Long> LINK_ID =
        createField(DSL.name("LINK_ID"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>STACKOVERFLOW_LINKS.LAST_UPDATE</code>.
     */
    public final TableField<StackoverflowLinksRecord, OffsetDateTime> LAST_UPDATE =
        createField(DSL.name("LAST_UPDATE"), SQLDataType.TIMESTAMPWITHTIMEZONE(6), this, "");

    /**
     * The column <code>STACKOVERFLOW_LINKS.ANSWERS_COUNT</code>.
     */
    public final TableField<StackoverflowLinksRecord, Integer> ANSWERS_COUNT = createField(DSL.name("ANSWERS_COUNT"),
        SQLDataType.INTEGER.nullable(false).defaultValue(DSL.field(DSL.raw("0"), SQLDataType.INTEGER)),
        this,
        ""
    );

    private StackoverflowLinks(Name alias, Table<StackoverflowLinksRecord> aliased) {
        this(alias, aliased, null);
    }

    private StackoverflowLinks(Name alias, Table<StackoverflowLinksRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>STACKOVERFLOW_LINKS</code> table reference
     */
    public StackoverflowLinks(String alias) {
        this(DSL.name(alias), STACKOVERFLOW_LINKS);
    }

    /**
     * Create an aliased <code>STACKOVERFLOW_LINKS</code> table reference
     */
    public StackoverflowLinks(Name alias) {
        this(alias, STACKOVERFLOW_LINKS);
    }

    /**
     * Create a <code>STACKOVERFLOW_LINKS</code> table reference
     */
    public StackoverflowLinks() {
        this(DSL.name("STACKOVERFLOW_LINKS"), null);
    }

    public <O extends Record> StackoverflowLinks(Table<O> child, ForeignKey<O, StackoverflowLinksRecord> key) {
        super(child, key, STACKOVERFLOW_LINKS);
    }

    @Override
    @Nullable
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    @NotNull
    public UniqueKey<StackoverflowLinksRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_1;
    }

    @Override
    @NotNull
    public List<ForeignKey<StackoverflowLinksRecord, ?>> getReferences() {
        return Arrays.asList(Keys.CONSTRAINT_1C);
    }

    private transient Links _links;

    /**
     * Get the implicit join path to the <code>PUBLIC.LINKS</code> table.
     */
    public Links links() {
        if (_links == null) {
            _links = new Links(this, Keys.CONSTRAINT_1C);
        }

        return _links;
    }

    @Override
    @NotNull
    public StackoverflowLinks as(String alias) {
        return new StackoverflowLinks(DSL.name(alias), this);
    }

    @Override
    @NotNull
    public StackoverflowLinks as(Name alias) {
        return new StackoverflowLinks(alias, this);
    }

    @Override
    @NotNull
    public StackoverflowLinks as(Table<?> alias) {
        return new StackoverflowLinks(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public StackoverflowLinks rename(String name) {
        return new StackoverflowLinks(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public StackoverflowLinks rename(Name name) {
        return new StackoverflowLinks(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public StackoverflowLinks rename(Table<?> name) {
        return new StackoverflowLinks(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row3<Long, OffsetDateTime, Integer> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function3<? super Long, ? super OffsetDateTime, ? super Integer, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(
        Class<U> toType,
        Function3<? super Long, ? super OffsetDateTime, ? super Integer, ? extends U> from
    ) {
        return convertFrom(toType, Records.mapping(from));
    }
}
