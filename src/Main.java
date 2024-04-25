
import Clusterer.ImportClusterer;
import Clusterer.KModeClusterer;
import Parser.ImportFinder;
import Test.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        ImportFinder importFinder = new ImportFinder();
        ImportClusterer importClusterer = new ImportClusterer();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter the directory of your project.");
        String directory = scanner.nextLine();
        File dir = new File(directory);

        if (!dir.exists()) {
            System.out.println("Directory does not exist: " + directory);
            return;
        }

        if (!dir.isDirectory()) {
            System.out.println("This is not a directory: " + directory);
            return;
        }

        System.out.println("Directory is valid and exists: " + directory);
        String outputFile = "C:\\Users\\asimo\\Desktop\\cs401 output\\output.txt";
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
        importFinder.createImports(directory, printWriter);
        printWriter.close();

        // Import clustering
        String clusteredFile = "C:\\Users\\asimo\\Desktop\\cs401 output\\clustered.txt";
        importClusterer.findClusters(outputFile, clusteredFile);
        System.out.println("Clustering complete, results saved to: " + clusteredFile);

        // K-Modes clustering
        KModeClusterer kModesClusterer = new KModeClusterer(3);  // Number of clusters can be adjusted
        String kModesOutputFile = "C:\\Users\\asimo\\Desktop\\cs401 output\\kModesClustered.txt";
        kModesClusterer.executeClustering(outputFile, kModesOutputFile);
        System.out.println("K-Modes Clustering complete, results saved to: " + kModesOutputFile);



    }

    public void test() {
        Test test = new Test();
    }
}
