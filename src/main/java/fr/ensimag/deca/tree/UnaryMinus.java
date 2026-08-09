package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Destination;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.OPP;

/**
 * @author gl10
 * @date 08/04/2025
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
                Type operandType = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
                if (!(operandType.isInt() || operandType.isFloat())){
                    throw new ContextualError("Incompatible type for unary minus : expected float or int, got " + operandType, getLocation());
                }
                setType(operandType);
                return operandType;
            }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(getOperatorName());
        getOperand().decompile(s);
    }

    @Override
    public void codeGenExpr(DecacCompiler compiler, Destination destination) {
        getOperand().codeGenExpr(compiler, destination);
        compiler.addInstruction(new OPP(destination.getRegister(), destination.getRegister()));
    }

    @Override
    protected String getOperatorName() {
        return "-";
    }

}
