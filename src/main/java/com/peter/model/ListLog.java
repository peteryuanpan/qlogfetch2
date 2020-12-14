package com.peter.model;

import java.util.Date;

public class ListLog {

    private Date fromDate;
    private Date toDate;
    private String[] domains;
    private String domainStr;

    public ListLog() {
    }

    public ListLog(Date fromDate, Date toDate, String[] domains, String domainStr) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.domains = domains;
        this.domainStr = domainStr;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String[] getDomains() {
        return domains;
    }

    public void setDomains(String[] domains) {
        this.domains = domains;
    }

    public String getDomainStr() {
        return domainStr;
    }

    public void setDomainStr(String domainStr) {
        this.domainStr = domainStr;
    }
}
