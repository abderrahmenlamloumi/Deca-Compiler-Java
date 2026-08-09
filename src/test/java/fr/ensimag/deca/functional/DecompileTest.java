package fr.ensimag.deca.functional;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.Tree;
import fr.ensimag.deca.tree.TreeFunction;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DecompileTest {

    /**
     * Use all {@link CompilerTest} tests to test decompilation.
     *
     * <p>A program is parsed, decompiled, and then reparsed. It should return the same
     * nodes as the original program, and decompilation should be idempotent.</p>
     *
     * <p>Note that node comparison test depends on the fact that the {@code Tree#iterChildren(TreeFunction)}
     * method is correctly implemented for all nodes.</p>
     */
    @TestFactory
    public Stream<DynamicTest> reparse() throws IOException {
        return DecaTestScanner.discoverTests().stream()
                .filter(test -> test.getExpectedTermination() != ExpectedTermination.PARSE_ERROR)
                .map(test -> {
                    String name = test.getName() + "/decompile";
                    return DynamicTest.dynamicTest(name, test.getSourceFile().toUri(), () -> {
                        AbstractProgram prog = parse(test.getSourceFile(), CharStreams.fromPath(test.getSourceFile()));
                        assertNotNull(prog, "Parsing failed");
                        List<Class<? extends Tree>> nodes = NodeCollector.collect(prog);

                        String decompiled = prog.decompile();
                        AbstractProgram reParsedProg = parse(test.getSourceFile(), CharStreams.fromString(decompiled));
                        assertNotNull(reParsedProg, "Re-parsing failed");
                        List<Class<? extends Tree>> reNodes = NodeCollector.collect(reParsedProg);
                        assertEquals(nodes, reNodes, "Re-parsed nodes should match original nodes");

                        String reDecompiled = reParsedProg.decompile();
                        AbstractProgram reReParsedProg = parse(test.getSourceFile(), CharStreams.fromString(reDecompiled));
                        assertNotNull(reReParsedProg, "Re-re-parsing failed");
                        String reReDecompiled = reReParsedProg.decompile();
                        assertEquals(reDecompiled, reReDecompiled, "Decompilation should be idempotent");

                        assertNotNull(prog.prettyPrint());
                    });
                });
    }

    private static AbstractProgram parse(Path source, CharStream stream) {
        DecaLexer lex = new DecaLexer(stream);
        lex.setSource(source.toFile());
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaParser parser = new DecaParser(tokens);
        DecacCompiler decacCompiler = new DecacCompiler(new CompilerOptions(), source.toFile());
        parser.setDecacCompiler(decacCompiler);
        return parser.parseProgramAndManageErrors(System.err);
    }

    private static class NodeCollector implements TreeFunction {

        private final List<Class<? extends Tree>> nodes = new ArrayList<>();

        private static List<Class<? extends Tree>> collect(Tree t) {
            NodeCollector collector = new NodeCollector();
            t.iter(collector);
            return collector.nodes;
        }

        @Override
        public void apply(Tree t) {
            this.nodes.add(t.getClass());
        }
    }
}
