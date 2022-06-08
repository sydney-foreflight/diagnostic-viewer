package com.foreflight.server.diagnosticviewer;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

public class DiagnosticInfo {

    private ArrayList<File> filesIncluded;
    private ArrayList<FileProcessor.Directory> directories;
    private ArrayList<Crash> crashes;



    public DiagnosticInfo(ArrayList<File> filesIncluded, ArrayList<FileProcessor.Directory> directories) {
        this.filesIncluded = filesIncluded;
        this.directories = directories;
        parse();
    }

    private void parse() {

        //first look through stack report files and create Crash objects if applicable
        // then go through sync_insights and masterLog to construct DataEntry objects and Buckets\
            /* step one: create un-filtered dataEntry log to ensure correct parsing
                   step two: decide how want to filter/contain dataEntries in order to streamline viewer/info
                   step three: implement changes */
                /* only allow DataEntries in arraylist if within 5 min of crash? -> ind data structures for containing
                    all dataEntries for each crash -> should create conditional if crash times close together? */
       File myFile = containsFile("sync_insights", filesIncluded);
       if(myFile != null) { ArrayList<DataEntry> syncData = syncParser(myFile); }
       myFile = containsFile("masterLog", filesIncluded);
       if(myFile != null) { ArrayList<DataEntry> masterLogData = masterLogParser(myFile); }
        // go through remaining files
    }

    private static File containsFile(String name, ArrayList<File> filesIncluded1) {
        for(int i = 0; i < filesIncluded1.size(); i++) {
            if(filesIncluded1.get(i).getName().equalsIgnoreCase(name)) { return filesIncluded1.get(i); }
        } return null;
    }

    private static ArrayList<File> getDirectoryFiles(String dirName, ArrayList<FileProcessor.Directory> directories1) {
        for(int i = 0; i < directories1.size(); i++) {
            if(directories1.get(i).getName().equalsIgnoreCase(dirName)) { return directories1.get(i).getFiles(); }
        } return null;
    }

    class Crash {
        private String hardwareModel;
        private String version;
        private Date crashTime;
        private String applicationSpecificInfo;

        public Crash(String hardwareModel, String version, Date crashTime, String applicationSpecificInfo) {
            this.hardwareModel = hardwareModel;
            this.version = version;
            this.crashTime = crashTime;
            this.applicationSpecificInfo = applicationSpecificInfo;
        }

        public String getHardwareModel() { return hardwareModel; }

        public String getVersion() { return version; }

        public Date getCrashTime() { return crashTime; }

        public String getApplicationSpecificInfo() { return applicationSpecificInfo; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Crash crash = (Crash) o;
            return hardwareModel.equals(crash.hardwareModel) && Objects.equals(version, crash.version) && Objects.equals(crashTime, crash.crashTime) && applicationSpecificInfo.equals(crash.applicationSpecificInfo);
        }
    }

    private static ArrayList<DataEntry> syncParser(File file) {
        ArrayList<DataEntry> myEntries = new ArrayList<>();
        ArrayList<String> myStrings;
        Pattern pattern = Pattern.compile("\\d+\\s\\d\\d-\\d\\d-\\d\\d\\d\\d");

        try {
            String syncInsight = new String(Files.readAllBytes(file.toPath()));
            myStrings = (ArrayList<String>)Arrays.asList(syncInsight.split("(?=\\d+\\s\\d\\d-\\d\\d-\\d\\d\\d\\d)"));
        }

        catch (Exception e){
            System.out.println("Could not read sync file. ");
            return null;
        }
        return null;
    }

    private static ArrayList<DataEntry> masterLogParser(File file) {
        ArrayList<DataEntry> myEntries = new ArrayList<>();

        try {
            String masterParser = new String(Files.readAllBytes(file.toPath()));
        }

        catch (Exception e){
            System.out.println("Could not read masterLog file. ");
            return null;
        }
        return null;
    }
}

