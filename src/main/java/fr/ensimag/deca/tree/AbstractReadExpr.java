package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Destination;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * read...() statement.
 *
 * @author gl10
 * @date 08/04/2025
 */
public abstract class AbstractReadExpr extends AbstractExpr {

    public AbstractReadExpr() {
        super();
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, Destination destination) {
        DVal readValue = codeGenDVal(compiler);
        compiler.addInstruction(new LOAD(readValue, destination.getRegister()));
    }
}
