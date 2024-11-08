package com.palomiklo.streampulse.blueprint

import com.palomiklo.streampulse.serialization.Mapper
import com.palomiklo.streampulse.util.Wrap.wrap
import java.util.*

data class Event(val id: String, val data: String) {
    constructor(id: UUID, data: String) : this(id.toString(), data)

    companion object {
        fun serialize(data: Event): Result<String> =
            wrap(supplier = { "data: " + Mapper.mapper.writeValueAsString(data) + "\n\n" }, message = "Failed to serialize event")
    }
}
