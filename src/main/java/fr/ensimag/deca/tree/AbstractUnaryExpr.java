package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * Unary expression.
 *
 * @author gl10
 * @date 08/04/2025
 */
public abstract class AbstractUnaryExpr extends AbstractExpr {

    protected abstract String getOperatorName();

    private AbstractExpr operand;

    public AbstractExpr getOperand() {
        return operand;
    }
    
    public AbstractUnaryExpr(AbstractExpr operand) {
        Validate.notNull(operand);
        this.operand = operand;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(getOperatorName());
        s.print("(");
        this.operand.decompile(s);
        s.print(")");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        operand.iter(f);
    }

    @Override
    public void prettyPrintChildren(PrintStream s, String prefix) {
        operand.prettyPrint(s, prefix, true);
    }

}
