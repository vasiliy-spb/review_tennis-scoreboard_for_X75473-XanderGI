package io.github.XanderGI.model;

public enum Point {
    ZERO, FIFTEEN, THIRTY, FORTY, ADVANTAGE;

    public Point next() {
        return switch (this) {
            case ZERO -> FIFTEEN;
            case FIFTEEN -> THIRTY;
            case THIRTY -> FORTY;
            case FORTY -> ADVANTAGE;
            case ADVANTAGE -> throw  new IllegalArgumentException("The score can't be more than an advantage");
        };
    }

    public Point prev() {
        return switch (this) {
            case ZERO, FIFTEEN, THIRTY, FORTY -> throw  new IllegalArgumentException("The score of less than forty cannot decrease");
            case ADVANTAGE -> FORTY;
        };
    }
}