package com.purchase_agent.webapp.giraffe.objectify_entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;
/**
 * Created by lukez on 2/18/17.
 */
public class Hours {
    public enum Day {
        MON,
        TUE,
        WED,
        THR,
        FRI,
        SAT,
        SUN
    }
    private Day day;
    private DateTime start;
    private DateTime end;

    public Hours() {
    }

    @JsonProperty("day")
    public Day getDay() {
        return day;
    }

    public void setDay(final Day day) {
        this.day = day;
    }

    @JsonProperty("start")
    public DateTime getStart() {
        return start;
    }

    public void setStart(final DateTime start) {
        this.start = start;
    }

    @JsonProperty("end")
    public DateTime getEnd() {
        return end;
    }

    public void setEnd(final DateTime end) {
        this.end = end;
    }
}
