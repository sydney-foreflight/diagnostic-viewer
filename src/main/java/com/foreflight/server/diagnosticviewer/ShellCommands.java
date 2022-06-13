/** ShellCommands.java creates the commands the Spring Shell can run with.
 */


package com.foreflight.server.diagnosticviewer;

import com.foreflight.server.diagnosticviewer.datastructures.BucketData;
import com.foreflight.server.diagnosticviewer.datastructures.Crash;
import com.foreflight.server.diagnosticviewer.datastructures.DataEntry;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

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
        return reader.getAllFiles();
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

    @ShellMethod("Get timelines")
    public String timelines(int a, int b) {
        ArrayList<Set<DataEntry>> myEntries = analysis.getEntriesAroundCrashes(a, b);
        String s = "";
        for(int i = 0; i < myEntries.size(); i++) {
            s += "Crash " + i + ": \n" + analysis.getCrashes().get(i) + "\nDiagnostic Entries:\n";
            Iterator read = myEntries.get(i).iterator();
            while(read.hasNext()) {
                s += read.next() + "\n";
            } s += "------------------------\n";
        } return s;
    }

    @ShellMethod("Getting flagged buckets")
    public String flagBuckets() {
        String s = "";
        ArrayList<BucketData> data = analysis.getFlaggedBuckets();
        if(data.size() == 0) {
            return "No buckets had any pending changes.\n";
        } for(int i = 0; i < data.size(); i++) {
            s += "\n" + data.get(i) + "\n-------------";
        } return s;
    }

    @ShellMethod("Getting masterLog error messages")
    public String getErrors() {
        Set<DataEntry> errorEntries = analysis.getErrorMessages();
        if(errorEntries.size() == 0) {
            return "No error messages in masterLog\n";
        } String s = "";
        Iterator read = errorEntries.iterator();
        while(read.hasNext()) {
            s += "\n" + read.next() + "\n";
        } return s;
    }
}
