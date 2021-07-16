package com.slf4.log;

public class Person {
    String name;
    String lastName;

    public Person(){

    }

    public Person(String name, String lastName) {
        this.lastName = lastName;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
