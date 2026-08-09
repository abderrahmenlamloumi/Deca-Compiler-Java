package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.Destination;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;


import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl10
 * @date 08/04/2025
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue) super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        Type leftOperandType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        if (getLeftOperand() instanceof Selection && (((Selection) getLeftOperand()).getObject()).getType().isArray()) {
            throw new ContextualError("Length is read only", getLocation());
        }
        setRightOperand(getRightOperand().verifyRValue(compiler, localEnv, currentClass, leftOperandType, "assign"));
        setType(leftOperandType);
        return leftOperandType;
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, Destination destination) {

        if ((getLeftOperand() instanceof Selection)) {
            Selection op = (Selection) getLeftOperand();
            op.getObject().codeGenExpr(compiler, destination);
            GPRegister value = compiler.stack.scoped((leftDestination) -> getRightOperand().codeGenExpr(compiler, leftDestination));
            if (!compiler.getCompilerOptions().doesNoCheck()) {
                compiler.addInstruction(new CMP(new NullOperand(), destination.getRegister()));
                compiler.addInstruction(new BEQ(compiler.procedures.dereferencingNull()));
            }
            compiler.addInstruction(new STORE(value, new RegisterOffset(op.getField().getFieldDefinition().getIndex(), destination.getRegister())));
            compiler.addInstruction(new LOAD(value, destination.getRegister()));
        } else if (getLeftOperand() instanceof Index) {
            compiler.addComment("Assigning to array");
            Index index = (Index) getLeftOperand();
            // indexArray[indexValue]
            AbstractExpr indexArray = index.getArray();
            AbstractExpr indexValue = index.getIndex();
            indexArray.codeGenExpr(compiler, destination);
            GPRegister newDest = compiler.stack.scoped(dest -> {
                indexValue.codeGenExpr(compiler, dest);
                compiler.addInstruction(new ADD(new ImmediateInteger(1), dest.getRegister()));
            });
            compiler.addInstruction(new PUSH(newDest));

            GPRegister assignValue = compiler.stack.scoped(dest -> {
                getRightOperand().codeGenExpr(compiler, dest);
            });
            compiler.addInstruction(new POP(Register.R1));
            compiler.addInstruction(new STORE(assignValue, new RegisterIndex(0, destination.getRegister(), Register.R1)));
        } else {
            getRightOperand().codeGenExpr(compiler, destination);
            ExpDefinition definition = ((AbstractIdentifier) getLeftOperand()).getExpDefinition();
            if (definition instanceof FieldDefinition) {
                compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R0));
                compiler.addInstruction(new STORE(destination.getRegister(), new RegisterOffset(((FieldDefinition) definition).getIndex(), Register.R0)));
            } else {
                DAddr offset = requireNonNull(definition.getOperand(), "variable location in memory is not set");
                compiler.addInstruction(new STORE(destination.getRegister(), offset));
            }
        }
    }

    @Override
    protected String getOperatorName() {
        return "=";
    }

}
