/* ShellCommands.java creates the commands the Spring Shell can run with.
   Author: Sydney Thompson
   Date: 06/09/22
 */


package com.foreflight.server.diagnosticviewer;

import com.foreflight.server.diagnosticviewer.datastructures.Crash;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.IOException;
import java.util.ArrayList;

@ShellComponent
public class ShellCommands {

    String zipFilePath = "";
    String saveFilePath = "";
    FileProcessor reader;
    DataAnalysis analysis;

    @ShellMethod("Set zip file directory")
    public String setZip(String s) {
        zipFilePath = s;
        return "Zip file path set to " + s;
    }

    @ShellMethod("Set save directory")
    public String setSaveDir(String s) {
        saveFilePath = s;
        return "Unzipped files will be saved to " + s;
    }

    @ShellMethod("unzip")
    public String unzip() throws IOException {
        if (zipFilePath.length() < 1) {
            return "Please set path to zip file";
        }
        if (saveFilePath.length() < 1) {
            return "Please set a path to a new directory";
        }
        reader = new FileProcessor(zipFilePath, saveFilePath);
        DiagnosticInfo myInfo = new DiagnosticInfo(reader.getFilesIncluded(), reader.getDirectoriesIncluded());
        analysis = myInfo.parse();
        return " ";
        //return reader.getAllFiles();
    }

    @ShellMethod("get crashes")
    public String getCrashes() {
        ArrayList<Crash> myCrashes = analysis.getCrashes();
        if (myCrashes.size() == 0) {
            return "Stack reports not found. No specific crashes.";
        } else {
            String s = "Crashes detected from stack reports: \n";
            for (int i = 0; i < myCrashes.size(); i++) {
                s += "\n" + myCrashes.get(i);
            }
            return s;
        }
    }
}
