package Ui.Listener;

import Parser.ImportFinder;
import Ui.ClusterFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class PathListener implements ActionListener {
    private JTextField userInput;
    private ImportFinder importFinder;
    JFrame frame;


    public PathListener(JTextField userInput,JFrame frame) {
        this.userInput = userInput;
        importFinder = new ImportFinder();
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String currentPath = System.getProperty("user.dir");
        String memoryPath = currentPath + File.separator + "src" + File.separator + "Memory";
        String path = userInput.getText();
        String outputFile = memoryPath + File.separator + "output.txt";
        System.out.println(path);
        File directory = new File(memoryPath);
        directory.mkdirs();

        try {
            PrintWriter printWriter = new PrintWriter(outputFile);
            importFinder.createImports(path, printWriter);
            printWriter.close();
            ClusterFrame clusterFrame = new ClusterFrame(path);
            frame.dispose();

        }
        catch (FileNotFoundException e1) {
            JOptionPane.showMessageDialog(null, "Couldn't find the given path: " + e1.getMessage());

        }

    }
}