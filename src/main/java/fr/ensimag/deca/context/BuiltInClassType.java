package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;

import java.util.function.BiConsumer;

public class BuiltInClassType extends ClassType {

    public BuiltInClassType(SymbolTable.Symbol className, BiConsumer<BuiltInClassType, EnvironmentExp> populator) {
        super(className);
        this.definition = new BuiltInClassDefinition(this, (members) -> populator.accept(this, members));
    }

    @Override
    public BuiltInClassDefinition getDefinition() {
        return (BuiltInClassDefinition) super.getDefinition();
    }
}
