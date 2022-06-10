package com.foreflight.server.diagnosticviewer.datastructures;

public class BucketData {
    private final String name;
    private final String displayName;
    private final int localPendingChanges;
    private final int localEnqChanges;
    private final int localEnqDeletions;
    private final int totalPendChanges;
    private final int numObjects;

    private final String conditionalSync;
    private final boolean flag;

    public BucketData(String name, String displayName, int localPendingChanges, int localEnqChanges,
                      int localEnqDeletions, int totalPendChanges, int numObjects, String conditionalSync) {
        this.name = name;
        this.displayName = displayName;
        this.localPendingChanges = localPendingChanges;
        this.localEnqChanges = localEnqChanges;
        this.localEnqDeletions = localEnqDeletions;
        this.totalPendChanges = totalPendChanges;
        this.numObjects = numObjects;
        this.conditionalSync = conditionalSync;
        if(conditionalSync.length() == 0) { conditionalSync = "NA"; }
        if(localEnqChanges > 0 || localPendingChanges > 0 || localEnqDeletions > 0 || totalPendChanges > 0 ||
                conditionalSync.equalsIgnoreCase("true")) {
            flag = true;
        } else {
            flag = false;
        }
    }

    public boolean isFlagged() {
        return flag;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getLocalPendingChanges() {
        return localPendingChanges;
    }

    public int getLocalEnqChanges() {
        return localEnqChanges;
    }

    public int getLocalEnqDeletions() {
        return localEnqDeletions;
    }

    public int getTotalPendChanges() {
        return totalPendChanges;
    }

    public int getNumObjects() {
        return numObjects;
    }

    @Override
    public String toString() {
        return String.format("Bucket Name: %s\nDisplay Name: %s\nLocal Pending Changes: %d\nLocal Enqueued Changes: %d\nLocal Enqueued Deletions: %d\nTotal Pending Changes: %d\nNumber of Objects: %d\n",
                name, displayName, localPendingChanges, localEnqChanges, localEnqDeletions, totalPendChanges, numObjects);
    }

}
