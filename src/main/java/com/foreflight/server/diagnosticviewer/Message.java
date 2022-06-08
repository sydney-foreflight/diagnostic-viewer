package com.foreflight.server.diagnosticviewer;

public class Message {

    private String message;

    public Message(String message) {
        this.message = message;
    }

    public String getFullMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }

}
