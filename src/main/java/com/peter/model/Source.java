package com.peter.model;

public enum Source {

    qiniu("Qiniu"),
    aliyun("Aliyun");

    private String upperCaseName;

    Source(String upperCaseName) {
        this.upperCaseName = upperCaseName;
    }

    public String getUpperCaseName() {
        return upperCaseName;
    }

    public static Source getSource(String name) {
        if (name != null) {
            for (Source source : Source.values()) {
                if (source.name().toLowerCase().equals(name.toLowerCase()))
                    return source;
            }
        }
        return null;
    }
}
