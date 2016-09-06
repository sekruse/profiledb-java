package de.hpi.isg.profiledb.store.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import de.hpi.isg.profiledb.store.model.Measurement;

import java.lang.reflect.Type;

/**
 * Custom serializer for {@link Measurement}s that detects the actual subclass of given instances, encodes this
 * class memebership, and then delegates serialization to that subtype.
 */
public class MeasurementSerializer implements JsonSerializer<Measurement> {

    @Override
    public JsonElement serialize(Measurement measurement, Type type, JsonSerializationContext jsonSerializationContext) {
        final JsonObject jsonObject = (JsonObject) jsonSerializationContext.serialize(measurement);
        jsonObject.addProperty("type", measurement.getType());
        return jsonObject;
    }
}
