package fr.ensimag.deca.syntax;

import org.antlr.v4.runtime.ParserRuleContext;

public class InstanceOfArrayNotAllowed extends DecaRecognitionException {

    private static final long serialVersionUID = 4670163376041273741L;

    public InstanceOfArrayNotAllowed(DecaParser recognizer, ParserRuleContext ctx) {
        super(recognizer, ctx);
    }

    @Override
    public String getMessage() {
        return "Instanceof can't be used with arrays";
    }
}
