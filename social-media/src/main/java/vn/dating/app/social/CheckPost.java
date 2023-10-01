package vn.dating.app.social;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetSocketAddress;
import java.net.Socket;

public class CheckPost {

        public static void main(String[] args) {
            JFrame frame = new JFrame("Port Checker");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 200);
            frame.setLayout(new FlowLayout());

            JLabel ipLabel = new JLabel("Enter IP Address:");
            JTextField ipField = new JTextField(15);

            JLabel portLabel = new JLabel("Enter Port:");
            JTextField portField = new JTextField(5);

            JButton checkButton = new JButton("Check Port");

            checkButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String ipAddress = ipField.getText();
                    int definedPort = Integer.parseInt(portField.getText());

                    try {
                        Socket socket = new Socket();
                        socket.connect(new InetSocketAddress(ipAddress, definedPort), 1000);
                        socket.close();
                        JOptionPane.showMessageDialog(null, "Port " + definedPort + " is open on " + ipAddress);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Port " + definedPort + " is closed on " + ipAddress);
                    }
                }
            });

            frame.add(ipLabel);
            frame.add(ipField);
            frame.add(portLabel);
            frame.add(portField);
            frame.add(checkButton);

            frame.setVisible(true);
        }

}
