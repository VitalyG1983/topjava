package ru.javawebinar.topjava.util.exception;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.Arrays;
import java.util.Objects;

public class ErrorInfo {
    private String url;
    private ErrorType type;
    private String[] details;

    public ErrorInfo() {
    }

    public ErrorInfo(CharSequence url, ErrorType type, String[] details) {
        this.url = url.toString();
        this.type = type;
        this.details = details;
    }

    // Deserializer from JSON - works only in test MealRestControllerTest -> createWithDoublicateDateTime()
    @JsonSetter("details")
    public void setDetailsFromJson(String detail) {
        this.details = new String[]{detail};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorInfo errorInfo = (ErrorInfo) o;
        return url.equals(errorInfo.url) && type == errorInfo.type && Arrays.equals(details, errorInfo.details);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(url, type);
        result = 31 * result + Arrays.hashCode(details);
        return result;
    }
}