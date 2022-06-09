package com.foreflight.server.diagnosticviewer;

import com.foreflight.server.diagnosticviewer.datastructures.DataEntry;
import com.foreflight.server.diagnosticviewer.datastructures.Message;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmallParseTests {

    public static void main(String[] args) {
        ArrayList<DataEntry> myEntries = new ArrayList<>();
        List<String> myStrings;
        File myFile = new File("/Users/sydney/Documents/testing/sync_insights");
        Parser p = new Parser();
        //System.out.println(p.syncParser(myFile));
       // System.out.println(p.getBuckets());
        File masterFile = new File("/Users/sydney/Documents/testing/masterLog");
        printArray(p.masterLogParser(masterFile));
        System.out.println(p.getUserDefaults() + p.getLastChangeAndShareSignatures());
    }

    private static void printArray(ArrayList<DataEntry> array) {
       /* for(int i = 0; i < array.size(); i++) {
            System.out.println(array.get(i).toString());
        } System.out.println("\n\n");*/
    }
}
