package de.hpi.isg.profiledb.store.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

/**
 * An experiment comprises measurements from one specific algorithm execution.
 */
public class Experiment {

    /**
     * Should identify this instance among others.
     */
    private String id;

    /**
     * Optional description of this very instance.
     */
    private String description;

    /**
     * When this experiment has been started.
     */
    private long startTime;

    /**
     * Optional tags to group multiple instances.
     */
    private Collection<String> tags;

    /**
     * {@link Measurement}s captured for this instance.
     */
    private Collection<Measurement> measurements;

    /**
     * The {@link Subject} being experimented with.
     */
    private Subject subject;

    /**
     * For deserialization.
     */
    private Experiment() {
    }

    /**
     * Create a new instance that is starting right now.
     *
     * @param id      an ID for the new instance
     * @param subject the {@link Subject}
     * @param tags    tags to group several experiments
     */
    public Experiment(String id, Subject subject, String... tags) {
        this(id, subject, System.currentTimeMillis(), tags);
    }

    /**
     * Create a new instance.
     *
     * @param id        an ID for the new instance
     * @param subject   the {@link Subject}
     * @param startTime start timestamp of experiment
     * @param tags      tags to group several experiments
     */
    public Experiment(String id, Subject subject, long startTime, String... tags) {
        this.id = id;
        this.subject = subject;
        this.startTime = startTime;
        this.tags = Arrays.asList(tags);
        this.measurements = new LinkedList<>();
    }

    /**
     * Adds a description for this instance.
     *
     * @param description the description
     * @return this instance
     */
    public Experiment withDescription(String description) {
        this.description = description;
        return this;
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
