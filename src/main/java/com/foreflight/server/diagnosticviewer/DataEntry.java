package com.foreflight.server.diagnosticviewer;

import java.io.File;
import java.util.Date;

public class DataEntry {

    private Message message;
    private int id;
    private File fileFrom;
    private Date entryDateAndTime;
    private String loggerType;
    private String className;
    private String methodName;


    public Message getMessage() { return message; }

    public int getId() { return id; }

    public File getFileFrom() { return fileFrom; }

    public Date getEntryDateAndTime() { return entryDateAndTime; }

    public String getLoggerType() { return loggerType; }

    public String getClassName() { return className; }

    public String getMethodName() { return methodName; }
}