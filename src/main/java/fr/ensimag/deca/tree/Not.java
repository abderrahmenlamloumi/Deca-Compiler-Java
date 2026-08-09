package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.ControlDestination;
import fr.ensimag.deca.codegen.Destination;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.SUB;

/**
 *
 * @author gl10
 * @date 08/04/2025
 */
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type boolType = compiler.environmentType.BOOLEAN;
        getOperand().verifyCondition(compiler, localEnv, currentClass);
        setType(boolType);
        return boolType;
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, Destination destination) {
        getOperand().codeGenExpr(compiler, destination);
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), Register.R0));
        compiler.addInstruction(new SUB(destination.getRegister(), Register.R0));
        compiler.addInstruction(new LOAD(Register.R0, destination.getRegister()));
    }

    @Override
    protected void codeGenCmp(DecacCompiler compiler, Destination destination, ControlDestination control) {
        getOperand().codeGenCmp(compiler, destination, control.negate());
    }

    @Override
    protected String getOperatorName() {
        return "!";
    }
}
