package org.snp.logutil;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EncryptedSerializerModifier extends BeanSerializerModifier {
    Set<String> patterns = new HashSet<>();
    Set<String> keys = new HashSet<>();
    public EncryptedSerializerModifier(Set<String> secureKeys) {
        this.keys = secureKeys;
        patterns.add("jsessionid");
    }

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {

        List<BeanPropertyWriter> newWriter = new ArrayList<>();
        for(BeanPropertyWriter writer : beanProperties){
            if(keys.contains(writer.getName())) {
                JsonSerializer<Object> serializer = new SecureJsonSerializer(writer.getSerializer());
                writer.assignSerializer(serializer);
            }
            newWriter.add(writer);
        }
        return newWriter;
    }

    private boolean containsPattern(String text) {
        String lowercase = text.toLowerCase();
        for (String pattern : patterns) {
            if (lowercase.contains(pattern)) {
                return true;
            }
        }
        return false;
    }
}
