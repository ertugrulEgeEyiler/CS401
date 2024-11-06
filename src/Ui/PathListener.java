package Ui;

import Parser.ImportFinder;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class PathListener implements ActionListener {
    private JTextField userInput;
    private ImportFinder importFinder;


    public PathListener(JTextField userInput) {
        this.userInput = userInput;
        importFinder = new ImportFinder();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String currentPath = System.getProperty("user.dir");
        String memoryPath = currentPath + File.separator + "src" + File.separator + "Memory";
        String path = userInput.getText();
        String outputFile = memoryPath + File.separator + "output.txt";
        System.out.println(path);
        try {
            PrintWriter printWriter = new PrintWriter(outputFile);
            importFinder.createImports(path, printWriter);
            printWriter.close();
        }
        catch (FileNotFoundException e1) {}
        ClusterFrame clusterFrame = new ClusterFrame(path);
    }
}