package com.ethamorim.pluginenxtest.key;

public enum PowerWindCharge {

    DEFAULT("default", 0.0f),
    MEDIUM("medium", 0.05f),
    HIGH("high", 0.2f),
    HUGE("huge", 1f);

    private final String description;
    private final float value;

    PowerWindCharge(String description, float value) {
        this.description = description;
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    @Override
    public String toString() {
        return description;
    }
}
