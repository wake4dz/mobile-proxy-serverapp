package com.wakefern.logging;

import java.io.File;
import java.text.NumberFormat;
/**
 * This class is used to get a running app's memory usage + OS + disk info.
 * Currently, it is not being used. 
 * In the future, we might create a CROB job to periodically (eg. every 30 minutes) send 
 * the app's memory usage info to the Papertrail for the memory analysis of a particular server instance
 */
public class SystemInfo {
    private Runtime runtime = Runtime.getRuntime();

    public String getSysInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getOsInfo());
        sb.append(this.getMemInfo());
        //sb.append(this.getDiskInfo());
        return sb.toString();
    }

    public String getOsName() {
        return System.getProperty("os.name");
    }

    public String getOsVersion() {
        return System.getProperty("os.version");
    }

    public String getOsArch() {
        return System.getProperty("os.arch");
    }

    public long getTotalMem() {
        return Runtime.getRuntime().totalMemory();
    }

    public long usedMem() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    public String getMemInfo() {
        NumberFormat format = NumberFormat.getInstance();
        StringBuilder sb = new StringBuilder();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        sb.append("Free memory: ");
        sb.append(format.format(freeMemory / 1024));
        sb.append(", ");
        sb.append("Allocated memory: ");
        sb.append(format.format(allocatedMemory / 1024));
        sb.append(",");
        sb.append("Max memory: ");
        sb.append(format.format(maxMemory / 1024));
        sb.append(",");
        sb.append("Total free memory: ");
        sb.append(format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024));
        sb.append(",");
        return sb.toString();

    }

    public String getOsInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("OS: ");
        sb.append(this.getOsName());
        sb.append(", ");
        sb.append("Version: ");
        sb.append(this.getOsVersion());
        sb.append(", ");
        sb.append(": ");
        sb.append(this.getOsArch());
        sb.append(", ");
        sb.append("Available processors (cores): ");
        sb.append(runtime.availableProcessors());
        sb.append(", ");
        return sb.toString();
    }

    public String getDiskInfo() {
        /* Get a list of all filesystem roots on this system */
        File[] roots = File.listRoots();
        StringBuilder sb = new StringBuilder();

        /* For each filesystem root, print some info */
        for (File root : roots) {
            sb.append("File system root: ");
            sb.append(root.getAbsolutePath());
            sb.append(",");
            sb.append("Total space (bytes): ");
            sb.append(root.getTotalSpace());
            sb.append(", ");
            sb.append("Free space (bytes): ");
            sb.append(root.getFreeSpace());
            sb.append(", ");
            sb.append("Usable space (bytes): ");
            sb.append(root.getUsableSpace());
            sb.append(", ");
        }
        return sb.toString();
    }
}
