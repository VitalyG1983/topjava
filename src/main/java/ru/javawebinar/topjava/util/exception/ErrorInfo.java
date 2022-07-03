package ru.javawebinar.topjava.util.exception;

import java.util.Objects;

public class ErrorInfo {
    private String url;
    private ErrorType type;
    private String detail;

    public ErrorInfo() {
    }

    public ErrorInfo(CharSequence url, ErrorType type, String detail) {
        this.url = url.toString();
        this.type = type;
        this.detail = detail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorInfo errorInfo = (ErrorInfo) o;
        return Objects.equals(url, errorInfo.url) && type == errorInfo.type && Objects.equals(detail, errorInfo.detail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, type, detail);
    }
}