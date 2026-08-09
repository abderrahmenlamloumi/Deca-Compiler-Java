package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;

import java.util.function.BiConsumer;

/**
 *
 * @author Ensimag
 * @date 08/04/2025
 */
public class StringType extends Type {

    private final boolean isNullable;
    private final EnvironmentExp members;
    private BiConsumer<StringType, EnvironmentExp> populator;

    public StringType(SymbolTable.Symbol name, boolean isNullable, BiConsumer<StringType, EnvironmentExp> populator) {
        super(name);
        this.isNullable = isNullable;
        this.members = new EnvironmentExp(null);
        this.populator = populator;
    }

    @Override
    public boolean isString() {
        return true;
    }

    @Override
    public boolean isClassOrNull() {
        return this.isNullable;
    }

    @Override
    public boolean isAssignableFrom(Type otherType) {
        return super.isAssignableFrom(otherType) || otherType.isNull();
    }

    @Override
    public boolean sameType(Type otherType) {
        return otherType.isString();
    }

    public EnvironmentExp getMembers() {
        if (this.populator != null) {
            BiConsumer<StringType, EnvironmentExp> consumer = this.populator;
            this.populator = null;
            consumer.accept(this, this.members);
        }
        return this.members;
    }
}
