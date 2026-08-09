package fr.ensimag.deca.context;

public class ArrayType extends Type {

    private final Type innerType;

    public ArrayType(Type innerType) {
        super(innerType.getName());
        this.innerType = innerType;
    }

    @Override
    public boolean sameType(Type otherType) {
        if(otherType instanceof ArrayType) {
            ArrayType otherArrayType = (ArrayType)otherType;
            return otherArrayType.innerType.sameType(this.innerType);
        }
        return false;
    }

    @Override
    public boolean isAssignableFrom(Type otherType) {
        return super.isAssignableFrom(otherType) || otherType.isNull();
    }

    public Type getInnerType() {
        return innerType;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public boolean isClassOrNull() {
        return true;
    }

    @Override
    public String toString() {
        return this.innerType.toString() + "[]";
    }
}
