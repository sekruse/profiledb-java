package de.hpi.isg.profiledb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.hpi.isg.profiledb.store.json.MeasurementDeserializer;
import de.hpi.isg.profiledb.store.json.MeasurementSerializer;
import de.hpi.isg.profiledb.store.model.Experiment;
import de.hpi.isg.profiledb.store.model.Measurement;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO
 */
public class ProfileDB {

    private List<Class<? extends Measurement>> measurementClasses = new LinkedList<>();

    private Gson gson;

    public ProfileDB registerMeasurementClass(Class<? extends Measurement> measurementClass) {
        this.measurementClasses.add(measurementClass);
        return this;
    }

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

    public Collection<Experiment> load(InputStream inputStream) {
        Collection<Experiment> experiments = new LinkedList<>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            Gson gson = this.getGson();
            final Experiment experiment = gson.fromJson(reader, Experiment.class);
            experiments.add(experiment);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unexpectedly, UTF-8 is not supported.");
        }

        return experiments;
    }

    public void save(Collection<Experiment> experiments, OutputStream outputStream) throws IOException {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            Gson gson = this.getGson();
            for (Experiment experiment : experiments) {
                gson.toJson(experiment, writer);
                writer.newLine();
            }
            writer.flush();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unexpectedly, UTF-8 is not supported.");
        }
    }

}
