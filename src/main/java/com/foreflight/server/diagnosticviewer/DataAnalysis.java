package com.foreflight.server.diagnosticviewer;

import com.foreflight.server.diagnosticviewer.datastructures.BucketData;
import com.foreflight.server.diagnosticviewer.datastructures.Crash;
import com.foreflight.server.diagnosticviewer.datastructures.DataEntry;

import java.util.ArrayList;

public class DataAnalysis {

    private final ArrayList<DataEntry> syncInfo;
    private final ArrayList<DataEntry> masterLogInfo;
    private final String userDefaults;
    private final String lastChangeAndShareSignatures;
    private final ArrayList<BucketData> buckets;
    private final ArrayList<Crash> crashes;

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
       for(int i = 0; i < buckets.size(); i++) {
           if(buckets.get(i).isFlagged()) {
               flagged.add(buckets.get(i));
           }
       } return flagged;
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
}
