package com.image.tech.signatures;

import com.image.tech.AlertType;

/**
 * Interface that links an external component to an activity
 */
public interface UIHandler {
    void onUIRender(Object data);
    void showAlert(AlertType alertType, int message);
}
