package com.palomiklo.streampulse.blueprint

import com.palomiklo.streampulse.serialization.Mapper
import com.palomiklo.streampulse.util.Wrap.wrap
import java.util.UUID

@JvmRecord
data class Event(val id: String, val data: String) {
    constructor(id: UUID, data: String) : this(id.toString(), data)

    companion object {
        fun serialize(data: Event): String {
            return wrap(
                { "data: " + Mapper.mapper.writeValueAsString(data) + "\n\n" },
                "Failed to serialize event: ")
        }
    }
}
