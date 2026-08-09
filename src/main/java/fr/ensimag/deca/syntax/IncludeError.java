package fr.ensimag.deca.syntax;

import org.antlr.v4.runtime.IntStream;

/**
 * Exception raised when a #include is found for a file that cannot be found or opened.
 *
 * @author gl10
 * @date 08/04/2025
 */
public class IncludeError extends DecaRecognitionException {
    protected final String name;
    protected final String message;

    public IncludeError(String name, AbstractDecaLexer recognizer, IntStream input, String message) {
        super(recognizer, input);
        this.name = name;
        this.message = message;
    }
    public IncludeError(String name, AbstractDecaLexer recognizer, IntStream input) {
        super(recognizer, input);
        this.name = name;
        this.message = "Include error";
    }

    public String getName() {
        return name;
    }

    @Override
    public String getMessage() {
        return name + ": " + message;
    }

    private static final long serialVersionUID = -8541996188279897766L;

}
