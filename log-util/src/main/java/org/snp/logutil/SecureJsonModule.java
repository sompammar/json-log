package org.snp.logutil;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashSet;
import java.util.Set;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;

public class SecureJsonModule extends Module {
    private static final String moduleName = "logutil";
    private Set<String> secureKeys = new HashSet<>();

    @Override
    public String getModuleName() {
        return moduleName;
    }

    @Override
    public Version version() {
        return new Version(1, 0, 0, "SNAPSHOT", "org.snp", "logutil");
    }

    @Override
    public void setupModule(SetupContext setupContext) {
        setupContext.addBeanSerializerModifier(new EncryptedSerializerModifier(secureKeys));
        setupContext.addBeanDeserializerModifier(new EncryptedDeserializerModifier());
    }

    public SecureJsonModule(Set<String> secureKeys) {
        this.secureKeys = secureKeys;
    }

    public ObjectMapper createMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        //NONNULL
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(FAIL_ON_EMPTY_BEANS, false);
        objectMapper.registerModule(new SecureJsonModule(secureKeys));
        return objectMapper;
    }
}