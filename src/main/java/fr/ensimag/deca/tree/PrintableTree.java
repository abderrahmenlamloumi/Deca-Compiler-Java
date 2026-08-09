package fr.ensimag.deca.tree;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public interface PrintableTree extends Locatable {
    /**
     * wrapper for
     * {@link #printNodeLine(PrintStream, String, boolean, boolean, String)},
     * calling {@link #prettyPrintNode()} to display the node element.
     *
     * @param s
     * @param prefix
     * @param last
     * @param inlist
     * @return The prefix to use for the next recursive calls to
     *         {@link #prettyPrint()}.
     */
    default String printNodeLine(PrintStream s, String prefix, boolean last,
                                   boolean inlist) {
        return printNodeLine(s, prefix, last, inlist, prettyPrintNode());
    }

    /**
     * Print the line corresponding to the current node.
     *
     * This displays the prefix (to show the tree hierarchy in ASCII-art), and
     * the node name and information.
     *
     * @param s
     * @param prefix
     * @param last
     * @param inlist
     * @param nodeName
     * @return The prefix to use for the next recursive calls to
     * {@link #prettyPrint()}.
     */
    default String printNodeLine(PrintStream s, String prefix, boolean last,
                                boolean inlist, String nodeName) {
        s.print(prefix);
        if (inlist) {
            s.print("[]>");
        } else if (last) {
            s.print("`>");
        } else {
            s.print("+>");
        }
        if (getLocation() != null) {
            s.print(" " + getLocation().toString());
        }
        s.print(" ");
        s.print(nodeName);
        s.println();
        String newPrefix;
        if (last) {
            if (inlist) {
                newPrefix = prefix + "    ";
            } else {
                newPrefix = prefix + "   ";
            }
        } else {
            if (inlist) {
                newPrefix = prefix + "||  ";
            } else {
                newPrefix = prefix + "|  ";
            }
        }
        prettyPrintType(s, newPrefix);
        return newPrefix;
    }

    /**
     * Pretty-print the type of the tree, if applicable
     */
    void prettyPrintType(PrintStream s, String prefix);

    /**
     * Pretty-print the definition of the tree, if applicable
     */
    default void prettyPrintDefinition(PrintStream s, String newPrefix) {
        // Nothing by default
    }

    /**
     * Print the node information on a single line.
     *
     * Does not print the children (the recursive call is done by prettyPrint).
     */
    String  prettyPrintNode();

    /**
     * Pretty-print tree (see {@link #prettyPrint()}), sending output to
     * PrintStream s.
     *
     * @param s
     */
    default void prettyPrint(PrintStream s) {
        prettyPrint(s, "", true, false);
    }

    /**
     * Pretty-print the tree as a String, using ASCII-art to show the tree
     * hierarchy. Useful for debugging.
     */
    default String prettyPrint() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream s = new PrintStream(out);
        prettyPrint(s);
        return out.toString();
    }

    default void prettyPrint(PrintStream s, String prefix,
                               boolean last) {
        prettyPrint(s, prefix, last, false);
    }

    /**
     * Pretty-print tree (see {@link #prettyPrint()}). This is an internal
     * function that should usually not be called directly.
     *
     * @param s Stream to send the output to
     * @param prefix Prefix (ASCII-art showing hierarchy) to print for this
     * node.
     * @param last Whether the node being displayed is the last child of a tree.
     * @param inlist Whether the node is being displayed as part of a list.
     */
    default void prettyPrint(PrintStream s, String prefix,
                                     boolean last, boolean inlist) {
        String next = printNodeLine(s, prefix, last, inlist);
        prettyPrintChildren(s, next);
    }

    /**
     * Used internally by {@link #prettyPrint}. Must call prettyPrint() on each
     * children.
     *
     * @param s
     * @param prefix
     */
    void prettyPrintChildren(PrintStream s, String prefix);

    void iter(TreeFunction f);
}
