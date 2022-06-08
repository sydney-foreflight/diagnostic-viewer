package com.foreflight.server.diagnosticviewer;

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
        Pattern pattern = Pattern.compile("\\d{2}\\s\\d{2}-\\d{2}-\\d{4}"); //\\d{2}\\s\\d{2}-\\d{2}-\\d{4}"
        File myFile = new File("/Users/sydney/Documents/syncParseTest.txt");


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
                String ind = myStrings.get(i);
                System.out.println(ind);
                Scanner scan = new Scanner(ind);
                int pid = scan.nextInt();
                String date = scan.next();
                String time = scan.next();
                date = date + " " + time.substring(0, time.length() - 1);
                Date date1 = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").parse(date);
                scan.next();
                String loggerType = scan.next();
                scan.next();
                String rest = "";
                while(scan.hasNext()) { rest +=  " " + scan.next(); }
                scan.close();
                rest = rest.substring(rest.indexOf("[") + 1);
                String className = rest.substring(0, rest.indexOf(" "));
                rest = rest.substring(rest.indexOf(" ") + 1);
                String methodName = rest.substring(0, rest.indexOf("]"));
                rest = rest.substring(rest.indexOf("]") + 1); //currently error w this bc parsing not done 100% correctly bc only processes pids w 2 digits
                DataEntry thisEntry = new DataEntry(new Message(rest), pid, myFile, date1, loggerType, className, methodName);
                System.out.println(thisEntry + "\n\n");
            }
        }

        catch (Exception e){
            System.out.println("Could not read sync file. \n\n");
            e.printStackTrace();
        }
    }
}
