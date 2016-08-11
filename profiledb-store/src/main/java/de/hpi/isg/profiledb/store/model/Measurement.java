package de.hpi.isg.profiledb.store.model;

import java.util.Objects;

/**
 * A measurement captures a specific metric at a specific time.
 */
public abstract class Measurement {

    public static String getTypeName(Class<? extends Measurement> measurementClass) {
        return measurementClass.getDeclaredAnnotation(Type.class).value();
    }

    private String id;

    /**
     * Deserialization constructor.
     */
    protected Measurement() {
    }

    public Measurement(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return getTypeName(this.getClass());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Measurement that = (Measurement) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
