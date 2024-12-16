package com.example.ServerExercise.model.responses;

public class ServerResponse
{
    private Object result;
    private String errorMessage;

    public ServerResponse() {}
    public ServerResponse(Object result) {
        this.result = result;
    }

    public ServerResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Object getResult() {
        return result;
    }
}
