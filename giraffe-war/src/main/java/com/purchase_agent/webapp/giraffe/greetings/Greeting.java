package com.purchase_agent.webapp.giraffe.greetings;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Greeting {
    private String text;

    public Greeting() {
    }

    public Greeting(String text) {
        this.text = text;
    }

    @JsonProperty("greeting")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
