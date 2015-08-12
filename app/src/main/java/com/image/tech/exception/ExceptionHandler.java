package com.image.tech.exception;

import com.image.tech.R;


public class ExceptionHandler<T> {
    private T result;
    private AppException exception;

    public ExceptionHandler(T result) {
        this.result = result;
    }

    public ExceptionHandler(AppException exception) {
        this.exception = exception;
        exception.printStackTrace();
    }

    /**
     * Returns the result
     * @return set generic type
     */
    public T getResult() {
        return result;
    }

    /**
     * Return error details
     * @return exception instance
     */
    public Exception getError() {
        return exception;
    }

    /**
     * Scans exception and returns type of exception
     * @return an integer representing exception
     */
    public int getErrorType() {
        switch(exception.getId()) {
            case AppException.READABLE_STREAM_ERROR: {
                return R.string.message_readable_stream_error;
            }
            case AppException.MALFORMED_URL: {
                return R.string.message_malformed_url;
            }
            case AppException.CONNECTION_OPEN_FAIL: {
                return R.string.message_connection_open_fail;
            }
            case AppException.ERROR_HTTP_PROTOCOL: {
                return R.string.message_error_http_protocol;
            }
            case AppException.INPUT_STREAM_OPEN_FAILURE: {
                return R.string.message_input_stream_open_failure;
            }
            case AppException.JSON_OBJECT_CREATE_ERROR: {
                return R.string.message_json_object_create_error;
            }
            case AppException.EMPTY_JSON: {
                return R.string.message_empty_json;
            }
            case AppException.JSON_PARSE_ERROR: {
                return R.string.message_json_parse_error;
            }
            case AppException.STREAM_CLOSE_FAIL: {
                return R.string.message_stream_close_fail;
            }
        }
        return R.string.message_error_unknown;
    }
}
