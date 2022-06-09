package com.foreflight.server.diagnosticviewer;

import com.foreflight.server.diagnosticviewer.datastructures.DataEntry;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

public class DiagnosticInfo {

    private final ArrayList<File> filesIncluded;
    private final ArrayList<FileProcessor.Directory> directories;
    private ArrayList<Crash> crashes;


    public DiagnosticInfo(ArrayList<File> filesIncluded, ArrayList<FileProcessor.Directory> directories) {
        this.filesIncluded = filesIncluded;
        this.directories = directories;
        parse();
    }

    private void parse() {
        Parser myParser = new Parser();
        //first look through stack report files and create Crash objects if applicable
        ArrayList<FileProcessor.Directory> stackDirectories = getStackDirectories(directories);
        for(int i = 0 ; i < stackDirectories.size(); i++) {
            crashes.addAll(myParser.getStackCrashes(stackDirectories.get(i)));
        }

        // then go through sync_insights and masterLog to construct DataEntry objects and Buckets\
            /* step one: create un-filtered dataEntry log to ensure correct parsing
                   step two: decide how want to filter/contain dataEntries in order to streamline viewer/info
                   step three: implement changes */
                /* only allow DataEntries in arraylist if within 5 min of crash? -> ind data structures for containing
                    all dataEntries for each crash -> should create conditional if crash times close together? */
        File myFile = containsFile("sync_insights", filesIncluded);
        if (myFile != null) {
            ArrayList<DataEntry> syncData = myParser.syncParser(myFile);
        }
        myFile = containsFile("masterLog", filesIncluded);
        if (myFile != null) {
            ArrayList<DataEntry> masterLogData = myParser.masterLogParser(myFile);
        }
        // go through remaining files
    }

    private static ArrayList<FileProcessor.Directory> getStackDirectories(ArrayList<FileProcessor.Directory> directories1) {
        ArrayList<FileProcessor.Directory> stackDirectories = new ArrayList<>();
        for(int i = 0; i < directories1.size(); i++) {
            if(directories1.get(i).getName().contains("stack_reports")) {
                stackDirectories.add(directories1.get(i));
            }
        }
        return stackDirectories;
    }

    private static File containsFile(String name, ArrayList<File> filesIncluded1) {
        for (int i = 0; i < filesIncluded1.size(); i++) {
            if (filesIncluded1.get(i).getName().equalsIgnoreCase(name)) {
                return filesIncluded1.get(i);
            }
        }
        return null;
    }

    private static ArrayList<File> getDirectoryFiles(String dirName, ArrayList<FileProcessor.Directory> directories1) {
        for (int i = 0; i < directories1.size(); i++) {
            if (directories1.get(i).getName().equalsIgnoreCase(dirName)) {
                return directories1.get(i).getFiles();
            }
        }
        return new ArrayList<File>();
    }
}

