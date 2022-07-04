package ru.javawebinar.topjava.util.exception;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ErrorInfo {
    private String url;
    private ErrorType type;
    private List<String> details;

    public ErrorInfo() {
    }

    public ErrorInfo(CharSequence url, ErrorType type, List<String> details) {
        this.url = url.toString();
        this.type = type;
        this.details = details;
    }

    // Deserializer from JSON - works only in test MealRestControllerTest -> createWithDoublicateDateTime()
    @JsonSetter("details" )
    public void setDetails(String detail) {
        ArrayList<String> details = new ArrayList<>();
        details.add(detail);
        this.details = details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorInfo errorInfo = (ErrorInfo) o;
        return Objects.equals(url, errorInfo.url) && type == errorInfo.type && Objects.equals(details, errorInfo.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, type, details);
    }
}