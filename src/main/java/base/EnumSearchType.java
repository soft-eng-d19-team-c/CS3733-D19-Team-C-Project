package base;

public enum EnumSearchType {
    LEVENSHTEIN(7),
    COMPARISON(0);

    protected int tolerance;

    EnumSearchType(int tolerance) {
        this.tolerance = tolerance;
    }

    public int getTolerance() {
        return this.tolerance;
    }
    public void setTolerance(int tolerance) {
        this.tolerance = tolerance;
    }
}
