package org.snp.logutil;

import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import ch.qos.logback.classic.pattern.ThrowableHandlingConverter;
import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;

public class JsonLogFormatter extends LayoutBase<ILoggingEvent> {

    private ThrowableHandlingConverter throwableProxyConverter = new ThrowableProxyConverter();
    JsonUtil jsonUtil = new JsonUtil();

    public void setSecureKeys(String secureKeys) {
        Set<String> secureKeySet = new HashSet<>();
        if (secureKeys != null) {
        }
        //convert secureKeys string to set
        this.jsonUtil = new JsonUtil(secureKeySet);
    }

    public JsonLogFormatter() {
    }

    public void start() {
        this.throwableProxyConverter.start();
        super.start();
    }

    public void stop() {
        super.stop();
        this.throwableProxyConverter.stop();
    }

    public String doLayout(ILoggingEvent event) {
        Map map = this.toMap(event);
        if (map != null && !map.isEmpty()) {
            String jsonString = this.toJson(map);
            return jsonString + CoreConstants.LINE_SEPARATOR;
        } else {
            return null;
        }
    }

    protected Map toMap(ILoggingEvent event) {
        Map<String, Object> map = new LinkedHashMap();
        addTimestamp("timestamp", event.getTimeStamp(), map);
        add("levelname", String.valueOf(event.getLevel()), map);
        add("threadName", event.getThreadName(), map);
        addMap("mdc", event.getMDCPropertyMap(), map);
        add("logger", event.getLoggerName(), map);
        add("message", jsonUtil.getFormattedMessage(event.getMessage(), event.getArgumentArray())
                , map);
        add("context", event.getLoggerContextVO().getName(), map);
        addThrowableInfo("exc_info", event, map);
        return map;
    }

    public void addMap(String key, Map<String, ?> mapValue, Map<String, Object> map) {
        if (mapValue != null && !mapValue.isEmpty()) {
            map.put(key, mapValue);
        }

    }

    public void addTimestamp(String key, long timeStamp, Map<String, Object> map) {
        map.put(key, Instant.ofEpochMilli(timeStamp).toString());
    }

    public void add(String fieldName, String value, Map<String, Object> map) {
        if (value != null) {
            map.put(fieldName, value);
        }

    }

    protected void addThrowableInfo(String fieldName, ILoggingEvent value, Map<String, Object> map) {
        if (value != null) {
            IThrowableProxy throwableProxy = value.getThrowableProxy();
            if (throwableProxy != null) {
                String ex = this.throwableProxyConverter.convert(value);
                if (ex != null && !ex.equals("")) {
                    map.put(fieldName, ex);
                }
            }
        }

    }

    private String toJson(Map map) {
        try {
            return jsonUtil.toJson(map);
        } catch (Exception var3) {
            System.err.println("Error occurred when converting map to Json String " + map);
            return map.toString();
        }
    }
}

