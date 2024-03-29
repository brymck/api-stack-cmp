package com.github.brymck.greetings.infrastructure

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.google.gson.stream.JsonToken.NULL
import java.io.IOException
import java.util.UUID

class UUIDAdapter : TypeAdapter<UUID>() {
    @Throws(IOException::class)
    override fun write(out: JsonWriter?, value: UUID?) {
        if (value == null) {
            out?.nullValue()
        } else {
            out?.value(value.toString())
        }
    }

    @Throws(IOException::class)
    override fun read(out: JsonReader?): UUID? {
        out ?: return null

        when (out.peek()) {
            NULL -> {
                out.nextNull()
                return null
            }
            else -> {
                return UUID.fromString(out.nextString())
            }
        }
    }
}
