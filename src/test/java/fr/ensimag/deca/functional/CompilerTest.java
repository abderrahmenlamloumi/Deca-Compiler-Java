package fr.ensimag.deca.functional;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.IOException;
import java.util.stream.Stream;

/**
 * Entrypoint for functional tests of the Deca compiler.
 *
 * <p>All {@code .deca} tests placed in a subdirectory of {@code src/test/deca} are
 * discovered by the {@link DecaTestScanner scanner} and are categorized
 * automatically based on their directory structure:</p>
 * <dl>
 *     <dt>syntax</dt>
 *     <dd>Tests that only invoke the parser.</dd>
 *     <dt>context</dt>
 *     <dd>Tests that invoke the parser and the context analysis.</dd>
 *     <dt>codegen</dt>
 *     <dd>Tests that are expected to generate assembly code and run it.</dd>
 * </dl>
 *
 * <p>The immediate subdirectory after the test type should be either:</p>
 * <dl>
 *     <dt>valid</dt>
 *     <dd>Tests that are expected to pass.</dd>
 *     <dt>invalid</dt>
 *     <dd>Tests that are expected to fail.</dd>
 *     <dt>interactive</dt>
 *     <dd>Tests that cannot be run without any specific input or compiler arguments.
 *     Most tests should not be placed here, as they will not be executed by the professor
 *     tests automatically. Using those tests do, however, unlock additional testing
 *     features. The next directory name should then be {@code valid} or {@code invalid}.</dd>
 *     <dt>perf</dt>
 *     <dd>Ignored tests in the automatic tests, but can be run manually.</dd>
 * </dl>
 *
 * <p>The tester will automatically run the Deca compiler on each source file,
 * and compare the output with the expected output. The expected output should
 * be placed in a file with the same name as the source file, but with the
 * {@code .expected} extension instead of {@code .deca}.</p>
 *
 * <p>Interactive tests can also provide an input file with the same name as the source file,
 * but with the {@code .in} extension. Thet may also provide additional compiler arguments
 * or runtime arguments in the prologue of the source file, using the following syntax:</p>
 * <pre>
 *     // compiler-options: -option1 -option2
 *     // runtime-options: -option1 -option2
 * </pre>
 */
public class CompilerTest {

    @TestFactory
    public Stream<DynamicTest> tests() throws IOException {
        return DecaTestScanner.discoverTests().stream()
                .map(test -> DynamicTest.dynamicTest(
                        test.getName(),
                        test.getSourceFile().toUri(),
                        new DecaTestExecutable(test)));
    }
}
