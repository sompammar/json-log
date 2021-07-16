package com.test.java.log;

import com.slf4.log.Person;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaLogTester {
    private static final Logger javaLogger = Logger.getLogger(JavaLogTester.class.getName());
    private static Person person = new Person("Java", "Test");

    public void testLog() {
        javaLogger.log(Level.SEVERE, "TEST Severe message {0}", person);
        javaLogger.log(Level.INFO," {1} TEST info message {0}", new Object[]{
                person, person
        });
        javaLogger.log(Level.WARNING, "TEST Warning message {0}", person);
    }
}
