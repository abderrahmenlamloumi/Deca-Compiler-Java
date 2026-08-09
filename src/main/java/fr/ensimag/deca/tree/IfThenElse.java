package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.ControlDestination;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import org.apache.commons.lang.Validate;

/**
 * Full if/else if/else statement.
 *
 * @author gl10
 * @date 08/04/2025
 */
public class IfThenElse extends AbstractInst {
    
    private final AbstractExpr condition; 
    private final ListInst thenBranch;
    private ListInst elseBranch;

    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        this.condition.verifyCondition(compiler, localEnv, currentClass);
        this.thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
        this.elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    public void setElseBranch(ListInst elseBranch) {
        this.elseBranch = elseBranch;
    }

    public void addElseBranch(AbstractInst inst) {
        this.elseBranch.add(inst);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, Label restoreParametersLabel) {
        Label elseLabel = compiler.labeller.create("else");
        Label endLabel = compiler.labeller.create("end");
        ControlDestination control = new ControlDestination(elseLabel);
        compiler.stack.scoped((conditionDestination) -> this.condition.codeGenCmp(compiler, conditionDestination, control));

        this.thenBranch.codeGenListInst(compiler, restoreParametersLabel);
        compiler.addInstruction(new BRA(endLabel));
        compiler.addLabel(elseLabel);
        this.elseBranch.codeGenListInst(compiler, restoreParametersLabel);
        compiler.addLabel(endLabel);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("if (");
        this.condition.decompile(s);
        s.println(") {");
        s.indent();
        this.thenBranch.decompile(s);
        s.unindent();
        s.println("} else {");
        s.indent();
        this.elseBranch.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    public void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }
}
