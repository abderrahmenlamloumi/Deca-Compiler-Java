package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.feature.FeatureFlag;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.ContextualError;

import java.io.PrintStream;
/**
 *
 * @author gl10
 * @date 08/04/2025
 */
public class Array implements AbstractType {

    private Location location;
    private final AbstractType innerType;
    private Type type;

    public Array(AbstractType type) {
        this.innerType = type;
    }

    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {
        if (!compiler.isFeatureEnabled(FeatureFlag.ARRAY)) {
            throw new ContextualError("Arrays are not supported in this version of Deca", getLocation());
        }
        Type type = this.innerType.verifyType(compiler);
        this.type = new ArrayType(type);
        return this.type;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        innerType.decompile(s);
        s.print("[]");
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public void iter(TreeFunction f) {
        this.innerType.iter(f);
    }

    @Override
    public Type getType() {
        return this.type;
    }

    @Override
    public void prettyPrintType(PrintStream s, String prefix) {
        s.print("Array (");
        this.innerType.prettyPrintType(s, prefix);
        s.print(")");
    }

    @Override
    public String prettyPrintNode() {
        return "Array (" + this.innerType.prettyPrintNode() + ")";
    }

    @Override
    public void prettyPrintChildren(PrintStream s, String prefix) {
        this.innerType.prettyPrint(s, prefix, true);
    }


}
