package com.example.foodbar.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.Date

object GsonUtil {
    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, JsonSerializer { src: LocalDate, _: Type?, _: JsonSerializationContext? ->
            JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE))
        })
        .registerTypeAdapter(LocalTime::class.java, JsonSerializer { src: LocalTime, _: Type?, _: JsonSerializationContext? ->
            JsonPrimitive(src.format(DateTimeFormatter.ofPattern("HH:mm:ss")))
        })
        .registerTypeAdapter(LocalDate::class.java, JsonDeserializer { json, _, _ ->
            val jsonArray = json.asJsonArray
            LocalDate.of(jsonArray.get(0).asInt, jsonArray.get(1).asInt, jsonArray.get(2).asInt)
        })
        .registerTypeAdapter(LocalTime::class.java, JsonDeserializer { json, _, _ ->
            val jsonArray = json.asJsonArray
            LocalTime.of(jsonArray.get(0).asInt, jsonArray.get(1).asInt)
        })
        // Handle Type Date with Pattern yyyy-MM-dd HH:mm:ss
        .registerTypeAdapter(Date::class.java, JsonSerializer<Date> { src, _, _ ->
            val zoneId = ZoneId.systemDefault()
            val zonedDateTime = src.toInstant().atZone(zoneId)
            val localDateTime = zonedDateTime.toLocalDateTime()
            JsonPrimitive(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")))
        })
        .registerTypeAdapter(Date::class.java, JsonDeserializer<Date> { json, _, _ ->
            val str = json.asString
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val localDateTime = LocalDateTime.parse(str, formatter)
            Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
        })
        .create()
}