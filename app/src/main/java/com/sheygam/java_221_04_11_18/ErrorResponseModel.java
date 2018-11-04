package com.sheygam.java_221_04_11_18;

public class ErrorResponseModel {
    private String code;
    private String message;

    public ErrorResponseModel() {
    }

    public ErrorResponseModel(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorResponseModel{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
