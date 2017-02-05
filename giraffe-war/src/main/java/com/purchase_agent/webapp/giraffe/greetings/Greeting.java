package com.purchase_agent.webapp.giraffe.greetings;

import com.fasterxml.jackson.annotation.JsonValue;

public class Greeting {
    private final String text;

    public Greeting(String text) {
        this.text = text;
    }

    @JsonValue
    public String getText() {
        return text;
    }
}
