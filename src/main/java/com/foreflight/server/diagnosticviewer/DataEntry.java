package com.foreflight.server.diagnosticviewer;

import javax.xml.crypto.Data;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataEntry {

    private Message message;
    private int id;
    private File fileFrom;
    private Date entryDateAndTime;
    private String loggerType;
    private String className;
    private String methodName;

    public DataEntry(Message message, int id, File fileFrom, Date date, String loggerType, String className, String methodName) {
        this.message = message;
        this.id = id;
        this.fileFrom = fileFrom;
        entryDateAndTime = date;
        this.loggerType = loggerType;
        this.className = className;
        this.methodName = methodName;
    }
    public Message getMessage() { return message; }

    public int getId() { return id; }

    public File getFileFrom() { return fileFrom; }

    public Date getEntryDateAndTime() { return entryDateAndTime; }

    public String getLoggerType() { return loggerType; }

    public String getClassName() { return className; }

    public String getMethodName() { return methodName; }

    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        return id + " " + format.format(entryDateAndTime) + "Z | ~" +  loggerType + "~ | -[" + className + " " + methodName + "]" + message;
    }

    public boolean isEarlier(DataEntry a) {
        if(a.getEntryDateAndTime().before(this.entryDateAndTime)) return true;
        return false;
    }

    class BucketData {
        private String name;
        private String displayName;
        private int localPendingChanges;
        private int localEnqChanges;
        private int localEnqDeletions;
        private int totalPendChanges;
        private int numObjects;

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
}