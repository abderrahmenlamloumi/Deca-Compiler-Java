package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.VirtualTable;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.log4j.Logger;

import java.util.stream.Stream;

/**
 * @author gl10
 * @date 08/04/2025
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        for (AbstractDeclClass decl : this.getList()) {
            decl.verifyClass(compiler);
        }
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        for (AbstractDeclClass abstractDecl : this.getList()) {
            DeclClass decl = (DeclClass) abstractDecl;
            decl.verifyClassMembers(compiler);
            // Save the method labels in the Vtable
            for (DeclMethod declMethod : decl.getMethods().getList()) {
                decl.getDefinition().addLabelInVtable(declMethod.getName().getMethodDefinition().getIndex(),
                        declMethod.getName().getMethodDefinition().getLabel());
            }
        }
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        for (AbstractDeclClass decl : this.getList()) {
            decl.verifyClassBody(compiler);
        }
    }

    public void codeGenVtable(DecacCompiler compiler) {
        Stream<ClassDefinition> stream = getList().stream().map(def ->  ((DeclClass) def).getDefinition());
        if (compiler.environmentType.OBJECT.getDefinition().isUsed()) {
            stream = Stream.concat(Stream.of(compiler.environmentType.OBJECT.getDefinition()), stream);
        }
        Iterable<ClassDefinition> iterable = stream::iterator;
        for (ClassDefinition def : iterable) {
            RegisterOffset tableStart = new RegisterOffset(compiler.stack.incGBIndex(), Register.GB);
            def.setOffset(tableStart.getOffset());

            if (def.getSuperClass() == null) {
                compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
            } else {
                compiler.addInstruction(new LEA(new RegisterOffset(def.getSuperClass().getOffset(), Register.LB), Register.R0));
            }
            compiler.addInstruction(new STORE(Register.R0, tableStart));

            VirtualTable table = def.getVtable();
            for (Label method : table.getMethodLabels()) {
                compiler.addInstruction(new LOAD(new LabelOperand(method), Register.R0));
                compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.stack.incGBIndex(), Register.GB)));
            }
        }
    }


    public void codeGenClasses(DecacCompiler compiler) {
        if (compiler.environmentType.OBJECT.getDefinition().isUsed()) {
            compiler.addLabel(compiler.labeller.create(compiler.environmentType.OBJECT.getDefinition()));
            compiler.addInstruction(new RTS());
        }
        for (AbstractDeclClass declClass : getList()) {
            declClass.codeGenClass(compiler);
        }
    }

}
