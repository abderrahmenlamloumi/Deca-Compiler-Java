package fr.ensimag.deca.functional;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import org.junit.jupiter.api.AssertionFailureBuilder;
import org.junit.jupiter.api.function.Executable;
import org.opentest4j.TestAbortedException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class DecaTestExecutable implements Executable {

    private static final int MAX_EXECUTION_CYCLES = 1000000;

    private final DecaTest test;

    public DecaTestExecutable(DecaTest test) {
        this.test = test;
    }

    @Override
    public void execute() throws Throwable {
        CompilerOptions options = createOptions();
        options.parseArgs(this.test.getCompilerArguments());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        DecacCompiler compiler = new DecacCompiler(options, this.test.getSourceFile().toFile());
        boolean hasError = compiler.doCompile(
                new PrintStream(new DummyOutputStream()), // we don't care about the decompilation result for now
                printStream
        );
        if (this.test.getExpectedTermination().doesAssertRuntime()) {
            assertEquals("", outputStream.toString(),
                    "Compiler should not have printed anything");
            assertFalse(hasError,
                    "Compiler should not have reported an error");

            List<String> command = new ArrayList<>();
            command.add("ima");
            command.addAll(Arrays.asList(this.test.getRuntimeArguments()));
            command.addAll(Arrays.asList(
                    "-T", Integer.toString(MAX_EXECUTION_CYCLES),
                    this.test.getAssemblyFile().toString()));
            ProcessBuilder builder = new ProcessBuilder(command);
            DecaTest.Input input = this.test.getRuntimeInput();
            if (input != null) {
                builder.redirectInput(input.asFile());
            }
            builder.redirectErrorStream(true);
            Process process = builder.start();
            hasError = process.waitFor() != 0;
            StringBuilder stdout = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    stdout.append(line).append("\n");
                }
            };
            String expectedOutput = verifyOutput(this.test.getExpectedRuntimeOutput());
            assertEquals(expectedOutput, stdout.toString(),
                    "Unexpected runtime output");
            assertEquals(this.test.getExpectedTermination().isError(), hasError,
                    "Unexpected termination");
        } else {
            String expectedOutput = verifyOutput(this.test.getExpectedCompilerOutput()).trim();
            assertContains(expectedOutput, outputStream.toString(),
                    "Unexpected compiler output");
            assertEquals(this.test.getExpectedTermination().isError(), hasError,
                    "Unexpected termination");
        }
    }

    private static String verifyOutput(String expectedOutput) {
        if (expectedOutput == null) {
            throw new TestAbortedException("No expected output is defined.");
        }
        return expectedOutput;
    }

    private static void assertContains(String expected, String actual, String message) {
        if (!actual.contains(expected)) {
            AssertionFailureBuilder.assertionFailure().message(message).expected(expected).actual(actual).buildAndThrow();
        }
    }

    private CompilerOptions createOptions() {
        CompilerOptions options = new CompilerOptions();
        if (this.test.areExtensionsEnabled()) {
            options = options.allExtensions();
        }
        switch (this.test.getExpectedTermination()) {
            case PARSE_ERROR:
            case PARSE_SUCCESS:
                return options.parseOnly();
            case CONTEXT_ERROR:
            case CONTEXT_SUCCESS:
                return options.verifyOnly();
            default:
                return options;
        }
    }

    private static class DummyOutputStream extends OutputStream {

        @Override
        public void write(int i) {
            // No-op
        }
    }
}
