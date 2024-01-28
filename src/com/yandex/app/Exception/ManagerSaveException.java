package com.yandex.app.Exception;

import java.io.IOException;

public class  ManagerSaveException extends RuntimeException {
    public ManagerSaveException(String message, Throwable cause) {
        super(message, cause);
    }

    public ManagerSaveException(String message) {
    }
}
