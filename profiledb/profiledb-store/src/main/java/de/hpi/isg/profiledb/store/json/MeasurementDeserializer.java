package de.hpi.isg.profiledb.store.json;

import com.google.gson.*;
import de.hpi.isg.profiledb.store.model.Measurement;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO
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
