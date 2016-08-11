package de.hpi.isg.profiledb;

import de.hpi.isg.profiledb.store.model.Experiment;
import de.hpi.isg.profiledb.store.model.Measurement;
import de.hpi.isg.profiledb.store.model.test.TestMemoryMeasurement;
import de.hpi.isg.profiledb.store.model.test.TestTimeMeasurement;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Test suite for the {@link ProfileDB}.
 */
public class ProfileDBTest {

    @Test
    public void testPolymorphSaveAndLoad() throws IOException {

        ProfileDB profileDB = new ProfileDB()
                .registerMeasurementClass(TestMemoryMeasurement.class)
                .registerMeasurementClass(TestTimeMeasurement.class);

        // Create an example experiment.
        final Experiment experiment = new Experiment("test-xp", "test experiment");
        Measurement timeMeasurement = new TestTimeMeasurement("exec-time", 12345L);
        Measurement memoryMeasurement = new TestMemoryMeasurement("exec-time", System.currentTimeMillis(), 54321L);
        experiment.addMeasurement(timeMeasurement);
        experiment.addMeasurement(memoryMeasurement);

        // Save the experiment.
        byte[] buffer;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        profileDB.save(Collections.singleton(experiment), bos);
        bos.close();
        buffer = bos.toByteArray();
        System.out.println("Buffer contents: " + new String(buffer, "UTF-8"));

        // Load the experiment.
        ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
        Collection<Experiment> loadedExperiments = profileDB.load(bis);

        // Compare the experiments.
        Assert.assertEquals(1, loadedExperiments.size());
        Experiment loadedExperiment = loadedExperiments.iterator().next();
        Assert.assertEquals(experiment, loadedExperiment);

        // Compare the measurements.
        Assert.assertEquals(2, loadedExperiment.getMeasurements().size());
        Set<Measurement> expectedMeasurements = new HashSet<>(2);
        expectedMeasurements.add(timeMeasurement);
        expectedMeasurements.add(memoryMeasurement);
        Set<Measurement> loadedMeasurements = new HashSet<>(loadedExperiment.getMeasurements());
        Assert.assertEquals(expectedMeasurements, loadedMeasurements);
    }

    @Test
    public void testRecursiveSaveAndLoad() throws IOException {

        ProfileDB profileDB = new ProfileDB()
                .registerMeasurementClass(TestMemoryMeasurement.class)
                .registerMeasurementClass(TestTimeMeasurement.class);

        // Create an example experiment.
        final Experiment experiment = new Experiment("test-xp", "test experiment");
        TestTimeMeasurement topLevelMeasurement = new TestTimeMeasurement("exec-time", 12345L);
        TestTimeMeasurement childMeasurement = new TestTimeMeasurement("sub-exec-time", 2345L);
        topLevelMeasurement.addSubmeasurements(childMeasurement);
        experiment.addMeasurement(topLevelMeasurement);

        // Save the experiment.
        byte[] buffer;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        profileDB.save(Collections.singleton(experiment), bos);
        bos.close();
        buffer = bos.toByteArray();
        System.out.println("Buffer contents: " + new String(buffer, "UTF-8"));

        // Load the experiment.
        ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
        Collection<Experiment> loadedExperiments = profileDB.load(bis);

        // Compare the experiments.
        Assert.assertEquals(1, loadedExperiments.size());
        Experiment loadedExperiment = loadedExperiments.iterator().next();
        Assert.assertEquals(experiment, loadedExperiment);

        // Compare the measurements.
        Assert.assertEquals(1, loadedExperiment.getMeasurements().size());
        final Measurement loadedMeasurement = loadedExperiment.getMeasurements().iterator().next();
        Assert.assertEquals(topLevelMeasurement, loadedMeasurement);
    }

}
