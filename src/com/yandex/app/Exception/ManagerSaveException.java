package com.yandex.app.Exception;

import java.io.IOException;

public class  ManagerSaveException extends RuntimeException {
    private final IOException e;
    String name;

    public ManagerSaveException(String name, IOException e) {
        this.name = name;
        this.e = e;
    }

}
