package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.Destination;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.REM;

/**
 *
 * @author gl10
 * @date 08/04/2025
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type resultType = super.verifyExpr(compiler, localEnv, currentClass);
        if (!resultType.isInt()) {
            throw new ContextualError("The modulo operator is only supported with integers.", getLocation());
        }
        return resultType;
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, Destination destination) {
        super.codeGenExpr(compiler, destination);
        if(!compiler.getCompilerOptions().doesNoCheck()){
            compiler.addInstruction(new BOV(compiler.procedures.divisionByZero()));
        }
    }

    @Override
    protected BinaryInstruction createArithOperation(GPRegister leftAndDestinationRegister, DVal rightRegister) {
        return new REM(rightRegister, leftAndDestinationRegister);
    }


    @Override
    protected String getOperatorName() {
        return "%";
    }

}
