/**
 * DiagnosticInfo.java holds the main information from diagnostic reports. It directs the parsing of files and subsequent
 * data analysis.
 */


package com.foreflight.server.diagnosticviewer;

import com.foreflight.server.diagnosticviewer.datastructures.BucketData;
import com.foreflight.server.diagnosticviewer.datastructures.Crash;
import com.foreflight.server.diagnosticviewer.datastructures.DataEntry;

import java.io.File;
import java.util.ArrayList;

public class DiagnosticInfo {

    private final ArrayList<File> filesIncluded;
    private final ArrayList<FileProcessor.Directory> directories;
    private ArrayList<Crash> crashes;


    public DiagnosticInfo(ArrayList<File> filesIncluded, ArrayList<FileProcessor.Directory> directories) {
        this.filesIncluded = filesIncluded;
        this.directories = directories;
        crashes = new ArrayList<>();
    }

    /** Main runner for DiagnosticInfo. Looks at what files are included and performs parsing,
     then appropriate data collection and analysis. After parsing files, creates a DataAnalysis
     object.
     @return DataAnalysis object containing information parsed from diagnostic files to give caller access to
     analysis techniques
     */
    public DataAnalysis parse() {
        Parser myParser = new Parser();
        //first look through stack report files and create Crash objects if applicable
        ArrayList<FileProcessor.Directory> stackDirectories = getStackDirectories(directories);
        for (int i = 0; i < stackDirectories.size(); i++) {
            ArrayList<Crash> getting = myParser.getStackCrashes(stackDirectories.get(i));
            if (getting.size() > 0) {
                crashes.addAll(myParser.getStackCrashes(stackDirectories.get(i)));
            }
        }

        // then go through sync_insights and masterLog to construct DataEntry objects and Buckets\
            /* step one: create un-filtered dataEntry log to ensure correct parsing
                   step two: decide how want to filter/contain dataEntries in order to streamline viewer/info
                   step three: implement changes */
                /* only allow DataEntries in arraylist if within 5 min of crash? -> ind data structures for containing
                    all dataEntries for each crash -> should create conditional if crash times close together? */
        File myFile = containsFile("sync_insights", filesIncluded);
        ArrayList<DataEntry> syncData = new ArrayList<>();
        ArrayList<BucketData> allBuckets = new ArrayList<>();
        ArrayList<DataEntry> masterLogData = new ArrayList<>();
        String userDefaults = "";
        String lastChangeAndShareSignatures = "";
        if (myFile != null) {
            syncData = myParser.syncParser(myFile);
            allBuckets = myParser.getBuckets();
        }
        myFile = containsFile("masterLog", filesIncluded);
        if (myFile != null) {
            masterLogData = myParser.masterLogParser(myFile);
            userDefaults = myParser.getUserDefaults();
            lastChangeAndShareSignatures = myParser.getLastChangeAndShareSignatures();
        }
        // go through remaining files
        DataAnalysis myAnalysis = new DataAnalysis(syncData, masterLogData, userDefaults, lastChangeAndShareSignatures, allBuckets, crashes);
        return myAnalysis;
    }


    /** Returns list of directories that contain stack information.
     @param directories1 the list of directories to search through
     @return ArrayList of directories that hold stack reports (note: no stack attachments reports included
     */
    private static ArrayList<FileProcessor.Directory> getStackDirectories(ArrayList<FileProcessor.Directory> directories1) {
        ArrayList<FileProcessor.Directory> stackDirectories = new ArrayList<>();
        for (int i = 0; i < directories1.size(); i++) {
            if (directories1.get(i).getName().contains("stack_reports") && !directories1.get(i).getName().contains("attachments")) {
                stackDirectories.add(directories1.get(i));
            }
        }
        return stackDirectories;
    }

    /** Finds file with the given name
     @param name the name of the file to search for
     @param filesIncluded1 ArrayList of files to search through
     @return File if found ; null if not found
     */
    private static File containsFile(String name, ArrayList<File> filesIncluded1) {
        for (int i = 0; i < filesIncluded1.size(); i++) {
            if (filesIncluded1.get(i).getName().equalsIgnoreCase(name)) {
                return filesIncluded1.get(i);
            }
        }
        return null;
    }

    /** Gets all the files within a given directory
     @param dirName the name of the directory to get files from
     @param directories1 list of directories to search for
     @return ArrayList of Files within the directory ; empty if directory not found
     */
    private static ArrayList<File> getDirectoryFiles(String dirName, ArrayList<FileProcessor.Directory> directories1) {
        for (int i = 0; i < directories1.size(); i++) {
            if (directories1.get(i).getName().equalsIgnoreCase(dirName)) {
                return directories1.get(i).getFiles();
            }
        }
        return new ArrayList<File>();
    }
}

