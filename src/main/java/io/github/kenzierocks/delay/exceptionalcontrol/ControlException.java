package io.github.kenzierocks.delay.exceptionalcontrol;

public class ControlException extends RuntimeException {
    private static final long serialVersionUID = -3491685001222864164L;

    public static enum ActingType {
        PROVIDED_LABEL, INTERNAL_LABEL;
    }

    public static final String DEFAULT_LABEL = "\u0001DEFAULT_LABEL";

    private final ActingType type;

    public ControlException(String label) {
        super(label);
        type = ActingType.PROVIDED_LABEL;
    }

    public ControlException() {
        super(DEFAULT_LABEL);
        type = ActingType.INTERNAL_LABEL;
    }

    public ActingType getType() {
        return type;
    }

    public String getLabel() {
        return getMessage();
    }
}
