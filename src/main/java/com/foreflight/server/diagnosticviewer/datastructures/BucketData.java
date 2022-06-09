package com.foreflight.server.diagnosticviewer.datastructures;

public class BucketData {
        private final String name;
        private final String displayName;
        private final int localPendingChanges;
        private final int localEnqChanges;
        private final int localEnqDeletions;
        private final int totalPendChanges;
        private final int numObjects;

        public BucketData(String name, String displayName, int localPendingChanges, int localEnqChanges,
                          int localEnqDeletions, int totalPendChanges, int numObjects) {
            this.name = name;
            this.displayName = displayName;
            this.localPendingChanges = localPendingChanges;
            this.localEnqChanges = localEnqChanges;
            this.localEnqDeletions = localEnqDeletions;
            this.totalPendChanges = totalPendChanges;
            this.numObjects = numObjects;
        }

        public String getName() { return name; }

        public String getDisplayName() { return displayName; }

        public int getLocalPendingChanges() { return localPendingChanges; }

        public int getLocalEnqChanges() { return localEnqChanges; }

        public int getLocalEnqDeletions() { return localEnqDeletions; }

        public int getTotalPendChanges() { return totalPendChanges; }

        public int getNumObjects() { return numObjects; }

}
