package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;

public abstract class AbstractMethodBody extends Tree {

    abstract public void codeGenMethod(DecacCompiler compiler, MethodDefinition methodDefinition);
    abstract protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError;
}
