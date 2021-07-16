package org.snp.logutil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.helpers.MessageFormatter;

import java.util.*;

public class JsonUtil {

    Set<String> secureKeys = new HashSet<>();
    SecureJsonModule secureJsonModule = null;
    Set<String> patterns = new HashSet<>();

    public JsonUtil(Set<String> secureKeys) {
        this.secureKeys = secureKeys;

//        this.secureKeys = secureKeys.addAll(Arrays.asList(csvKeys.split(",")));
        secureJsonModule = new SecureJsonModule(secureKeys);
    }

    public JsonUtil(){}

    public String maskString(String input) {
        if (input == null || input.length() == 0) {
            return input;
        }
        return "**masked**";
    }

    public String getFormattedMessage(String message, Object[] args) {
        Object[] jsonObjArray = null;
        if (args != null) {
            jsonObjArray = new Object[args.length];
            for (int i=0 ; i < args.length; ++i) {
                jsonObjArray[i] = toJson(args[i]);
            }
        }
        return MessageFormatter.arrayFormat(message, jsonObjArray).getMessage();
    }

    public String toJson(Object obj) {
        ObjectMapper objectMapper = secureJsonModule.createMapper();

        try {

            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            Map<String, Object> failed = new HashMap<>();
            failed.put("error", "Failed to serialize as json string");
            failed.put("obj", obj);
            try {
                return objectMapper.writeValueAsString(failed);
            } catch (JsonProcessingException jsonProcessingException) {
                return "ERROR";
            }
        }
    }
}
