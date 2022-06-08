package com.foreflight.server.diagnosticviewer;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmallParseTests {

    public static void main(String[] args) {
        ArrayList<DataEntry> myEntries = new ArrayList<>();
        List<String> myStrings;
        Pattern pattern = Pattern.compile("\\d{2}\\s\\d{2}-\\d{2}-\\d{4}"); //\\d{2}\\s\\d{2}-\\d{2}-\\d{4}"
        File myFile = new File("/Users/sydney/Documents/testing/sync_insights");


        try {
            String syncInsight = new String(Files.readAllBytes(myFile.toPath()));
//            Matcher m = pattern.matcher(syncInsight);
//            m.find();
//            System.out.println("group count: " + m.groupCount());
//            int start = 0;
//            while(m.find(start)) {
//                int start1 = m.start() + 1;
//                if(m.find(start1)) { myStrings.add(syncInsight.substring((start1 - 1), m.start())); }
//                else { myStrings.add(syncInsight.substring(start)); }
//                start = start1;
//            }
            myStrings = Arrays.asList(syncInsight.split("(?=\\d{2}\\s\\d{2}-\\d{2}-\\d{4})"));
            for(int i = 0; i < myStrings.size(); i++) {
                System.out.println(myStrings.get(i) + "\n\n");
            } System.out.println("size of array: " + myStrings.size());
        }

        catch (Exception e){
            System.out.println("Could not read sync file. \n\n");
            e.printStackTrace();
        }
    }
}
