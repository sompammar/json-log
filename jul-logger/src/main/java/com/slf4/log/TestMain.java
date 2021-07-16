package com.slf4.log;

import com.test.java.log.JavaLogTester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.LogManager;

public class TestMain {
    static {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.install();
    }

    public static void main(String[] args) {
        Person person = new Person("Jane", "Doe");
        Logger logger = LoggerFactory.getLogger(TestMain.class);
        logger.info("Starting test main {}", person);
        logger.info("Logging jsessionId {}", "abc234");
        JavaLogTester tester = new JavaLogTester();
        tester.testLog();
    }
}
