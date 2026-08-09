package fr.ensimag.deca.functional;

import java.io.File;
import java.nio.file.Path;

public class DecaTest {

    private final Path sourceFile;
    private final String name;
    private final ExpectedTermination expectedTermination;
    private String[] compilerArguments = new String[0];
    private String[] runtimeArguments = new String[0];
    private Input runtimeInput;
    private String expectedCompilerOutput;
    private String expectedRuntimeOutput;
    private boolean extensionsEnabled = false;

    public DecaTest(Path sourceFile, Path relativePath, ExpectedTermination expectedTermination) {
        this.sourceFile = sourceFile;
        this.name = relativePath.toString().replace(".deca", "");
        this.expectedTermination = expectedTermination;
    }

    public Path getSourceFile() {
        return this.sourceFile;
    }

    public Path getAssemblyFile() {
        return this.sourceFile.resolveSibling(this.sourceFile.getFileName().toString().replace(".deca", ".ass"));
    }

    public String getName() {
        return this.name;
    }

    public ExpectedTermination getExpectedTermination() {
        return this.expectedTermination;
    }

    public Input getRuntimeInput() {
        return this.runtimeInput;
    }

    public void setRuntimeInput(Input runtimeInput) {
        this.runtimeInput = runtimeInput;
    }

    public String[] getCompilerArguments() {
        return this.compilerArguments;
    }

    public void setCompilerArguments(String[] compilerArguments) {
        this.compilerArguments = compilerArguments;
    }

    public String[] getRuntimeArguments() {
        return this.runtimeArguments;
    }

    public void setRuntimeArguments(String[] runtimeArguments) {
        this.runtimeArguments = runtimeArguments;
    }

    public String getExpectedCompilerOutput() {
        return this.expectedCompilerOutput;
    }

    public void setExpectedCompilerOutput(String expectedCompilerOutput) {
        this.expectedCompilerOutput = expectedCompilerOutput;
    }

    public String getExpectedRuntimeOutput() {
        return this.expectedRuntimeOutput;
    }

    public void setExpectedRuntimeOutput(String expectedRuntimeOutput) {
        this.expectedRuntimeOutput = expectedRuntimeOutput;
    }

    public boolean areExtensionsEnabled() {
        return this.extensionsEnabled;
    }

    public void enableExtensions() {
        this.extensionsEnabled = true;
    }

    @Override
    public String toString() {
        return "DecaTest{" + this.name + "}";
    }

    public static class Input {
        private final Path path;

        public Input(Path path) {
            this.path = path;
        }

        public File asFile() {
            return this.path.toFile();
        }
    }
}
