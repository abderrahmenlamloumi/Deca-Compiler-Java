package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;

/**
 * Type defined by a class.
 *
 * @author gl10
 * @date 08/04/2025
 */
public class ClassType extends Type {
    
    protected ClassDefinition definition;
    
    public ClassDefinition getDefinition() {
        return this.definition;
    }

    @Override
    public boolean isAssignableFrom(Type otherType) {
        if (otherType.isNull()) {
            return true;
        }
        if (!(otherType instanceof ClassType)) {
            return false;
        }
        ClassDefinition otherClassDef = ((ClassType) otherType).getDefinition();
        while (otherClassDef != null) {
            if (otherClassDef.getType().sameType(this)) {
                return true;
            }
            otherClassDef = otherClassDef.getSuperClass();
        }
        return false;
    }
            
    @Override
    public ClassType asClassType(String errorMessage, Location l) {
        return this;
    }

    @Override
    public boolean isClass() {
        return true;
    }

    @Override
    public boolean isClassOrNull() {
        return true;
    }

    /**
     * Standard creation of a type class.
     */
    public ClassType(Symbol className, Location location, ClassDefinition superClass) {
        super(className);
        this.definition = new ClassDefinition(this, location, superClass);
    }

    /**
     * Creates a type representing a class className.
     * (To be used by subclasses only)
     */
    protected ClassType(Symbol className) {
        super(className);
    }
    

    @Override
    public boolean sameType(Type otherType) {
        return this == otherType;
    }

}
