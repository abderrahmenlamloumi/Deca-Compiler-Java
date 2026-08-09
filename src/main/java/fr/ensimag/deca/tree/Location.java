package fr.ensimag.deca.tree;

import java.io.Serializable;

/**
 * Location in a file (File, line, positionInLine).
 *
 * @author gl10
 * @date 08/04/2025
 */
public class Location implements Serializable {
    /*
     * Location implements Serializable because it appears as a field
     * of LocationException, which is serializable.
     */
    private static final long serialVersionUID = -2906437663480660298L;

    public static final String NO_SOURCE_NAME = "<no source file>";
    public static final Location BUILTIN = new Location(-1, -1, -1, -1,NO_SOURCE_NAME);

    /**
     * Display the (line, positionInLine) as a String. The file is not
     * displayed.
     */
    @Override
    public String toString() {
        if (this == BUILTIN) {
            return "[builtin]";
        } else {
            return "[" + line + ", " + positionInLine + "]";
        }
    }

    public int getLine() {
        return line;
    }

    public int getEndLine() {
        return endLine;
    }

    public int getPositionInLine() {
        return positionInLine;
    }

    public int getEndPositionInLine() {
        return  endPositionInLine;
    }

    public String getFilename() {
        if (filename != null) {
            return filename;
        } else {
            // we're probably reading from stdin
            return NO_SOURCE_NAME;
        }
    }

    private final int line;
    private final int endLine;
    private final int positionInLine;
    private final int endPositionInLine;
    private final String filename;

    public Location(int line, int endLine, int positionInLine, int endPositionInLine, String filename) {
        super();
        this.line = line;
        this.endLine = endLine;
        this.positionInLine = positionInLine;
        this.endPositionInLine = endPositionInLine;
        this.filename = filename;
    }

}
