package com.image.tech;

/**
 * Enum class to identify available alert types
 */
public enum AlertType {
    Info(1), Error(2);

    private int value;
    public int getValue() {
        return value;
    }

    private AlertType(int value) {
        this.value = value;
    }
}
