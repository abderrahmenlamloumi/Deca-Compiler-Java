package fr.ensimag.deca.functional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DecaTestScanner {

    private static final Path TEST_DIR = Paths.get("src", "test", "deca");
    private static final String TEST_EXTENSION = ".deca";
    private static final String INPUT_EXTENSION = ".in";
    private static final String EXPECTED_EXTENSION = ".expected";
    private static final String SYNTAX_DIR = "syntax";
    private static final String CONTEXT_DIR = "context";
    private static final String CODEGEN_DIR = "codegen";
    private static final String INTERACTIVE_DIR = "interactive";
    private static final String EXTENSION_DIR = "extension";
    private static final String PERF_DIR = "perf";
    private static final String PROVIDED_DIR = "provided";

    private static final String VALID_DIR = "valid";
    private static final String INVALID_DIR = "invalid";

    private DecaTestScanner() {}

    public static List<DecaTest> discoverTests() throws IOException {
        try (Stream<Path> sourceFiles = Files.walk(TEST_DIR)) {
            return sourceFiles
                    .filter(path -> {
                        if (!path.getFileName().toString().endsWith(TEST_EXTENSION)) {
                            return false;
                        }
                        Path relativePath = TEST_DIR.relativize(path);
                        return !relativePath.getName(1).startsWith(PERF_DIR)
                                && (relativePath.getNameCount() > 1 && !relativePath.getName(2).startsWith(PROVIDED_DIR));
                    })
                    .map(DecaTestScanner::createDecaTest)
                    .collect(Collectors.toList());
        }
    }

    private static DecaTest createDecaTest(Path sourceFile) {
        Path relativePath = TEST_DIR.relativize(sourceFile);
        ExpectedTermination expectedTermination = determineExpectedOutput(relativePath);
        DecaTest test = new DecaTest(sourceFile, relativePath, expectedTermination);
        Path expectedFile = sourceFile.resolveSibling(
                sourceFile.getFileName().toString().replace(TEST_EXTENSION, EXPECTED_EXTENSION));
        try {
            String expectedOutput = new String(Files.readAllBytes(expectedFile));
            if (expectedTermination.doesAssertRuntime()) {
                test.setExpectedRuntimeOutput(expectedOutput);
            } else {
                test.setExpectedCompilerOutput(expectedOutput);
            }

            Path name = relativePath.getName(1);
            if (name.startsWith(INTERACTIVE_DIR) || name.startsWith(EXTENSION_DIR)) {
                PrologueParser.parseTestHeader(test);
                Path inputFile = sourceFile.resolveSibling(
                        sourceFile.getFileName().toString().replace(TEST_EXTENSION, INPUT_EXTENSION));
                if (Files.exists(inputFile)) {
                    test.setRuntimeInput(new DecaTest.Input(inputFile));
                }
            }
            if (name.startsWith(EXTENSION_DIR)) {
                test.enableExtensions();
            }
        } catch (NoSuchFileException ignored) {
            if (test.getExpectedTermination() == ExpectedTermination.PARSE_SUCCESS
                    || test.getExpectedTermination() == ExpectedTermination.CONTEXT_SUCCESS) {
                // Default to empty output for parse and context tests
                test.setExpectedCompilerOutput("");
            }
            // Will report missing expected output later for other test types
        } catch (IOException ex) {
            throw new RuntimeException("Failed to read expected output for test: " + sourceFile, ex);
        }
        return test;
    }

    private static ExpectedTermination determineExpectedOutput(Path relativePath) {
        if (relativePath.startsWith(SYNTAX_DIR)) {
            return isValidTest(relativePath)
                    ? ExpectedTermination.PARSE_SUCCESS
                    : ExpectedTermination.PARSE_ERROR;
        } else if (relativePath.startsWith(CONTEXT_DIR)) {
            return isValidTest(relativePath)
                    ? ExpectedTermination.CONTEXT_SUCCESS
                    : ExpectedTermination.CONTEXT_ERROR;
        } else if (relativePath.startsWith(CODEGEN_DIR)) {
            return isValidTest(relativePath)
                    ? ExpectedTermination.VALID
                    : ExpectedTermination.RUNTIME_ERROR;
        } else {
            throw new IllegalArgumentException("Unknown test type for path: " + relativePath +
                    ". Expected to start with one of: " + SYNTAX_DIR + ", " + CONTEXT_DIR + ", " + CODEGEN_DIR);
        }
    }

    private static boolean isValidTest(Path relativePath) {
        Path name = relativePath.getName(1);
        if (name.startsWith(INTERACTIVE_DIR) || name.startsWith(EXTENSION_DIR)) {
            name = relativePath.getName(2); // Skip the "interactive" or "extension" directory
        }
        if (name.startsWith(VALID_DIR)) {
            return true;
        } else if (name.startsWith(INVALID_DIR)) {
            return false;
        } else {
            throw new IllegalArgumentException("Unknown test validity for path: " + relativePath +
                    ". Expected to start with one of: " + VALID_DIR + ", " + INVALID_DIR);
        }
    }
}
