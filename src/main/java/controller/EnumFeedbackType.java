package controller;

public enum EnumFeedbackType {
    GOOD(2),
    OK(1),
    BAD(0);

    private int value;

    EnumFeedbackType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
