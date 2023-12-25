package com.yandex.app.Manager;

import java.io.IOException;

public class  ManagerSaveException extends RuntimeException {
    public ManagerSaveException(String name, IOException e) {
    }
}
