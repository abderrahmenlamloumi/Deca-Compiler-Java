package fr.ensimag.deca.ir;

class ConditionCode {

    private final boolean equals;
    private final boolean lessThan;

    ConditionCode(boolean equals, boolean lessThan) {
        this.equals = equals;
        this.lessThan = lessThan;
    }

    boolean isLessThan() {
        return this.lessThan;
    }

    boolean isLessThanOrEquals() {
        return this.equals || this.lessThan;
    }

    boolean isGreaterThan() {
        return !this.lessThan && !this.equals;
    }

    boolean isGreaterThanOrEquals() {
        return this.equals || !this.lessThan;
    }

    boolean isEquals() {
        return this.equals;
    }

    boolean isNotEquals() {
        return !this.equals;
    }
}
