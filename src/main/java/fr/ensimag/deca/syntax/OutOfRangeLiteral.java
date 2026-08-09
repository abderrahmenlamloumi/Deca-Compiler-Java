package fr.ensimag.deca.syntax;

import org.antlr.v4.runtime.ParserRuleContext;

public class OutOfRangeLiteral extends DecaRecognitionException {

    private static final long serialVersionUID = 4670163376041273741L;

    public OutOfRangeLiteral(DecaParser recognizer, ParserRuleContext ctx) {
        super(recognizer, ctx);
    }

    @Override
    public String getMessage() {
        return "literal is not representable in the target type";
    }
}
