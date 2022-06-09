/* Parser.java parses different files depending on the method called. Mainly used to re-organize data into Object-centric structure.
   Author: Sydney Thompson
   Date: 06/09/22
 */


package com.foreflight.server.diagnosticviewer;

import com.foreflight.server.diagnosticviewer.datastructures.BucketData;
import com.foreflight.server.diagnosticviewer.datastructures.Crash;
import com.foreflight.server.diagnosticviewer.datastructures.DataEntry;
import com.foreflight.server.diagnosticviewer.datastructures.Message;
import sun.java2d.pipe.SpanShapeRenderer;

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

    /* Parses stack files to create Crash objects.
       @param stackDirect the directory to pull files from
       @return an ArrayList of Crashes created
     */
    public ArrayList<Crash> getStackCrashes(FileProcessor.Directory stackDirect) {
        ArrayList<Crash> stackCrashes = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //code for parsing + formatting crashes
        for(int i = 0; i < stackDirect.getFiles().size(); i++) {
            File file1 = stackDirect.getFiles().get(i);
            try {
                BufferedReader read = Files.newBufferedReader(file1.toPath());
                String line;
                read.readLine(); // Incident Identifier
                line = read.readLine(); // Hardware Model
                String hardWare = line.substring(line.indexOf(":") + 1).trim();
                read.readLine(); //Process
                read.readLine(); //Path
                read.readLine(); //Identifier
                line = read.readLine(); // Version
                String version = line.substring(line.indexOf(":") + 1).trim();
                read.readLine(); //Code Type
                read.readLine(); // Parent Process
                read.readLine(); //blank
                line = read.readLine();
                String time = line.substring(line.indexOf(":")+ 1, line.indexOf("+")).trim();
                Date timeCrash = format.parse(time);
                read.readLine(); //OS Version
                read.readLine(); //Report Version
                read.readLine(); //blank
                read.readLine(); //exception type
                read.readLine(); //exception codes
                read.readLine(); //crashed thread
                read.readLine(); //blank
                read.readLine(); //title
                String appInfo = read.readLine().trim(); //applicationSpecificInfo
                stackCrashes.add(new Crash(hardWare, version, timeCrash, appInfo));
            } catch (Exception e) {
                System.out.println("Could not read " + file1.getName());
                e.printStackTrace();
            }
        }
        return stackCrashes;
    }

    /* Parses the sync_insights file. Note: will also populate buckets ArrayList if applicable.
       @param file the file to parse
       @return ArrayList of the data entries pulled from sync_insights
     */
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


    /* Parses the part of sync_insights that contains bucket info and stores data in buckets variable.
       @param reader the BufferedReader used by syncParser in order to access appropriate section of sync_insights
     */
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

    /* Parses the masterLog file by creating DataEntries. Will also call setUserDefaultsAndChangeSignatures if
        needed to populate userDefaults and lastChangeAndShareSignatures.
       @param file the file to parse
     */
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

    /* Called from masterLogParser. Will go through and add information to userDefaults and lastChangeAndShareSignatures based
        on info from masterLog.
       @param read the BufferedReader masterLogParser uses to access the correct section of the file
     */
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
