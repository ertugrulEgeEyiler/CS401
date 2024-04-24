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

        importFinder.createImports(directory, printWriter);
        importClusterer.findClusters(outputFile, clusteredFile);

    }

    public void test() {

        Test Test = new Test();

    }

}