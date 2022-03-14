package com.avdienko.storeum.util;

public class MessageFormatters {

    public static String userNotFound(Long id) {
        return String.format("User with id=%s was not found in DB", id);
    }

    public static String folderNotFound(Long id) {
        return String.format("Folder with id=%s was not found in DB", id);
    }

    public static String noteNotFound(Long id) {
        return String.format("Note with id=%s was not found in DB", id);
    }
}
