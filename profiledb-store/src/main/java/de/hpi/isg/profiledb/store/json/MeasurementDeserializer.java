package de.hpi.isg.profiledb.store.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import de.hpi.isg.profiledb.store.model.Measurement;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom deserializer for {@link Measurement}s that detects the actual subclass of the serialized instances and
 * then delegates the deserialization to that subtype.
 */
public class MeasurementDeserializer implements JsonDeserializer<Measurement> {

    private final Map<String, Class<? extends Measurement>> measurementTypes = new HashMap<>();

    public void register(Class<? extends Measurement> measurementClass) {
        String typeName = Measurement.getTypeName(measurementClass);
        this.measurementTypes.put(typeName, measurementClass);
    }

    @Override
    public Measurement deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final JsonElement typeElement = jsonElement.getAsJsonObject().get("type");
        if (typeElement == null) {
            throw new IllegalArgumentException("Missing type in " + jsonElement);
        }
        final String typeName = typeElement.getAsString();
        final Class<? extends Measurement> measurementClass = this.measurementTypes.get(typeName);
        if (measurementClass == null) {
            throw new JsonParseException("Unknown measurement type: " + typeName);
        }
        return jsonDeserializationContext.deserialize(jsonElement, measurementClass);
    }

}
