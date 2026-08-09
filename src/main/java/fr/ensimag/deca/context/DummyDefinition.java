package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;

public class DummyDefinition extends Definition {
    public DummyDefinition(Type type, Location location) {
        super(type, location);
    }

    @Override
    public String getNature() {
        return "";
    }

    @Override
    public boolean isExpression() {
        return false;
    }
}
