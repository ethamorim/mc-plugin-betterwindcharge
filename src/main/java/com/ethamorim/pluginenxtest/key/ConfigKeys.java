package com.ethamorim.pluginenxtest.key;

public enum ConfigKeys {
    VELOCITY_FACTOR("velocity-factor"),
    EXPLOSION_FACTOR("explosion-factor"),
    TRAILING_PARTICLES("trailing-particles");

    private final String description;

    ConfigKeys(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
