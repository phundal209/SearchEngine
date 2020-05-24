public enum Argument {
    Directory("-d");

    private String flag;
    Argument(String flag) {
        this.flag = flag;
    }

    public String getFlag() {
        return this.flag;
    }
}
