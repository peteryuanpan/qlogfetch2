package com.peter.model;

public class DownLog {

    private ListLog listLog;
    private String dest;
    private int worker;
    private boolean overwrite;
    private int retry;

    public ListLog getListLog() {
        return listLog;
    }

    public void setListLog(ListLog listLog) {
        this.listLog = listLog;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public int getWorker() {
        return worker;
    }

    public void setWorker(int worker) {
        this.worker = worker;
    }

    public boolean isOverwrite() {
        return overwrite;
    }

    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }
}
