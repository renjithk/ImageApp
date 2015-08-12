package com.image.tech.exception;

/**
 * This class defines all possible exception to be handled
 */
public class AppException extends Exception {
    public static final int READABLE_STREAM_ERROR = 114;
    public static final int MALFORMED_URL = 120;
    public static final int CONNECTION_OPEN_FAIL = 121;
    public static final int ERROR_HTTP_PROTOCOL = 122;
    public static final int INPUT_STREAM_OPEN_FAILURE = 124;
    public static final int JSON_OBJECT_CREATE_ERROR = 117;
    public static final int EMPTY_JSON = 118;
    public static final int JSON_PARSE_ERROR = 119;
    public static final int STREAM_CLOSE_FAIL = 128;

    private int id;

    public AppException(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
