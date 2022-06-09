package com.foreflight.server.diagnosticviewer;

import com.foreflight.server.diagnosticviewer.datastructures.DataEntry;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

public class SmallParseTests {

    public static void main(String[] args) {
        ArrayList<DataEntry> myEntries = new ArrayList<>();
        List<String> myStrings;
        Pattern pattern = Pattern.compile("\\d{2}\\s\\d{2}-\\d{2}-\\d{4}"); //\\d{2}\\s\\d{2}-\\d{2}-\\d{4}"
        File myFile = new File("/Users/sydney/Documents/syncParseTest.txt");


        try {
            Scanner scan = new Scanner(myFile);
        }

        catch (Exception e){
            System.out.println("Could not read sync file. \n\n");
            e.printStackTrace();
        }
    }
}
