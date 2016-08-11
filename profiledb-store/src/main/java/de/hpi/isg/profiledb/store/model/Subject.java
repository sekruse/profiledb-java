package de.hpi.isg.profiledb.store.model;

import java.util.HashMap;
import java.util.Map;

/**
 * The subject of an {@link Experiment}, e.g., an application or algorithm.
 */
public class Subject {

    /**
     * Should identify the subject.
     */
    private String id;

    /**
     * The optional version of this subject.
     */
    private String version;

    /**
     * Configuration of this object.
     */
    private Map<String, Object> configuration = new HashMap<>();

    /**
     * Creates a new instance.
     *
     * @param id      should identify the new instance
     * @param version should distinguish versions among instances with the same {@code id}
     */
    public Subject(String id, String version) {
        this.id = id;
        this.version = version;
    }

    /**
     * Adds a configuration entry.
     *
     * @param key   the key of the configuration entry
     * @param value the value of the configuration entry; should be JSON-compatible, e.g. {@link Integer} or {@link String}
     * @return this instance
     */
    public Subject addConfiguration(String key, Object value) {
        this.configuration.put(key, value);
        return this;
    }
}
