package fr.ensimag.deca.fuzz;

import com.code_intelligence.jazzer.junit.FuzzTest;
import com.code_intelligence.jazzer.mutation.annotation.NotNull;
import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tree.AbstractProgram;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CompileFuzzTest {

    @FuzzTest
    public void compile(@NotNull String input) {
        DecacCompiler compiler = new DecacCompiler(new CompilerOptions(), null);
        AbstractProgram program = parseProgram(compiler, input);
        if (program != null) {
            String decompiled1 = program.decompile();
            AbstractProgram program2 = parseProgram(new DecacCompiler(new CompilerOptions(), null), decompiled1);
            assertNotNull(program2, "Decompiled program should be parsable");
            assertEquals(decompiled1, program2.decompile());

            boolean isValid = true;
            try {
                program.verifyProgram(compiler);
            } catch (ContextualError e) {
                isValid = false;
            }
            if (isValid) {
                program.codeGenProgram(compiler);
                compiler.procedures.codeGen(compiler);
                compiler.getProgram().replaceAllErrors();
                try {
                    Path path = Files.createTempFile("decac", ".ass");
                    try (PrintStream out = new PrintStream(Files.newOutputStream(path))) {
                        compiler.getProgram().display(out);
                    } catch (IOException e) {
                        Files.delete(path);
                        throw e;
                    }
                    ProcessBuilder builder = new ProcessBuilder("ima", "-T", "2000", path.toAbsolutePath().toString());
                    Process process = builder.start();
                    List<String> offendingLines = new ArrayList<>();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (line.startsWith("  ** IMA ** ERREUR **") || line.startsWith("ERREUR ligne")) {
                                offendingLines.add(line);
                                break;
                            }
                        }
                    } finally {
                        Files.delete(path);
                    }
                    if (process.waitFor() != 0) {
                        if (offendingLines.isEmpty()) {
                            throw new RuntimeException("IMA execution failed: " + offendingLines);
                        }
                    }
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    private static AbstractProgram parseProgram(DecacCompiler compiler, String code) {
        DecaLexer lex = new DecaLexer(CharStreams.fromString(code));
        lex.setDecacCompiler(compiler);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaParser parser = new DecaParser(tokens);
        parser.setDecacCompiler(compiler);
        return parser.parseProgramAndManageErrors(new PrintStream(new OutputStream() {
            @Override
            public void write(int i) {

            }
        }));
    }
}
