package org.workshop.api.enums;

public enum BuildState {
    FINISHED("finished");

    private String value;

    BuildState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
