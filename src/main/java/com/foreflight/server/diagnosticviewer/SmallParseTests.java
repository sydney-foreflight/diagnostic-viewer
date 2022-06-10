/* SmallParserTests.java is intended as a testing class in order to briefly check if parsers are working the correct way.
   Author: Sydney Thompson
   Date: 06/09/22
 */
package com.foreflight.server.diagnosticviewer;

import com.foreflight.server.diagnosticviewer.datastructures.Crash;
import com.foreflight.server.diagnosticviewer.datastructures.DataEntry;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SmallParseTests {

    public static void main(String[] args) {
        ArrayList<DataEntry> myEntries = new ArrayList<>();
        List<String> myStrings;
        File myFile = new File("/Users/sydney/Documents/testing/sync_insights");
        Parser p = new Parser();
        //System.out.println(p.syncParser(myFile));
        // System.out.println(p.getBuckets());
        File masterFile = new File("/Users/sydney/Documents/testing/masterLog");
        // printArray(p.masterLogParser(masterFile));
        //System.out.println(p.getUserDefaults() + p.getLastChangeAndShareSignatures());

        File stackFile = new File("/Users/sydney/Documents/testing/stack_reports_sync/153943803");
        printArray(getStackCrashes(stackFile));

    }

    private static <T> void printArray(ArrayList<T> array) {
        for (int i = 0; i < array.size(); i++) {
            System.out.println(array.get(i).toString());
        }
        System.out.println("\n\n");
    }


    public static ArrayList<Crash> getStackCrashes(File file) {
        ArrayList<Crash> stackCrashes = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            BufferedReader read = Files.newBufferedReader(file.toPath());
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
            String time = line.substring(line.indexOf(":") + 1, line.indexOf("+")).trim();
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
            System.out.println("Could not read " + file.getName());
            e.printStackTrace();
        }

        return stackCrashes;
    }
}
