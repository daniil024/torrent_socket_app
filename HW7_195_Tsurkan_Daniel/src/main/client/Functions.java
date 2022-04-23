package client;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.ArrayList;

public class Functions {

    private static ObjectInputStream ois = null;
    private static DataOutputStream ps = null;
    private static FileOutputStream fos = null;
    private static PrintStream prs;

    private static InetAddress serverHost;
    static Socket clientSocket;
    private static String hostName = "localhost";
    private static int portNumber = 3000;

    static Button button = null;
    static String fileName;
    static Long fileSize;

    static ArrayList<String> files = new ArrayList<>();


    /**
     * Method used to get connection to server and to get list of file names.
     * @param s host
     * @param p port
     */
    public static void getConnection(String s, String p) {
        hostName = s;
        portNumber = Integer.parseInt(p);
        try {
            if (clientSocket != null)
                clientSocket.close();

            try {
                serverHost = InetAddress.getByName(hostName);
            } catch (UnknownHostException e) {
                showAlert("There is no host with entered value!");
                return;
            }

            try {
                clientSocket = new Socket(serverHost, portNumber);
            } catch (ConnectException e) {
                showAlert("There is no host with entered port value!");
                return;
            }

            ois = new ObjectInputStream(clientSocket.getInputStream());
            ps = new DataOutputStream(clientSocket.getOutputStream());
            prs = new PrintStream(clientSocket.getOutputStream());

            files.addAll((ArrayList<String>) ois.readObject());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Method used to show alert when input data is incorrect.
     *
     * @param text  text for the alert.
     */
    protected static void showAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Attention!");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }


    /**
     * Method used to close all connections when client is disconnected.
     */
    public static void closeConnections() {
        try {
            sendNameOfFile("quit");
            if (clientSocket != null)
                clientSocket.close();
            if (ois != null)
                ois.close();
            if (ps != null)
                ps.close();
            if (fos != null)
                fos.close();
        } catch (Exception ignored) {
        }
    }


    /**
     * Method used to send name of file, which client want to upload.
     * @param name name of file.
     */
    public static void sendNameOfFile(String name) {
        try {
            prs.println(name);
            prs.flush();
        } catch (Exception ignored) {
        }
    }


    /**
     * Method used to receive file from stream.
     * @param path path where we need to save file.
     */
    public static void receiveFile(String path) {
        File file;

        try {
            clientSocket = new Socket(serverHost, portNumber);
            file = (File) ois.readObject();

            byte[] fileContent = Files.readAllBytes(file.toPath());
            File output = new File(path + "\\" + file.getName());
            fos = new FileOutputStream(output);
            fos.write(fileContent);
            fos.flush();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
