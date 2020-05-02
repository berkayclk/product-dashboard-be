package com.paydaybank.dashboard.exception;

public enum ExceptionType {
    MISSING_PARAMETER("missing.parameter"),
    NOT_FOUND("not.found"),
    DUPLICATE("duplicate"),
    GENERIC_EXCEPTION("exception");

    String value;

    ExceptionType(String value) {
        this.value = value;
    }

    String getValue() {
        return this.value;
    }
}
