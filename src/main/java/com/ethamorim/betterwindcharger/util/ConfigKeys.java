package com.ethamorim.betterwindcharger.util;

public enum ConfigKeys {
    VELOCITY_FACTOR("velocity-factor"),
    EXPLOSION_FACTOR("explosion-factor"),
    ANIMATION("animation");

    private final String description;

    ConfigKeys(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
