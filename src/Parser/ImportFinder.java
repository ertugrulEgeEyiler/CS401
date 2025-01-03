package Parser;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ImportFinder {

    public void createImports(String directory, PrintWriter printWriter){
        try {
            findAndWriteImports(directory, printWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void findAndWriteImports(String directory, PrintWriter writer) throws IOException {
        Files.walk(Paths.get(directory))
                .filter(Files::isRegularFile)
                .filter(file -> file.toString().endsWith(".java"))
                .forEach(file -> {
                    try {
                        writeImportsToFile(file, writer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    public static void writeImportsToFile(Path filePath, PrintWriter writer) throws IOException {
        String fileName = filePath.getFileName().toString();
        writer.println(fileName + " imports:");
        Set<String> imports = getImportsFromFile(filePath.toString());
        for (String imp : imports) {
            writer.println("\t" + imp);
        }
        writer.println();
    }

    public static Set<String> getImportsFromFile(String filePath) throws IOException {
        Set<String> imports = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("import ")) {
                    String importStatement = line.trim().replace("import", "").replace(";", "").trim();
                    imports.add(importStatement);
                }
            }
        }
        return imports;
    }

}

