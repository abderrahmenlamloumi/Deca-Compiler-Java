package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public interface AbstractType extends Locatable, PrintableTree {

    Type verifyType(DecacCompiler compiler) throws ContextualError;

    void decompile(IndentPrintStream s);

    Type getType();
}
