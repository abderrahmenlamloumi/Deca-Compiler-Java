package fr.ensimag.deca.functional;

public enum ExpectedTermination {

    PARSE_ERROR,
    PARSE_SUCCESS,

    CONTEXT_ERROR,
    CONTEXT_SUCCESS,

    VALID,
    RUNTIME_ERROR;

    public boolean doesAssertRuntime() {
        return this == VALID || this == RUNTIME_ERROR;
    }

    public boolean isError() {
        return this == PARSE_ERROR || this == CONTEXT_ERROR || this == RUNTIME_ERROR;
    }
}
