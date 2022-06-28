package ru.javawebinar.topjava.util.exception;

import java.util.Objects;

public class ErrorInfo {
    private String url;
    private ErrorType type;
    private String detail;

    public ErrorInfo() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorInfo errorInfo = (ErrorInfo) o;
        return url.equals(errorInfo.url) && type == errorInfo.type && detail.equals(errorInfo.detail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, type, detail);
    }

    public ErrorInfo(CharSequence url, ErrorType type, String detail) {
        this.url = url.toString();
        this.type = type;
        this.detail = detail;
    }
}