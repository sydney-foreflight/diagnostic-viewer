package com.foreflight.server.diagnosticviewer;

import com.foreflight.server.diagnosticviewer.datastructures.DataEntry;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

public class Parser {

    public ArrayList<Crash> getStackCrashes(FileProcessor.Directory stackDirect) {
        ArrayList<Crash> stackCrashes = new ArrayList<>();
        //code for parsing + formatting crashes
        return stackCrashes;
    }

    public ArrayList<DataEntry> syncParser(File file) {
        ArrayList<DataEntry> myEntries = new ArrayList<>();
        try {
            BufferedReader read = Files.newBufferedReader(file.toPath());
            String line;
            while ((line = read.readLine()) != null) {
                //code for parsing
            }
        } catch (Exception e) {
            System.out.println("could not read sync file");
        }
        return null;
    }

    public ArrayList<DataEntry> masterLogParser(File file) {
        ArrayList<DataEntry> myEntries = new ArrayList<>();

        try {
            BufferedReader read = Files.newBufferedReader(file.toPath());
            String line;
            while ((line = read.readLine()) != null) {
                //code for parsing
            }
        } catch (Exception e) {
            System.out.println("Could not read masterLog file. ");
            return null;
        }
        return null;
    }
}
