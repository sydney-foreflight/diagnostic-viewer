package com.foreflight.server.diagnosticviewer;

import com.foreflight.server.diagnosticviewer.datastructures.BucketData;
import com.foreflight.server.diagnosticviewer.datastructures.Crash;
import com.foreflight.server.diagnosticviewer.datastructures.DataEntry;
import com.foreflight.server.diagnosticviewer.datastructures.Message;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class DataAnalysis {

    private final ArrayList<DataEntry> syncInfo;
    private final ArrayList<DataEntry> masterLogInfo;
    private final String userDefaults;
    private final String lastChangeAndShareSignatures;
    private final ArrayList<BucketData> buckets;
    private final ArrayList<Crash> crashes;

    private final Comparator myCompare = new Comparator<DataEntry>() {
        @Override
        public int compare(DataEntry o1, DataEntry o2) {
            return o1.getEntryDateAndTime().compareTo(o2.getEntryDateAndTime());
        }
    };

    public DataAnalysis(ArrayList<DataEntry> syncInfo, ArrayList<DataEntry> masterLogInfo, String userDefaults,
                        String lastChangeAndShareSignatures, ArrayList<BucketData> buckets, ArrayList<Crash> crashes) {
        this.syncInfo = syncInfo;
        this.masterLogInfo = masterLogInfo;
        this.userDefaults = userDefaults;
        this.lastChangeAndShareSignatures = lastChangeAndShareSignatures;
        this.buckets = buckets;
        this.crashes = crashes;
    }


    public ArrayList<BucketData> getFlaggedBuckets() {
        ArrayList<BucketData> flagged = new ArrayList<>();
        for (int i = 0; i < buckets.size(); i++) {
            if (buckets.get(i).isFlagged()) {
                flagged.add(buckets.get(i));
            }
        }
        return flagged;
    }

    public ArrayList<DataEntry> getSyncInfo() {
        return syncInfo;
    }

    public ArrayList<DataEntry> getMasterLogInfo() {
        return masterLogInfo;
    }

    public String getUserDefaults() {
        return userDefaults;
    }

    public String getLastChangeAndShareSignatures() {
        return lastChangeAndShareSignatures;
    }

    public ArrayList<BucketData> getBuckets() {
        return buckets;
    }

    public ArrayList<Crash> getCrashes() {
        return crashes;
    }

    /** Returns an ArrayList of Sets of DataEntry for each crashLog. Each set contains all logs from different files
     *  sorted by the time recorded.
     *  @param minBefore the number of minutes before the crash to include in the Set
     *  @param minAfter the number of minutes after the crash to include in the Set
     *  @return an ArrayList of Sets that sorts DataEntries around the time of each crash
     */
    public ArrayList<Set<DataEntry>> getEntriesAroundCrashes(int minBefore, int minAfter) {

        ArrayList<Set<DataEntry>> entriesPerCrash = new ArrayList<>();
        TreeSet<DataEntry> everything = (TreeSet) combineEntries();
        try {
            for (int i = 0; i < crashes.size(); i++) {
                Set<DataEntry> crashEntries = new TreeSet<>(myCompare);
                Calendar cal = Calendar.getInstance();
                Date date = crashes.get(i).getCrashTime();
                cal.setTime(date);
                cal.add(Calendar.MINUTE, minBefore * -1);
                DataEntry lowest = new DataEntry(new Message(""), 0, null, cal.getTime(),
                        "", "", "");
                DataEntry floorLowest = everything.floor(lowest);
                if (floorLowest == null) {
                    floorLowest = everything.first();
                }
                cal.add(Calendar.MINUTE, minBefore + minAfter);
                DataEntry highest = new DataEntry(new Message(""), 0, null, cal.getTime(),
                        "", "", "");
                DataEntry floorHighest = everything.higher(highest);
                if (floorHighest == null) {
                    floorHighest = everything.last();
                }
                crashEntries = everything.subSet(floorLowest, floorHighest);
                entriesPerCrash.add(crashEntries);

            }
        } catch (Exception e) {
            System.out.println("Error combining all diagnostic entries");
            e.printStackTrace();
        }
        return entriesPerCrash;
    }

    public Set<DataEntry> combineEntries() {
        Set<DataEntry> allEntries = new TreeSet<>(myCompare);
        allEntries.addAll(syncInfo);
        allEntries.addAll(masterLogInfo);
        return allEntries;
    }

    public Set<DataEntry> getErrorMessages() {
        Set<DataEntry> errorEntries = new TreeSet<>(myCompare);
        for(int i = 0; i < masterLogInfo.size(); i++) {
            if(masterLogInfo.get(i).getClassName().equalsIgnoreCase("Error Message")) {
                errorEntries.add(masterLogInfo.get(i));
            }
        } return errorEntries;
    }
}
