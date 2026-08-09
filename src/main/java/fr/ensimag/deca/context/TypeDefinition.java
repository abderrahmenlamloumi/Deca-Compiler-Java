package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;

/**
 * Definition of a Deca type (builtin or class).
 *
 * @author gl10
 * @date 08/04/2025
 */
public class TypeDefinition extends Definition {

    public TypeDefinition(Type type, Location location) {
        super(type, location);
    }

    @Override
    public String getNature() {
        return "type";
    }

    @Override
    public boolean isExpression() {
        return false;
    }

    public void markUsed() {
        // Nothing to do
    }

}
