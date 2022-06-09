package com.foreflight.server.diagnosticviewer.datastructures;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataEntry {

    private final Message message;
    private final int id;
    private final File fileFrom;
    private final Date entryDateAndTime;
    private final String loggerType;
    private final String className;
    private final String methodName;

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
        return id + " " + format.format(entryDateAndTime) + "Z |  " +  loggerType + " | -[" + className + " " + methodName + "]" + message;
    }

    public boolean isEarlier(DataEntry a) {
        if(a.getEntryDateAndTime().before(this.entryDateAndTime)) return true;
        return false;
    }
}
