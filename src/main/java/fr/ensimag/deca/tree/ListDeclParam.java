package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;

import java.util.List;

public class ListDeclParam extends TreeList<DeclParam> {
    @Override
    public void decompile(IndentPrintStream s) {
        List<DeclParam> params = getList();
        if (params.size() > 1) {
            params.get(0).decompile(s);
            s.print(", ");
            for (int i = 1; i < params.size() - 1; i++) {
                params.get(i).decompile(s);
                s.print(", ");
            }
            params.get(params.size() - 1).decompile(s);
        } else if (params.size() == 1) {
            params.get(0).decompile(s);
        }
    }
}
