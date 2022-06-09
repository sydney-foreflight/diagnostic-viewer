package com.foreflight.server.diagnosticviewer.datastructures;

public class Message {

    private final String message;

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
