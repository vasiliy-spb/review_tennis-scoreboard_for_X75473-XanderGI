package io.github.XanderGI.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Point {
    ZERO("0"), FIFTEEN("15"), THIRTY("30"), FORTY("40"), ADVANTAGE("AD");

    private final String value;

    Point next() {
        return switch (this) {
            case ZERO -> FIFTEEN;
            case FIFTEEN -> THIRTY;
            case THIRTY -> FORTY;
            case FORTY -> ADVANTAGE;
            case ADVANTAGE -> throw new IllegalStateException("The score can't be more than an advantage");
        };
    }

    // Этот метод не должен знать о бизнес-правилах приложения (что после 40 и ниже нельзя вернуться обратно) — это обязанность клиентского кода
    Point prev() {
        return switch (this) {
            case ZERO, FIFTEEN, THIRTY, FORTY ->
                    throw new IllegalStateException("The score of less than forty cannot decrease");
            case ADVANTAGE -> FORTY;
        };
    }
}