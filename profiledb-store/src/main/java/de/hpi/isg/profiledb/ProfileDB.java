package de.hpi.isg.profiledb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.hpi.isg.profiledb.store.json.MeasurementDeserializer;
import de.hpi.isg.profiledb.store.json.MeasurementSerializer;
import de.hpi.isg.profiledb.store.model.Experiment;
import de.hpi.isg.profiledb.store.model.Measurement;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * This class provides facilities to save and load {@link Experiment}s.
 */
public class ProfileDB {

    /**
     * Maintains a list of {@link Class}es for {@link Measurement}s. Required for deserialization.
     */
    private List<Class<? extends Measurement>> measurementClasses = new LinkedList<>();

    /**
     * Maintains a {@link Gson} object for efficiency. It will be dropped on changes, though.
     */
    private Gson gson;

    /**
     * Register a {@link Measurement} type. This is required before being able to load that type.
     *
     * @param measurementClass the {@link Measurement} {@link Class}
     * @return this instance
     */
    public ProfileDB registerMeasurementClass(Class<? extends Measurement> measurementClass) {
        this.measurementClasses.add(measurementClass);
        this.gson = null;
        return this;
    }

    /**
     * Provide a {@link Gson} object.
     *
     * @return the {@link Gson} object
     */
    private Gson getGson() {
        if (this.gson == null) {
            MeasurementSerializer measurementSerializer = new MeasurementSerializer();
            MeasurementDeserializer measurementDeserializer = new MeasurementDeserializer();
            this.measurementClasses.forEach(measurementDeserializer::register);
            this.gson = new GsonBuilder()
                    .registerTypeAdapter(Measurement.class, measurementDeserializer)
                    .registerTypeAdapter(Measurement.class, measurementSerializer)
                    .create();
        }
        return this.gson;
    }

    /**
     * Load {@link Experiment}s from a {@link File}.
     *
     * @param file the {@link File}
     * @return the {@link Experiment}s
     */
    public Collection<Experiment> load(File file) throws IOException {
        return load(new FileInputStream(file));
    }

    /**
     * Load {@link Experiment}s from an {@link InputStream}.
     *
     * @param inputStream the {@link InputStream}
     * @return the {@link Experiment}s
     */
    public Collection<Experiment> load(InputStream inputStream) throws IOException {
        try {
            return load(new BufferedReader(new InputStreamReader(inputStream, "UTF-8")));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unexpectedly, UTF-8 is not supported.");
        }
    }

    /**
     * Load {@link Experiment}s from an {@link Reader}.
     *
     * @param reader the {@link Reader}
     * @return the {@link Experiment}s
     */
    public Collection<Experiment> load(BufferedReader reader) throws IOException {
        Collection<Experiment> experiments = new LinkedList<>();
        Gson gson = this.getGson();
        String line;
        while ((line = reader.readLine()) != null) {
            Experiment experiment = gson.fromJson(line, Experiment.class);
            experiments.add(experiment);
        }
        return experiments;
    }

    /**
     * Write {@link Experiment}s to a {@link File}. Existing file contents will be overwritten.
     *
     * @param file        the {@link File}
     * @param experiments the {@link Experiment}s
     * @throws IOException if the writing fails
     */
    public void save(File file, Collection<Experiment> experiments) throws IOException {
        file.getParentFile().mkdirs();
        try (FileOutputStream fos = new FileOutputStream(file, false)) {
            this.save(experiments, fos);
        }
    }

    /**
     * Write {@link Experiment}s to a {@link File}. Existing file contents will be overwritten.
     *
     * @param file        the {@link File}
     * @param experiments the {@link Experiment}s
     * @throws IOException if the writing fails
     */
    public void save(File file, Experiment... experiments) throws IOException {
        this.save(file, Arrays.asList(experiments));
    }

    /**
     * Append {@link Experiment}s to a {@link File}. Existing file contents will be preserved.
     *
     * @param file        the {@link File}
     * @param experiments the {@link Experiment}s
     * @throws IOException if the writing fails
     */
    public void append(File file, Collection<Experiment> experiments) throws IOException {
        file.getParentFile().mkdirs();
        try (FileOutputStream fos = new FileOutputStream(file, true)) {
            this.save(experiments, fos);
        }
    }

    /**
     * Append {@link Experiment}s to a {@link File}. Existing file contents will be preserved.
     *
     * @param file        the {@link File}
     * @param experiments the {@link Experiment}s
     * @throws IOException if the writing fails
     */
    public void append(File file, Experiment... experiments) throws IOException {
        this.append(file, Arrays.asList(experiments));
    }

    /**
     * Write {@link Experiment}s to an {@link OutputStream}.
     *
     * @param outputStream the {@link OutputStream}
     */
    public void save(Collection<Experiment> experiments, OutputStream outputStream) throws IOException {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            this.save(experiments, writer);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unexpectedly, UTF-8 is not supported.");
        }
    }

    /**
     * Write {@link Experiment}s to a {@link Writer}.
     *
     * @param writer the {@link Writer}
     */
    public void save(Collection<Experiment> experiments, Writer writer) throws IOException {
        try {
            Gson gson = this.getGson();
            for (Experiment experiment : experiments) {
                gson.toJson(experiment, writer);
                writer.append('\n');
            }
            writer.flush();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unexpectedly, UTF-8 is not supported.");
        }
    }

}
