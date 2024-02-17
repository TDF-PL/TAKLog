package pl.tdf.atak.TAKLog.sorting;

public enum SortMode {
    DISTANCE("distance"), TIME("time");

    private final String label;

    SortMode(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
