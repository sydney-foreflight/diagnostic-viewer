package com.foreflight.server.diagnosticviewer;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class ShellCommands {

    @ShellMethod("test")
    public String test() {
        return "yay!";
    }
}
