package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.RINT;

import java.io.PrintStream;

/**
 *
 * @author gl10
 * @date 08/04/2025
 */
public class ReadInt extends AbstractReadExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type value = compiler.environmentType.INT;
        setType(value);
        return value;
    }

    @Override
    protected DVal codeGenDVal(DecacCompiler compiler) {
        // It is safe to return a scratch register from here, since the user of this method is expected to
        // immediately use it.
        compiler.addInstruction(new RINT());
        if(!compiler.getCompilerOptions().doesNoCheck()){
            compiler.addInstruction(new BOV(compiler.procedures.ioError()));
        }
        return Register.R1;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readInt()");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    public void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

}
