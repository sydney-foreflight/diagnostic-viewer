/* ShellCommands.java creates the commands the Spring Shell can run with.
   Author: Sydney Thompson
   Date: 06/09/22
 */


package com.foreflight.server.diagnosticviewer;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.IOException;

@ShellComponent
public class ShellCommands {

    String zipFilePath = "";
    String saveFilePath = "";
    FileProcessor reader;

    @ShellMethod("Set zip file directory")
    public String setZip(String s) {
        zipFilePath = s;
        return "Zip file path set to " + s;
    }

    @ShellMethod("Set save directory")
    public String setNewDir(String s) {
        saveFilePath = s;
        return "Unzipped files will be saved to " + s;
    }

    @ShellMethod("unzip")
    public String unzip() throws IOException {
        if(zipFilePath.length() < 1) {
            return "Please set path to zip file";
        } if(saveFilePath.length() < 1) {
            return "Please set a path to a new directory";
        } reader = new FileProcessor(zipFilePath, saveFilePath);
        return reader.getAllFiles();
    }
}
