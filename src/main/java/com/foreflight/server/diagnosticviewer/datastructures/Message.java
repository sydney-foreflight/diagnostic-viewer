/**
 * Message.java hold the specifics of each diagnostic entry. It takes the String and performs more analysis as needed.
 * It is an immutable data organization class.
 */

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
