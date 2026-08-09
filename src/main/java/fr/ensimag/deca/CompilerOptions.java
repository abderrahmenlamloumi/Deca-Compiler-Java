package fr.ensimag.deca;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import fr.ensimag.deca.feature.FeatureFlag;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl10
 * @date 08/04/2025
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;
    public int getDebug() {
        return debug;
    }

    public boolean doesParseOnly() {
        return parseOnly;
    }

    public boolean doesVerifyOnly() {
        return verifyOnly;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }

    public boolean doesNoCheck(){
        return noCheck;
    }
    
    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    public int getAvailableRegisters() {
        return availableRegisters;
    }

    public boolean isEnabled(FeatureFlag flag) {
        return enabledFlags.contains(flag);
    }

    private int debug = 0;
    private boolean parseOnly = false;
    private boolean verifyOnly = false;
    private int availableRegisters = 16;
    private boolean parallel = false;
    private boolean printBanner = false;
    private boolean noCheck = false;
    private List<File> sourceFiles = new ArrayList<File>();
    private Set<FeatureFlag> enabledFlags = EnumSet.noneOf(FeatureFlag.class);

    
    public void parseArgs(String[] args) throws CLIException {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("-d")) {
                debug = DEBUG;
            } else if (arg.equals("-p")) {
                parseOnly = true;
            } else if (arg.equals("-v")) {
                verifyOnly = true;
            } else if (arg.equals("-P")) {
                parallel = true;
            } else if (arg.equals("-b")) {
                printBanner = true;
            } else if (arg.equals("-r")) {
                i++;
                if (i >= args.length) {
                    throw new CLIException("Missing argument for -r option");
                }
                try {
                    this.availableRegisters = Integer.parseInt(args[i]);
                    if (this.availableRegisters < 4 || this.availableRegisters > 16) {
                        throw new CLIException("Invalid value for -r option: " + args[i]);
                    }
                } catch (NumberFormatException e) {
                    throw new CLIException("Invalid value for -r option: " + args[i]);
                }
            } else if (arg.equals("-n")) {
                noCheck = true;
            } else if (arg.startsWith("-f")) {
                arg = arg.substring(2);
                if (arg.isEmpty()) {
                    this.enabledFlags = EnumSet.allOf(FeatureFlag.class);
                    continue;
                }
                boolean found = false;
                for (FeatureFlag flag : FeatureFlag.values()) {
                    if (flag.getShortName().equals(arg)) {
                        enabledFlags.add(flag);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    throw new CLIException("Unknown feature flag: " + arg);
                }
            } else if (arg.startsWith("-")) {
                throw new CLIException("Unknown option: " + arg);
            } else {
                sourceFiles.add(new File(arg));
            }
        }
        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
        case QUIET: break; // keep default
        case INFO:
            logger.setLevel(Level.INFO); break;
        case DEBUG:
            logger.setLevel(Level.DEBUG); break;
        case TRACE:
            logger.setLevel(Level.TRACE); break;
        default:
            logger.setLevel(Level.ALL); break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }
    }

    public CompilerOptions withRegisters(int availableRegisters) {
        Validate.isTrue(availableRegisters >= 4 && availableRegisters <= 16,
                "available registers must be between 4 and 16");
        this.availableRegisters = availableRegisters;
        return this;
    }

    public CompilerOptions parseOnly() {
        this.parseOnly = true;
        return this;
    }

    public CompilerOptions verifyOnly() {
        this.verifyOnly = true;
        return this;
    }

    public CompilerOptions allExtensions() {
        this.enabledFlags = EnumSet.allOf(FeatureFlag.class);
        return this;
    }

    protected static void displayUsage() {
        System.out.println("usage: decac [[-p | -v] [-n] [-r X] [-f<features>] <deca file>...] | [-b]");
        System.out.println("\n -b  (banner): displays a banner with the team's name");
        System.out.println("\n -p  (parse): stops decac after the tree construction stage\n" +
                "and displays its decompilation\n" +
                "(i.e., if there is only one source file to\n" +
                "compile, the output should be a syntactically\n" +
                "correct deca program)");
        System.out.println("\n -v  (verification): stops decac after the verification stage\n" +
                "(produces no output if there are no errors)");
        System.out.println("\n -n  (no check): disables runtime overflow checks\n" +
                "- arithmetic overflow\n" +
                "- memory overflow\n" +
                "- null dereference");
        System.out.println("\n -r X (registers): limits the available general-purpose registers to\n" +
                "R0 ... R{X-1}, with 4 <= X <= 16");
        System.out.println("\n -d  (debug): enables debug traces. Repeat\n" +
                "the option multiple times for more\n" +
                "detailed traces.");
        System.out.println("\n -P  (parallel): if there are multiple source files,\n" +
                "compiles them in parallel (to speed up compilation)");
        System.out.println("\n -w (warning): allows the display of warning messages\n" +
                "during compilation.");
        System.out.println("\n -f (features): allows every single features/extensions\n" +
                "during compilation.");
        //options for extensions
        for (FeatureFlag flag : FeatureFlag.values()) {
            System.out.println("\n -f"+flag.getShortName ()+ ": "+ flag.getDescription() +
                    " during compilation.");
        }
    }
}
