package com.dailystudio.devbricksx.compiler.processor;

public class GeneratedNames {

    private final static String ROOM_COMPANION_PREFIX = "_";
    private final static String ROOM_COMPANION_DB_SUFFIX = "Database";
    private final static String ROOM_COMPANION_DAO_SUFFIX = "Dao";

    public static String getRoomCompanionName(String className) {
        StringBuilder builder = new StringBuilder(className);

        builder.insert(0, ROOM_COMPANION_PREFIX);

        return builder.toString();
    }

    public static String getRoomCompanionDaoName(String className) {
        StringBuilder builder = new StringBuilder(className);

        builder.append(ROOM_COMPANION_DAO_SUFFIX);
        builder.insert(0, ROOM_COMPANION_PREFIX);

        return builder.toString();
    }

    public static String getRoomCompanionDaoWrapperName(String className) {
        StringBuilder builder = new StringBuilder(className);

        builder.append(ROOM_COMPANION_DAO_SUFFIX);

        return builder.toString();
    }

    public static String getRoomCompanionDaoWrapperInnerClassName(String className) {
        StringBuilder builder = new StringBuilder(getRoomCompanionDatabaseName(className));

        builder.append(".");
        builder.append(getRoomCompanionDaoWrapperName(className));

        return builder.toString();
    }

    public static String getRoomCompanionDatabaseName(String className) {
        StringBuilder builder = new StringBuilder(className);

        builder.append(ROOM_COMPANION_DB_SUFFIX);

        return builder.toString();
    }

}
