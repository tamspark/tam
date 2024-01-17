package com.sparklab.TAM.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class DecimalJsonSerializer extends JsonSerializer<Double> {

     private static final DecimalFormat formatter = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));
    @Override
    public void serialize(Double value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {
        String formattedValue = formatter.format(value);
        jgen.writeNumber(formattedValue);
    }
}
