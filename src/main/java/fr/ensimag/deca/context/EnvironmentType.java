package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import java.util.HashMap;
import java.util.Map;

import fr.ensimag.deca.feature.FeatureFlag;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;

// A FAIRE: étendre cette classe pour traiter la partie "avec objet" de Déca
/**
 * Environment containing types. Initially contains predefined identifiers, more
 * classes can be added with declareClass().
 *
 * @author gl10
 * @date 08/04/2025
 */
public class EnvironmentType {
    public EnvironmentType(DecacCompiler compiler) {
        
        envTypes = new HashMap<Symbol, TypeDefinition>();
        
        Symbol intSymb = compiler.createSymbol("int");
        INT = new IntType(intSymb);
        envTypes.put(intSymb, new TypeDefinition(INT, Location.BUILTIN));

        Symbol floatSymb = compiler.createSymbol("float");
        FLOAT = new FloatType(floatSymb);
        envTypes.put(floatSymb, new TypeDefinition(FLOAT, Location.BUILTIN));

        Symbol voidSymb = compiler.createSymbol("void");
        VOID = new VoidType(voidSymb);
        envTypes.put(voidSymb, new TypeDefinition(VOID, Location.BUILTIN));

        Symbol booleanSymb = compiler.createSymbol("boolean");
        BOOLEAN = new BooleanType(booleanSymb);
        envTypes.put(booleanSymb, new TypeDefinition(BOOLEAN, Location.BUILTIN));

        Symbol stringSymb = compiler.createSymbol("string");
        Symbol equalsSymb = compiler.createSymbol("equals");
        Symbol lengthSymb = compiler.createSymbol("length");
        STRING = new StringType(stringSymb, compiler.isFeatureEnabled(FeatureFlag.STRING_OBJECT), (type, members) -> {
            Signature signature = new Signature();
            signature.add(type);
            MethodDefinition equalsMethod = new MethodDefinition(
                    new CallableType(equalsSymb, BOOLEAN),
                    Location.BUILTIN,
                    signature,
                    0
            );
            equalsMethod.setLabel(compiler.procedures.stringEquals());
            MethodDefinition lengthMethod = new MethodDefinition(
                    new CallableType(lengthSymb, INT),
                    Location.BUILTIN,
                    new Signature(),
                    0
            );
            lengthMethod.setLabel(compiler.procedures.stringLength());
            try {
                members.declare(equalsSymb, equalsMethod);
                members.declare(lengthSymb, lengthMethod);
            } catch (EnvironmentExp.DoubleDefException e) {
                throw new RuntimeException(e);
            }
        });
        if (compiler.isFeatureEnabled(FeatureFlag.STRING_OBJECT)) {
            envTypes.put(stringSymb, new TypeDefinition(STRING, Location.BUILTIN));
        }

        Symbol nullSymb = compiler.createSymbol("null");
        NULL = new NullType(nullSymb);

        Symbol objectSymb = compiler.createSymbol("Object");
        OBJECT = new BuiltInClassType(objectSymb, (objectType, members) -> {
            Signature signature = new Signature();
            signature.add(objectType);
            int index = objectType.getDefinition().incNumberOfMethods();
            MethodDefinition equalsMethod = new MethodDefinition(
                    new CallableType(equalsSymb, BOOLEAN),
                    Location.BUILTIN,
                    signature,
                    index
            );
            equalsMethod.setLabel(compiler.procedures.objectEquals());
            try {
                members.declare(equalsSymb, equalsMethod);
            } catch (EnvironmentExp.DoubleDefException ex) {
                throw new IllegalStateException(ex);
            }
            objectType.getDefinition().getVtable().addLabel(equalsMethod.getIndex(), equalsMethod.getLabel());
        });
        envTypes.put(objectSymb, OBJECT.getDefinition());
    }

    private final Map<Symbol, TypeDefinition> envTypes;

    public TypeDefinition defOfType(Symbol s) {
        TypeDefinition type = envTypes.get(s);
        if (type != null) {
            type.markUsed();
        }
        return type;
    }

    public void defineClass(Symbol s, ClassDefinition def) {
        envTypes.put(s, def);
    }

    public boolean exist(Symbol s) {
        return envTypes.containsKey(s);
    }

    public final VoidType    VOID;
    public final IntType     INT;
    public final FloatType   FLOAT;
    public final StringType  STRING;
    public final BooleanType BOOLEAN;
    public final NullType    NULL;
    public final BuiltInClassType    OBJECT;
}
