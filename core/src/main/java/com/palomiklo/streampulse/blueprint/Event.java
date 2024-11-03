package com.palomiklo.streampulse.blueprint;

import static com.palomiklo.streampulse.serialization.Mapper.getMapper;

import java.util.UUID;

import static com.palomiklo.streampulse.util.Wrap.wrap;

public record Event(String id, String data) {

    public Event(String id, String data) {
        this.id = id;
        this.data = data;
    }

    public Event(UUID id, String data) {
        this(id.toString(), data);
    }

    public static String serialize(Object data) {
        return wrap(() -> "data: " + getMapper().writeValueAsString(data) + "\n\n", "Failed to serialize event: ", () -> "{}");
    }
}
