package Ui.Listener;

import Ui.ClusterFrame;
import Ui.SaveFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

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
        String location = pathInput.getText() + File.separator + "clusterOutput.txt";
        Path path = Paths.get(location);

        if(button.getText().equals("Confirm")){
            String cluster = (String) list.getSelectedValue();
            String source = System.getProperty("user.dir") + File.separator + "src" + File.separator + "Memory" + File.separator;
            if(cluster.equals("Genetic Algorithm")){
                source += "gaAlgorithmCluster.rsf";
            }
            if(cluster.equals("KMode Clusterer")){
                source += "kModesOutput.rsf";
            }
            if(cluster.equals("Import Clusterer")){
                source += "clustered.rsf";
            }
            if(cluster.equals("Import Analyzer Clusterer")){
                source += "relationshipOutput.rsf";
            }
            Path sourcePath = Paths.get(source);
            try {
                Files.copy(sourcePath,path,REPLACE_EXISTING);
                JFrame popUp = new JFrame();
                JLabel label = new JLabel("Succesfully saved to: " + location);
                popUp.add(label);
                popUp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                popUp.setVisible(true);
                popUp.setBounds(0,0,500,500);
                popUp.setLocationRelativeTo(null);
                frame.dispose();
                ClusterFrame clusterFrame = new ClusterFrame(location);

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }
        if(button.getText().equals("Cancel")){
            frame.dispose();
            ClusterFrame clusterFrame = new ClusterFrame(location);

        }
    }
}
