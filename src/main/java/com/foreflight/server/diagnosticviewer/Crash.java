package com.foreflight.server.diagnosticviewer;

import java.util.Date;
import java.util.Objects;

public class Crash {
    private final String hardwareModel;
    private final String version;
    private final Date crashTime;
    private final String applicationSpecificInfo;

    public Crash(String hardwareModel, String version, Date crashTime, String applicationSpecificInfo) {
        this.hardwareModel = hardwareModel;
        this.version = version;
        this.crashTime = crashTime;
        this.applicationSpecificInfo = applicationSpecificInfo;
    }

    public String getHardwareModel() {
        return hardwareModel;
    }

    public String getVersion() {
        return version;
    }

    public Date getCrashTime() {
        return crashTime;
    }

    public String getApplicationSpecificInfo() {
        return applicationSpecificInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Crash crash = (Crash) o;
        return hardwareModel.equals(crash.hardwareModel) && Objects.equals(version, crash.version) && Objects.equals(crashTime, crash.crashTime) && applicationSpecificInfo.equals(crash.applicationSpecificInfo);
    }
}
