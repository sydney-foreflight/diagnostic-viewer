/* Crash.java is an immutable data organization class. For each crash collected by stack_reports, a Crash object is created and holds
    the below info.
   Author: Sydney Thompson
   Date: 06/09/22
 */

package com.foreflight.server.diagnosticviewer.datastructures;

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

    @Override
    public String toString() {
        return String.format("hardware type: %s\nversion: %s\nDate/Time: %s\nApplication Specific Information: %s\n",
                hardwareModel, version, crashTime.toString(), applicationSpecificInfo);
    }

}
