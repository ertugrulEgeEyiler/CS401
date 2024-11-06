package Ui;

import Parser.ImportFinder;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.http.WebSocket;

public class GuiMain {

    private static ImportFinder importFinder = new ImportFinder();

    public static void main(String[] args) {
    JPanel panel = new JPanel();
    JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.add(panel);
        panel.setLayout(null);

    JLabel label = new JLabel("Path:");
        panel.add(label);
        label.setBounds(330, 250, 80, 25);

    JTextField userInput = new JTextField(40);
        userInput.setBounds(360, 250, 260, 25);
        panel.add(userInput);

    JButton button = new JButton("Confirm");
        panel.add(button);
        PathListener pathListener = new PathListener(userInput);
        button.addActionListener(pathListener);
        button.setBounds(450, 280, 120, 25);
        frame.setVisible(true);


}
}
