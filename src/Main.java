import Clusterer.ImportClusterer;
import Clusterer.ImportRelationshipAnalyzer;
import Clusterer.KModeClusterer;
import Parser.ImportFinder;
import Test.Test;
import Clusterer.GeneticAlgorithm;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        ImportFinder importFinder = new ImportFinder();
        ImportClusterer importClusterer = new ImportClusterer();
        KModeClusterer kModesClusterer = new KModeClusterer(5);
        GeneticAlgorithm gaClusterer = new GeneticAlgorithm(10, 5, 50, 0.05);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter the directory of your project.");
        String directory = scanner.nextLine();
        String currentDirectory = "C:\\Users\\kalma\\OneDrive\\Belgeler\\GitHub\\CS401";
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
        String testPath = currentDirectory + File.separator + "src" + File.separator + "Test";
        String outputFile = testPath + File.separator + "output.txt";

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

        // Use the specified test directory for all output files
        String clusteredFile = testPath + File.separator + "clustered.rsf";
        importClusterer.findClusters(outputFile, clusteredFile);
        System.out.println("Clustering complete, results saved to: " + clusteredFile);

        // K-Modes clustering
        String kModesOutputFile = testPath + File.separator + "kModesOutput.rsf";
        kModesClusterer.executeClustering(outputFile, kModesOutputFile);
        System.out.println("K-Modes Clustering complete, results saved to: " + kModesOutputFile);

        // Genetic Algorithm Clustering
        String gaAlgorithmClusterFile = testPath + File.separator + "gaAlgorithmCluster.rsf";
        gaClusterer.findClusters(outputFile, gaAlgorithmClusterFile);
        System.out.println("Genetic Algorithm Clustering complete, results saved to: " + gaAlgorithmClusterFile);

        // Analyze import relationships
        String relationshipOutputFile = testPath + File.separator + "relationshipOutput.rsf";
        ImportRelationshipAnalyzer analyzer = new ImportRelationshipAnalyzer();
        analyzer.readFile(outputFile);
        analyzer.analyzeAndPrintClusters(relationshipOutputFile);
        System.out.println("Import clustering completed, results saved to: " + relationshipOutputFile);
    }
    
    public void test() {
        Test test = new Test();
    }

}