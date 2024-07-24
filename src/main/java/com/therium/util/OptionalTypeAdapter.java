package com.therium.util;

import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;

public class OptionalTypeAdapter<T> extends TypeAdapter<Optional<T>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(OptionalTypeAdapter.class);
    private final TypeAdapter<T> delegate;

    public OptionalTypeAdapter(TypeAdapter<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void write(JsonWriter out, Optional<T> value) throws IOException {
        if (value.isPresent()) {
            LOGGER.info("Writing value: {}", value.get());
            delegate.write(out, value.get());
        } else {
            LOGGER.info("Writing null value for empty Optional");
            out.nullValue();
        }
    }

    @Override
    public Optional<T> read(JsonReader in) throws IOException {
        LOGGER.info("Reading value...");
        T value = delegate.read(in);
        if (value == null) {
            LOGGER.info("Read null value, returning Optional.empty()");
        } else {
            LOGGER.info("Read value: {}, wrapping in Optional", value);
        }
        return Optional.ofNullable(value);
    }

    public static TypeAdapterFactory optionalFactory() {
        return new TypeAdapterFactory() {
            @Override
            @SuppressWarnings("unchecked")
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
                if (!Optional.class.isAssignableFrom(type.getRawType())) {
                    return null;
                }
                TypeAdapter<?> delegate = gson.getAdapter(TypeToken.get(((ParameterizedType) type.getType()).getActualTypeArguments()[0]));
                LOGGER.info("Creating OptionalTypeAdapter for type: {}", type.getType());
                return (TypeAdapter<T>) new OptionalTypeAdapter<>(delegate);
            }
        };
    }
}
