package de.hpi.isg.profiledb.store.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

/**
 * An experiment comprises measurements from one specific algorithm execution.
 */
public class Experiment {

    private String id;

    private String description;

    private long startTime;

    private Collection<String> tags;

    private Collection<Measurement> measurements;

    /**
     * For deserialization.
     */
    private Experiment() { }

    public Experiment(String id, String description, String... tags) {
        this(id, description, System.currentTimeMillis(), tags);
    }

    public Experiment(String id, String description, long startTime, String... tags) {
        this.id = id;
        this.description = description;
        this.startTime = startTime;
        this.tags = Arrays.asList(tags);
        this.measurements = new LinkedList<>();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public Collection<String> getTags() {
        return tags;
    }

    public void setTags(Collection<String> tags) {
        this.tags = tags;
    }

    public void addMeasurement(Measurement measurement) {
        this.measurements.add(measurement);
    }

    public Collection<Measurement> getMeasurements() {
        return measurements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Experiment that = (Experiment) o;
        return startTime == that.startTime &&
                Objects.equals(id, that.id) &&
                Objects.equals(description, that.description) &&
                Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, startTime, tags);
    }
}
