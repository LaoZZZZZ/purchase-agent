package com.purchase_agent.webapp.giraffe.greetings;

import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class GreetingService {
    public Iterable<Greeting> getEnglishGreetings() {
        return Arrays.asList(
            new Greeting("Hi"),
            new Greeting("Hello"),
            new Greeting("Yo"));
    }
}

