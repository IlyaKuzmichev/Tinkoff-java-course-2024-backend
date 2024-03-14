/*
 * This file is generated by jOOQ.
 */

package edu.java.domain.jooq.enums;

import edu.java.domain.jooq.DefaultSchema;
import javax.annotation.processing.Generated;
import org.jooq.Catalog;
import org.jooq.EnumType;
import org.jooq.Schema;

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
public enum LinkTypeEnum implements EnumType {

    github("github"),

    stackoverflow("stackoverflow");

    private final String literal;

    private LinkTypeEnum(String literal) {
        this.literal = literal;
    }

    @Override
    public Catalog getCatalog() {
        return getSchema().getCatalog();
    }

    @Override
    public Schema getSchema() {
        return DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public String getName() {
        return "LINK_TYPE_ENUM";
    }

    @Override
    public String getLiteral() {
        return literal;
    }

    /**
     * Lookup a value of this EnumType by its literal
     */
    public static LinkTypeEnum lookupLiteral(String literal) {
        return EnumType.lookupLiteral(LinkTypeEnum.class, literal);
    }
}
