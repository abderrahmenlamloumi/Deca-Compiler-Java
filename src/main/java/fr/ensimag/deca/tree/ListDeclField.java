package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclField extends TreeList<DeclField> {

    @Override
    public void decompile(IndentPrintStream s) {
        for (DeclField field : getList()) {
            field.decompile(s);
            s.println(";");
        }
    }

    public void codeGenFields(DecacCompiler compiler) {
        for (DeclField declField : getList()) {
            if ((declField.getInitialization() instanceof Initialization)){
                declField.codeGenInit(compiler);
            }
        }
    }

    public void codeGenFieldsDefaultInit(DecacCompiler compiler) {
        for (DeclField declField : getList()) {
            declField.codeGenDefaultInit(compiler);
        }
    }
}
