package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclMethod extends TreeList<DeclMethod>{
    @Override
    public void decompile(IndentPrintStream s) {
        for (DeclMethod method: getList()){
            method.decompile(s);
        }
    }

    public void codeGenMethods(DecacCompiler compiler){
        for (DeclMethod declMethod : getList()) {
            declMethod.codeGenMethod(compiler);
        }
    }
}
