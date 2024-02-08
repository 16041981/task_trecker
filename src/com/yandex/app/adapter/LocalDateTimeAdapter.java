package com.yandex.app.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter formatterWriter = DateTimeFormatter.ISO_DATE_TIME; //DateTimeFormatter.ofPattern("dd-MM-yyyy");//2024-02-08T18:23:58.097207
    private static final DateTimeFormatter formatterReader = DateTimeFormatter.ISO_DATE_TIME; //DateTimeFormatter.ofPattern("dd.MM.yyyy");

//    @Override
//    public void write(final JsonWriter jsonWriter, final LocalDate localDate) throws IOException {
//        jsonWriter.value(localDate.format(formatterWriter));
//    }



    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString(), formatterReader);
    }

    @Override
    public void write(final JsonWriter jsonWriter, LocalDateTime value) throws IOException {
        jsonWriter.value(LocalDateTime.now().format(formatterWriter));
    }
}