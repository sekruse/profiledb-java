package de.hpi.isg.profiledb.store.json;

import com.google.gson.*;
import de.hpi.isg.profiledb.store.model.Measurement;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 */
public class MeasurementSerializer implements JsonSerializer<Measurement> {

    @Override
    public JsonElement serialize(Measurement measurement, Type type, JsonSerializationContext jsonSerializationContext) {
        final JsonObject jsonObject = (JsonObject) jsonSerializationContext.serialize(measurement);
        jsonObject.addProperty("type", measurement.getType());
        return jsonObject;
    }
}
