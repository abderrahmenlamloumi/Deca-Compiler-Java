package fr.ensimag.deca.functional;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PrologueParser {

    private static final String COMPILER_OPTIONS_KEYWORD = "compiler-options";
    private static final String RUNTIME_OPTIONS_KEYWORD = "runtime-options";

    private String content;
    private int columnIndex;

    private PrologueParser() {}

    public static void parseTestHeader(DecaTest test) throws IOException {
        try (Stream<String> lines = Files.lines(test.getSourceFile())) {
            List<String> prologue = new ArrayList<>();
            Iterable<String> iterable = lines::iterator;
            for (String line : iterable) {
                if (!line.startsWith("//")) {
                    break;
                }
                prologue.add(line.substring(2));
            }
            PrologueParser parser = new PrologueParser();
            parser.parsePrologue(test, prologue);
        }
    }

    private void parsePrologue(DecaTest test, List<String> lines) {
        for (String line : lines) {
            int indent = indentLevel(line);
            this.content = line.substring(indent);

            String keyword = parseKeyword();
            switch (keyword) {
                case COMPILER_OPTIONS_KEYWORD:
                    expectColon();
                    String options = parseValue();
                    test.setCompilerArguments(options.split(" "));
                    break;
                case RUNTIME_OPTIONS_KEYWORD:
                    expectColon();
                    String runtimeOptions = parseValue();
                    test.setRuntimeArguments(runtimeOptions.split(" "));
                    break;
            }
        }
    }

    private String parseKeyword() {
        for (int i = 0; i < this.content.length(); i++) {
            char c = this.content.charAt(i);
            if (c == ' ' || c == ':') {
                this.columnIndex = i;
                return this.content.substring(0, i);
            }
        }
        return this.content;
    }

    private void expectColon() {
        if (this.columnIndex >= this.content.length() || this.content.charAt(this.columnIndex) != ':') {
            throw new IllegalArgumentException("Expected ':' at column " + this.columnIndex + " in line: " + this.content);
        }
        this.columnIndex++;
    }

    private String parseValue() {
        int start = this.columnIndex;
        while (start < this.content.length() && this.content.charAt(start) == ' ') {
            start++;
        }
        return this.content.substring(start).trim();
    }

    private static int indentLevel(String line) {
        int indent = 0;
        while (indent < line.length() && line.charAt(indent) == ' ') {
            indent++;
        }
        return indent;
    }
}
