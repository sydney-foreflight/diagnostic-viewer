package com.foreflight.server.diagnosticviewer;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

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
        // go through remaining files
    }

    private File containsFile(String name) {
        for(int i = 0; i < filesIncluded.size(); i++) {
            if(filesIncluded.get(i).getName().equalsIgnoreCase(name)) { return filesIncluded.get(i); }
        } return null;
    }

    private ArrayList<File> getDirectoryFiles(String dirName) {
        for(int i = 0; i < directories.size(); i++) {
            if(directories.get(i).getName().equalsIgnoreCase(dirName)) { return directories.get(i).getFiles(); }
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
}
