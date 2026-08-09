package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.Destination;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl10
 * @date 08/04/2025
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) {
        assert getOperand().getType().isInt();
        Type floatType = compiler.environmentType.FLOAT;
        setType(floatType);
        return floatType;
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, Destination destination) {
        getOperand().codeGenExpr(compiler, destination);
        compiler.addInstruction(new FLOAT(destination.getRegister(), destination.getRegister()));
    }

    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

}
