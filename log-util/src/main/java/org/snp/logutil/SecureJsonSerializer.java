package org.snp.logutil;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.StringWriter;

public class SecureJsonSerializer extends JsonSerializer<Object> {

    /**
     * Default serialization tool object
     */
    private final JsonSerializer<Object> serializer;

    public SecureJsonSerializer(JsonSerializer<Object> serializer) {
        this.serializer = serializer;
    }

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        StringWriter stringWriter = new StringWriter();
        ObjectCodec objectCodec = jsonGenerator.getCodec();
        JsonGenerator nestedGenerator = null;

        // Empty objects or empty strings are not processed.
        if(o == null || StringUtils.isBlank(String.valueOf(o))){
            if (serializer == null) {
                serializerProvider.defaultSerializeValue(o, jsonGenerator);
            }else{
                serializer.serialize(o, jsonGenerator, serializerProvider);
            }

            return;
        }

        /*
                         Generate a new JsonGenerator for writing o.
         */
        if(objectCodec instanceof ObjectMapper){
            nestedGenerator = ((ObjectMapper) objectCodec).getFactory().createGenerator(stringWriter);
        }

        if (nestedGenerator == null) {
            throw new NullPointerException("nestedGenerator == null");
        }

        /*
                         Write data to the newly generated JsonGenerator
         */
        if (serializer == null) {
            serializerProvider.defaultSerializeValue(o, nestedGenerator);
        }else{
            serializer.serialize(o, nestedGenerator, serializerProvider);
        }

        nestedGenerator.close();
        /*
                         JsonGenerator will generate a string with double quotes, encrypt the data and write it.
         */
        String value = stringWriter.getBuffer().toString();
        try {
            // empty string is not encrypted
//            jsonGenerator.writeString(AESTools.encrypt(value));
            jsonGenerator.writeString("[***MASKED**]");
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}

