package com.suesskind.minicms.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BlogStatus {
    DRAFT("Entwurf"),
    PUBLISHED("Veröffentlicht"),
    ARCHIVED("Archiviert");

    private final String displayName;

    BlogStatus(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static BlogStatus fromJson(String value) {
        for (BlogStatus status : values()) {
            if (status.displayName.equalsIgnoreCase(value)) {
                return status;
            }
        }
        return BlogStatus.DRAFT;
    }
}
