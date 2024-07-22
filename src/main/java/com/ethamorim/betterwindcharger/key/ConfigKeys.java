package com.ethamorim.betterwindcharger.key;

public enum ConfigKeys {
    VELOCITY_FACTOR("velocity-factor"),
    EXPLOSION_FACTOR("explosion-factor"),
    PARTICLES("particles");

    private final String description;

    ConfigKeys(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
