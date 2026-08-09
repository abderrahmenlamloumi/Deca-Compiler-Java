package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.apache.log4j.Logger;

/**
 * Base class for any node in the Deca abstract syntax tree.
 *
 * Factors all the common elements and utility functions to manipulate trees
 * (location in source-code, pretty-printing, ...).
 *
 * @author gl10
 * @date 08/04/2025
 *
 */
public abstract class Tree implements Locatable, PrintableTree {

    private static final Logger LOG = Logger.getLogger(Main.class);

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    private Location location;

    /**
     * Display the tree as a (compilable) source program
     *
     * @param s Buffer to which the result will be written.
     */
    public abstract void decompile(IndentPrintStream s);

    public void decompile(PrintStream s) {
        decompile(new IndentPrintStream(s));
    }

    /**
     * Display the tree as a (compilable) source program
     */
    public String decompile() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream s = new PrintStream(out);
        decompile(s);
        return out.toString();
    }

    @Override
    public String  prettyPrintNode() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void prettyPrintType(PrintStream s, String prefix) {
        // Nothing by default
    }

    /**
     * Call function f on each node of the tree.
     *
     * @param f
     */
    public void iter(TreeFunction f) {
        f.apply(this);
        iterChildren(f);
    }

    /**
     * Function used internally by {@link #iter(TreeFunction)}. Must call iter() on each
     * child of the tree.
     *
     * @param f
     */
    protected abstract void iterChildren(TreeFunction f);

    /**
     * Check that the current node has correctly been decorated, and throws an
     * error if not.
     *
     * This is used only for defensive programming, each node type can add
     * checks by overloading this method. Does nothing by default.
     *
     * The method is called automatically by {@link #checkAllDecorations()}.
     */
    protected void checkDecoration() {
        // Nothing by default. Override to add checks for specific nodes.
    }

    /**
     * Check that all nodes of the tree have been correctly decorated.
     *
     * Useful for debugging/defensive programming.
     *
     * @return true. Raises an exception in case of error. The return value is
     * meant to allow assert(tree.checkAllLocations()), to enable the defensive
     * check only if assertions are enabled.
     */
    public boolean checkAllDecorations() {
        iter(new TreeFunction() {
            @Override
            public void apply(Tree t) {
                t.checkDecoration();
            }
        });
        return true;
    }

    /**
     * Check that the location has been correctly set for this tree.
     *
     * By default, this checks that getLocation() does not return null, but can
     * be overridden for particular classes that do not require location
     * information.
     */
    protected void checkLocation() {
        if (getLocation() == null) {
            LOG.info(prettyPrint());
            throw new DecacInternalError("Tree "
                    + getClass().getName()
                    + " has no location set");
        }
    }

    /**
     * Check that all nodes of the tree have a location correctly set.
     *
     * Useful for debugging/defensive programming.
     *
     * @return true. Raises an exception in case of error. The return value is
     * meant to allow assert(tree.checkAllLocations()), to enable the defensive
     * check only if assertions are enabled.
     */
    public boolean checkAllLocations() {
        iter(new TreeFunction() {
            @Override
            public void apply(Tree t) {
                t.checkLocation();
            }
        });
        return true;
    }

    /**
     * Call decompile() if the compiler has a debug level greater than 1.
     * 
     * Useful for debugging.
     * 
     * @param compiler
     * @return Decompilation, or the empty string.
     */
    protected String decompileIfDebug(DecacCompiler compiler) {
        if (compiler.getCompilerOptions().getDebug() > 1) {
            return decompile();
        } else {
            return "";
        }
    }
}
