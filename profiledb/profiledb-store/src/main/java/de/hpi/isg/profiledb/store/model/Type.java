package de.hpi.isg.profiledb.store.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares the type ID of a {@link Measurement}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Type {

    /**
     * Declares the name of the type.
     *
     * @return the name
     */
    String value();

}
