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

        // Print the paths for debugging
        System.out.println("Output file generated: " + outputFile);

        // Run the external C++ clustering algorithm on output.txt
        String matrixAlgorithmOutput = testPath + File.separator + "matrixAlgorithm.rsf";
        System.out.println("Clustering algorithm output will be written to: " + matrixAlgorithmOutput);
        
        runClusteringAlgorithm(outputFile, matrixAlgorithmOutput);

        // Continue with Java-based clustering
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

    // Function to execute the C++ clustering algorithm
    public static void runClusteringAlgorithm(String inputFilePath, String outputFilePath) {
        try {
            // Full path to the matrixAlgorithm.exe file
            String exePath = "C:\\Users\\kalma\\OneDrive\\Belgeler\\GitHub\\CS401\\src\\Clusterer\\matrixAlgorithm.exe";
            
            // Debug print to ensure the correct paths are used
            System.out.println("Running clustering algorithm with input: " + inputFilePath + " and output: " + outputFilePath);
            
            // Create a ProcessBuilder to run the executable with arguments
            ProcessBuilder processBuilder = new ProcessBuilder(exePath, inputFilePath, outputFilePath);
            
            // Set the working directory to where the executable is located
            processBuilder.directory(new File("C:\\Users\\kalma\\OneDrive\\Belgeler\\GitHub\\CS401\\src\\Clusterer"));
            
            // Redirect error stream to the output stream to capture both stdout and stderr
            processBuilder.redirectErrorStream(true);
            
            // Start the process
            Process process = processBuilder.start();
    
            // Print process output
            Scanner scanner = new Scanner(process.getInputStream());
            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }
    
            // Wait for the process to finish
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Clustering algorithm completed successfully. Output saved to: " + outputFilePath);
            } else {
                System.err.println("Clustering algorithm encountered an error with exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to run clustering algorithm.");
            e.printStackTrace();
        }
    }
    

    // Test method (placeholder)
    public void test() {
        Test test = new Test();
    }
}
