package com.person.demo.Exception;

import java.lang.Exception;

public class AppException extends Exception{
    private int status;

    public AppException(int status, String errorMessage) {
        super(errorMessage);
        this.status = status;
    }

    public AppException(String errorMessage) {
        super(errorMessage);
        this.status = 500;
    }

    public int getStatus() {
        return status;
    }
}
