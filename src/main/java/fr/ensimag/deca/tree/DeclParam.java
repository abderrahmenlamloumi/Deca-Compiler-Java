package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class DeclParam extends Tree{

    private final AbstractType type;
    private final AbstractIdentifier name;

    public DeclParam(AbstractType type, AbstractIdentifier name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        this.type.decompile(s);
        s.print(" ");
        this.name.decompile(s);
    }

    @Override
    public void prettyPrintChildren(PrintStream s, String prefix) {
        this.type.prettyPrint(s, prefix, false);
        this.name.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.type.iter(f);
        this.name.iter(f);
    }

    public AbstractType getType(){
        return this.type;
    }

    public AbstractIdentifier getName(){
        return name;
    }

    public void verifyParam(DecacCompiler compiler) throws ContextualError{
        Type varType = this.type.verifyType(compiler);
        if (varType.isVoid()) {
            throw new ContextualError("Param '" + this.name.getName().getName() + "' cannot have type void", this.type.getLocation());
        }
    }

}
