package ru.sfedu.projectpuskaFinalVersion;

public class Constants {

    public static final String CFG_KEY = "cfg";
    public static final String CFG_DEFAULT_VALUE = "src/main/resources/config.properties";
    public static final String PATH_TO_NATIVE_LIB_WINDOWS = "ru.sfedu.cv.PathToNativeLibs.Windows";
    public static final String PATH_TO_NATIVE_LIB_LINUX = "ru.sfedu.cv.PathToNativeLibs.Linux";
    public static final String PATH_TO_NATIVE_LIB_MAC_OS = "ru.sfedu.cv.PathToNativeLibs.MacOS";
    public static final String PATH_TO_NATIVE_LIB_OTHER = "ru.sfedu.cv.PathToNativeLibs.Other";

    public enum OSType {
        WINDOWS,
        MACOS,
        LINUX,
        OTHER
    }
}
