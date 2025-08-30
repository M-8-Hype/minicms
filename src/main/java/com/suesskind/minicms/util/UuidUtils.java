package com.suesskind.minicms.util;

import java.util.UUID;

public class UuidUtils {
    public static UUID generateId() {
        return UUID.randomUUID();
    }

    public static UUID parseId(String id) {
        return UUID.fromString(id);
    }
}
