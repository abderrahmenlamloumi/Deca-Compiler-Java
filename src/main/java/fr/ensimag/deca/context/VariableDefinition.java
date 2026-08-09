package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;

/**
 * Definition of a variable.
 *
 * @author gl10
 * @date 08/04/2025
 */
public class VariableDefinition extends ExpDefinition {
    public VariableDefinition(Type type, Location location) {
        super(type, location);
    }

    @Override
    public String getNature() {
        return "variable";
    }

    @Override
    public boolean isExpression() {
        return true;
    }
}
