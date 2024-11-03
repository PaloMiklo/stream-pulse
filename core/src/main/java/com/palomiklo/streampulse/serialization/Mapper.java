package com.palomiklo.streampulse.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Mapper {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static ObjectMapper getMapper() {
        return mapper;
    }
}
