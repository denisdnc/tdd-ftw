package com.tdd.user.domains;

public enum SerasaStatus {
    PENDING_DEBIT("1"), NO_DEBIT("2");

    String value;

    SerasaStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
