package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;

public class CallableType extends Type{
    private final Type returnType;
    public CallableType(SymbolTable.Symbol name, Type returnType) {
        super(name);
        this.returnType = returnType;
    }

    @Override
    public boolean sameType(Type otherType) {
        return false;
    }

    public Type getReturnType() {
        return returnType;
    }
}
