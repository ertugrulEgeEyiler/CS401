package Ui.Listener;

import Ui.ClusterFrame;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class SaveListener implements ActionListener {
    JFrame frame;
    JList list;
    JTextField pathInput;

    public SaveListener(JFrame frame, JTextField pathInput, JList list){
        this.pathInput = pathInput;
        this.frame = frame;
        this.list = list;


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();

        if(button.getText().equals("Confirm")){
            String cluster = (String) list.getSelectedValue();
            String location = pathInput.getText() + File.separator;
            String source = System.getProperty("user.dir") + File.separator + "src" + File.separator + "Memory" + File.separator;

            if(cluster.equals("Genetic Algorithm")){
                source += "gaAlgorithmCluster.rsf";
                location += "geneticCluster.txt";
            }
            else if(cluster.equals("KMode Clusterer")){
                source += "kModesOutput.rsf";
                location += "kModeCluster.txt";
            }
            else if(cluster.equals("Import Clusterer")){
                source += "clustered.rsf";
                location += "importCluster.txt";
            }
            else if(cluster.equals("Import Analyzer Clusterer")){
                source += "relationshipOutput.rsf";
                location += "importAnalyzerCluster.txt";
            }
            else if(cluster.equals("Matrix Algorithm")){
                source += "matrixOutput.rsf";
                location += "matrixCluster.txt";
            }
            Path sourcePath = Paths.get(source);
            try {

                Path path = Paths.get(location);
                Files.createDirectories(path.getParent());
                Files.copy(sourcePath, path, StandardCopyOption.REPLACE_EXISTING);

                JFrame popUp = new JFrame();
                JLabel label = new JLabel("Succesfully saved to: " + location);
                popUp.add(label);
                popUp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                popUp.setVisible(true);
                popUp.setBounds(0,0,500,500);
                popUp.setLocationRelativeTo(null);
                frame.dispose();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Save wasn't successful: " + ex.getMessage());

            }

        }
        if(button.getText().equals("Cancel")){
            frame.dispose();
        }
    }
}
