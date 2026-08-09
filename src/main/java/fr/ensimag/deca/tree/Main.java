package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import static java.util.Objects.requireNonNull;

/**
 * @author gl10
 * @date 08/04/2025
 */
public class Main extends AbstractMain {
    private static final Logger LOG = Logger.getLogger(Main.class);

    private ListDeclVar declVariables;
    private ListInst insts;

    public Main(ListDeclVar declVariables,
                ListInst insts) {
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        this.declVariables = declVariables;
        this.insts = insts;
    }

    @Override
    protected void verifyMain(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify Main: start");
        Type voidType = compiler.environmentType.VOID;
        EnvironmentExp env = new EnvironmentExp(null);
        declVariables.verifyListDeclVariable(compiler, env, null);
        insts.verifyListInst(compiler, env, null, voidType);
        LOG.debug("verify Main: end");
    }

    @Override
    protected void codeGenMain(DecacCompiler compiler) {
        compiler.addInstruction(new ADDSP(this.declVariables.size() + compiler.stack.getGBIndex()));

        int offset = compiler.stack.getGBIndex();
        for (AbstractDeclVar decl : this.declVariables.getList()) {
            VariableDefinition definition = requireNonNull(decl.getDefinition(), "the variable definition should exist");
            definition.setOperand(new RegisterOffset(++offset, Register.LB));
            decl.codeGenDecl(compiler);
        }
        compiler.addComment("Beginning of main instructions:");

        // Generer le code IMA des instructions
        insts.codeGenListInst(compiler, new Label("endprogram"));

        if (!compiler.getCompilerOptions().doesNoCheck()) {
            // Place TSTO before the main and before the virtual table
            compiler.addFirst(new BOV(compiler.procedures.stackOverFlow()));
            compiler.addFirst(new TSTO(compiler.stack.getMaxTemporariesAllocations() + offset));
        }
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        declVariables.decompile(s);
        insts.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        declVariables.iter(f);
        insts.iter(f);
    }

    @Override
    public void prettyPrintChildren(PrintStream s, String prefix) {
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
    }
}
