package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ControlDestination;
import fr.ensimag.deca.codegen.Destination;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;

/**
 *
 * @author gl10
 * @date 08/04/2025
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected  void codeGenExpr(DecacCompiler compiler, Destination destination) {
        getLeftOperand().codeGenExpr(compiler, destination);
        compiler.addInstruction(new CMP(new ImmediateInteger(0), destination.getRegister()));
        Label label = compiler.labeller.create("and");
        compiler.addInstruction(new BEQ(label));
        getRightOperand().codeGenExpr(compiler, destination);
        compiler.addLabel(label);
    }

    @Override
    protected void codeGenCmp(DecacCompiler compiler, Destination destination, ControlDestination control) {
        if (control.isBranchToElse()) {
            getLeftOperand().codeGenCmp(compiler, destination, control);
            getRightOperand().codeGenCmp(compiler, destination, control);
        } else {
            Label label = compiler.labeller.create("and");
            getLeftOperand().codeGenCmp(compiler, destination, new ControlDestination(label));
            getRightOperand().codeGenCmp(compiler, destination, control);
            compiler.addLabel(label);
        }
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }


}
