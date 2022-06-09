package com.foreflight.server.diagnosticviewer;

import com.foreflight.server.diagnosticviewer.datastructures.BucketData;
import com.foreflight.server.diagnosticviewer.datastructures.DataEntry;
import com.foreflight.server.diagnosticviewer.datastructures.Message;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private ArrayList<BucketData> buckets;
    private String userDefaults;
    private String lastChangeAndShareSignatures;

    public Parser() {
        buckets = new ArrayList<>();
        userDefaults = "";
        lastChangeAndShareSignatures = "";
    }

    public ArrayList<BucketData> getBuckets() {
        return buckets;
    }

    public String getUserDefaults() {
        return userDefaults;
    }

    public String getLastChangeAndShareSignatures() {
        return lastChangeAndShareSignatures;
    }

    public ArrayList<Crash> getStackCrashes(FileProcessor.Directory stackDirect) {
        ArrayList<Crash> stackCrashes = new ArrayList<>();
        //code for parsing + formatting crashes
        return stackCrashes;
    }

    public ArrayList<DataEntry> syncParser(File file) {
        ArrayList<DataEntry> myEntries = new ArrayList<>();
        Pattern datePattern = Pattern.compile("\\d+\\s\\d{2}-\\d{2}-\\d{4}");
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        try {
            BufferedReader read = Files.newBufferedReader(file.toPath());
            String line;
            while ((line = read.readLine()) != null) {
                //System.out.printf("Scanned line " +  line +"\n");
                Matcher m = datePattern.matcher(line);
                if(m.find()) {
                    Scanner s = new Scanner(line);
                    int id = s.nextInt();
                    String dateAndTime = s.next() + " " + s.next();
                    dateAndTime = dateAndTime.substring(0, dateAndTime.length() - 1);
                    Date entry = format.parse(dateAndTime);
                    s.next(); //going through '|'
                    String loggerType = s.next();
                    s.next(); // passing '|'
                    line = line.substring(line.indexOf("[") + 1);
                    String className = line.substring(0, line.indexOf(" "));
                    String methodName = line.substring(line.indexOf(" ") + 1, line.indexOf("]"));
                    String message = line.substring(line.indexOf("]") + 1);
                    s.close();
                    // System.out.printf("id: %d, date: %s, class: %s, method: %s\n\n", id, entry.toString(), className, methodName);
                    if(line.indexOf("{(") != -1) {
                        while((line = read.readLine()) != null) {
                            message += "\n" + line;
                            if(line.indexOf(")}") != -1) {
                                break;
                            }
                        }
                    } else if(methodName.equals("backgroundDiagnosticFiles:")) {
                        parseBucketData(read);
                    }
                    DataEntry entry1 = new DataEntry(new Message(message), id, file, entry, loggerType, className, methodName);
                    myEntries.add(entry1);
                }
            } read.close();
        } catch (Exception e) {
            System.out.println("could not read sync file");
            e.printStackTrace();
        }
        return myEntries;
    }

    private void parseBucketData(BufferedReader reader) {
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                if(line.contains("Bucket Name")) {
                    String name = line.substring(line.indexOf(":") + 2);
                    line = reader.readLine() + " " + reader.readLine() + " " + reader.readLine() + " " +
                            reader.readLine() + " " + reader.readLine() + " " + reader.readLine(); //reads all lines for one bucket together
                    Scanner s = new Scanner(line);
                    s.next();
                    s.next();
                    String displayName = s.next();
                    s.next();
                    s.next();
                    s.next();
                    int localPendChanges = s.nextInt();
                    s.next();
                    s.next();
                    s.next();
                    int localEnqChanges = s.nextInt();
                    s.next();
                    s.next();
                    s.next();
                    int localEnqDeletions = s.nextInt();
                    s.next();
                    s.next();
                    s.next();
                    int totalPendChanges = s.nextInt();
                    s.next();
                    s.next();
                    s.next();
                    int totalNumObjects = s.nextInt();
                    buckets.add(new BucketData(name, displayName, localPendChanges, localEnqChanges, localEnqDeletions, totalPendChanges, totalNumObjects));
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error transitioning to reading bucket data");
            e.printStackTrace();
        }
    }

    public ArrayList<DataEntry> masterLogParser(File file) {
        ArrayList<DataEntry> myEntries = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        Pattern datePattern = Pattern.compile("\\d+\\s\\d{2}-\\d{2}-\\d{4}");
        Pattern endPattern = Pattern.compile("}$");
        try {
            BufferedReader read = Files.newBufferedReader(file.toPath());
            String line;
            while ((line = read.readLine()) != null) {
                Matcher m = datePattern.matcher(line);
                if(m.find()) {
                    Scanner s = new Scanner(line);
                    int id = s.nextInt();
                    String dateAndTime = s.next() + " " + s.next();
                    dateAndTime = dateAndTime.substring(0, dateAndTime.length() - 1);
                    Date entry = format.parse(dateAndTime);
                    s.next(); //going through '|'
                    String loggerType = s.next();
                    s.next(); // passing '|'
                    line = line.substring(line.indexOf("[") + 1);
                    String className = line.substring(0, line.indexOf(" "));
                    String methodName = line.substring(line.indexOf(" ") + 1, line.indexOf("]"));
                    String message = line.substring(line.indexOf("]") + 1);
                    s.close();
                    //System.out.println(id + " " + dateAndTime + " " + methodName);
                    if (methodName.equals("logSyncURLsWithMessage:")) {
                        message += "\n" + read.readLine() + "\n" + read.readLine();
                        // System.out.println(message);
                    } else if (methodName.equals("logUserDefaults")) {
                        setUserDefaultsAndChangeSignatures(read);
                    } else if(message.contains("{")) {
                        Matcher seeEnd = endPattern.matcher(line);
                        while(!seeEnd.find()) {
                            line = read.readLine();
                            message += "\n" + line;
                            seeEnd = endPattern.matcher(line);
                        }
                    }
                    myEntries.add(new DataEntry(new Message(message), id, file, entry, loggerType, className, methodName));
                }
            } read.close();
        } catch (Exception e) {
            System.out.println("Could not read masterLog file. ");
            e.printStackTrace();
        }
        return myEntries;
    }

    private void setUserDefaultsAndChangeSignatures(BufferedReader read) {
        String line;
        Pattern endSection = Pattern.compile("^}");
        try {
            while ((line = read.readLine()) != null) {
                if(line.contains("lastChange") || line.contains("lastShared")) {
                    lastChangeAndShareSignatures += line + "\n";
                } else {
                    userDefaults += line + "\n";
                } Matcher m = endSection.matcher(line);
                if(m.matches()) { //if end of section, goes back to process masterLog line by line
                    break;
                }
            }
        } catch (Exception e){
            System.out.println("Error getting user defaults from masterLog");
            e.printStackTrace();
        }
    }
}
