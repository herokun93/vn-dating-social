package vn.dating.app.social;

import java.net.*;

public class UDPPortChecker {

    public static void main(String[] args) {
        String ipAddress = "15.235.162.218"; // Replace with the desired IPv4 address
        int definedPort = 68; // Replace with the desired UDP port number

        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(1000); // Set a timeout of 1000 milliseconds
            byte[] sendData = new byte[1];
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ipAddress), definedPort);

            socket.send(sendPacket);
            socket.receive(new DatagramPacket(new byte[1024], 1024));

            System.out.println("Port " + definedPort + " is open on " + ipAddress);
        } catch (Exception e) {
            System.out.println("Port " + definedPort + " is closed on " + ipAddress);
        }
    }
}
