package io;

public enum Argument {
    Directory("-d"),
    Query("-q"),
    ResultsFile("-r"),
    IndexOutputFile("-i");

    private String flag;
    Argument(String flag) {
        this.flag = flag;
    }

    public String getFlag() {
        return this.flag;
    }
}
