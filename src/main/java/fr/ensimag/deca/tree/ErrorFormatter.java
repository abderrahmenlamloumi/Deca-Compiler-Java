package fr.ensimag.deca.tree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ErrorFormatter {

    private String filePath;

    public ErrorFormatter(String filePath) {
        this.filePath = filePath;
    }

    String getErrorLine(int lineIndex) throws IOException {
        return Files.readAllLines(Paths.get(filePath)).get(lineIndex-1);
    }
}
