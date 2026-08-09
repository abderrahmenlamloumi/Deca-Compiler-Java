package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;

import java.util.function.Consumer;

public class BuiltInClassDefinition extends ClassDefinition {

    private Consumer<EnvironmentExp> populator;

    public BuiltInClassDefinition(ClassType type, Consumer<EnvironmentExp> populator) {
        super(type, Location.BUILTIN, null);
        this.populator = populator;
    }

    @Override
    public EnvironmentExp getMembers() {
        EnvironmentExp members = super.getMembers();
        if (this.populator != null) {
            Consumer<EnvironmentExp> consumer = this.populator;
            this.populator = null;
            consumer.accept(members);
        }
        return members;
    }

    public boolean isUsed() {
        return this.populator == null;
    }

    @Override
    public void markUsed() {
        getMembers();
    }
}
