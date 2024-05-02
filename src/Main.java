import Clusterer.ImportClusterer;
import Parser.ImportFinder;
import Test.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {

        ImportFinder importFinder = new ImportFinder();
        ImportClusterer importClusterer = new ImportClusterer();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the directory of your project.");
        String directory =  scanner.nextLine();
        String outputFile = "C:\\Users\\zeroc\\Desktop\\java\\output.txt";
        PrintWriter printWriter = new PrintWriter(outputFile);
        String clusteredFile = directory + "\\clustered.txt";

<<<<<<< Updated upstream
=======
        if (!dir.exists()) {
            System.out.println("Directory does not exist: " + directory);
            return;
        }

        if (!dir.isDirectory()) {
            System.out.println("This is not a directory: " + directory);
            return;
        }
        

        System.out.println("Directory is valid and exists: " + directory);
        String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
        String outputFile = desktopPath + File.separator + "output.txt";

        File outputFileParent = new File(outputFile).getParentFile();
        if (!outputFileParent.exists()) {
            outputFileParent.mkdirs();
            System.out.println("Created directory for output: " + outputFileParent.getPath());
        }

        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(outputFile);
        } catch (FileNotFoundException e) {
            System.err.println("Failed to create file at: " + outputFile);
            e.printStackTrace();
            return;
        }

        // Process import files
>>>>>>> Stashed changes
        importFinder.createImports(directory, printWriter);
        importClusterer.findClusters(outputFile, clusteredFile);

    }

    public void test() {

        Test Test = new Test();

    }

}