package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.VariableDefinition;

/**
 * Variable declaration
 *
 * @author gl10
 * @date 08/04/2025
 */
public abstract class AbstractDeclVar extends Tree {
    
    /**
     * Implements non-terminal "decl_var" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains "env_types" attribute
     * @param localEnv 
     *   its "parentEnvironment" corresponds to the "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to 
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to 
     *      the synthetized attribute
     * @param currentClass 
     *          corresponds to the "class" attribute (null in the main bloc).
     */    
    protected abstract void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Generate the appropriate code for the initializer.
     *
     * The variable position in memory should be defined before calling this method.
     *
     * @param compiler the compiler.
     */
    public abstract void codeGenDecl(DecacCompiler compiler);

    /**
     * Get the variable definition.
     *
     * It should be defined after the pass 3.
     *
     * @return the definition, or {@code null} if the instruction has not been checked yet.
     */
    protected abstract VariableDefinition getDefinition();
}
