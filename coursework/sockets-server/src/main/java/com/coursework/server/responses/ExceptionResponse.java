package com.coursework.server.responses;

public class ExceptionResponse {
    private String message;

    public ExceptionResponse(String message){
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }

    public void setMessage(String message){
        this.message = message;
    }
}
