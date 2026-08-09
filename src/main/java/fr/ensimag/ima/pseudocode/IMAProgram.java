package fr.ensimag.ima.pseudocode;

import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.HALT;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstract representation of an IMA program, i.e. set of Lines.
 *
 * @author Ensimag
 * @date 08/04/2025
 */
public class IMAProgram {
    private final LinkedList<AbstractLine> lines = new LinkedList<AbstractLine>();

    public void add(AbstractLine line) {
        lines.add(line);
    }

    public void addComment(String s) {
        lines.add(new Line(s));
    }

    public void addLabel(Label l) {
        lines.add(new Line(l));
    }

    public void addInstruction(Instruction i) {
        lines.add(new Line(i));
    }

    public void addInstruction(Instruction i, String s) {
        lines.add(new Line(null, i, s));
    }

    public void addInstructionAtIndex(Instruction i, int index, int offset) {
        lines.add(index + offset, new Line(i));
    }

    public int getMarker() {
        return lines.size();
    }

    /**
     * Append the content of program p to the current program. The new program
     * and p may or may not share content with this program, so p should not be
     * used anymore after calling this function.
     */
    public void append(IMAProgram p) {
        lines.addAll(p.lines);
    }
    
    /**
     * Add a line at the front of the program.
     */
    public void addFirst(Line l) {
        lines.addFirst(l);
    }

    /**
     * Display the program in a textual form readable by IMA to stream s.
     */
    public void display(PrintStream s) {
        for (AbstractLine l: lines) {
            l.display(s);
        }
    }

    /**
     * Return the program in a textual form readable by IMA as a String.
     */
    public String display() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream s = new PrintStream(out);
        display(s);
        return out.toString();
    }

    public void addFirst(Instruction i) {
        addFirst(new Line(i));
    }
    
    public void addFirst(Instruction i, String comment) {
        addFirst(new Line(null, i, comment));
    }

    public List<AbstractLine> getLines() {
        return this.lines;
    }

    public void addAll(List<AbstractLine> instructions) {
        this.lines.addAll(instructions);
    }

    public void replaceAllErrors() {
        for (AbstractLine line : lines) {
            if (line instanceof Line) {
                if (((Line) line).getInstruction() instanceof ERROR) {
                    ((Line) line).setInstruction(new HALT());
                }
            }
        }
    }
}
