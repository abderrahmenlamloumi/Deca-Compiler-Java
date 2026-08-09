package fr.ensimag.deca.tree;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.feature.FeatureFlag;

import java.io.IOException;
import java.io.PrintStream;

/**
 * Exception corresponding to an error at a particular location in a file.
 *
 * @author gl10
 * @date 08/04/2025
 */
public class LocationException extends Exception {
    public Location getLocation() {
        return location;
    }

    public void display(PrintStream s) {
        Location loc = getLocation();
        String line;
        String column;
        if (loc == null) {
            line = "<unknown>";
            column = "";
        } else {
            line = Integer.toString(loc.getLine());
            column = ":" + loc.getPositionInLine();
        }
        s.println(location.getFilename() + ":" + line + column + ": " + getMessage());
    }

    public void displayFancy(CompilerOptions options, PrintStream s) {
        display(s);
        if (!options.isEnabled(FeatureFlag.FANCY_ERRORS)) {
            return;
        }
        Location loc = getLocation();
        ErrorFormatter errorFormatter = new ErrorFormatter(location.getFilename());
        try {
            assert loc != null;
            String errorLine = errorFormatter.getErrorLine(loc.getLine());
            int end = loc.getLine() == loc.getEndLine() ? loc.getEndPositionInLine() : errorLine.length();
            s.println(errorLine.substring(0, loc.getPositionInLine()) + "\u001B[31m" +
                    errorLine.substring(loc.getPositionInLine(), end)
                    + "\u001B[0m" + errorLine.substring(end));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final long serialVersionUID = 7628400022855935597L;
    protected Location location;

    public LocationException(String message, Location location) {
        super(message);
        assert(location == null || location.getFilename() != null);
        this.location = location;
    }

}
