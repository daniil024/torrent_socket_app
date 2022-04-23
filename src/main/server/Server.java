package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {

        int serverPort = 3000;
        System.out.println("Please, input directory with files:");
        Scanner in = new Scanner(System.in);
        File directory = new File(in.nextLine());

        System.out.println("Please, enter number of port (or you can enter \"no\", if you want to use default number):");
        String tmp;
        try {
            if (!(tmp = in.nextLine()).equalsIgnoreCase("no"))
                serverPort = Integer.parseInt(tmp);
        } catch (Exception e) {
            System.out.println("Probably, you entered not valid value");
        }

        File[] directoryFiles = null;
        ArrayList<File> files = new ArrayList<>();

        try {
            if (directory.isDirectory())
                directoryFiles = directory.listFiles();

            if (directoryFiles != null)
                for (File item : directoryFiles)
                    if (!item.isDirectory() && item.length() <= 137438953472L)
                        files.add(item);

            ServerSocket phonesServer = new ServerSocket(serverPort);
            while (true) {
                Socket socket = phonesServer.accept();
                System.out.println("Connection accepted.");
                Thread thread = new ServerThread(socket, files);
                thread.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

class ServerThread extends Thread {
    final Socket clientSocket;
    ArrayList<File> list;

    ObjectOutputStream oos;
    DataOutputStream out;
    DataInputStream in;

    BufferedReader br;


    ServerThread(Socket s, ArrayList<File> l) {
        clientSocket = s;
        list = l;
        try {
            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new DataInputStream(clientSocket.getInputStream());
            oos = new ObjectOutputStream(clientSocket.getOutputStream());

            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (Exception ignored) {
        }
    }


    /**
     * Method used to send file to client.
     * @param inputFilePath name of file, which need to be send.
     */
    public void sendFile(String inputFilePath) {
        File file = null;
        try {
            for (var f : list)
                if (f.getPath().contains(inputFilePath))
                    file = f;
            if (file == null)
                return;

            oos.writeObject(file);
            oos.flush();
        } catch (IOException ignored) {
        }
    }

    public void run() {
        try {
            System.out.println("DataOutputStream created");
            System.out.println("DataInputStream created");

            ArrayList<String> names = new ArrayList<>();
            for (File file : list) names.add(file.getName() + ":size:" + file.length());
            oos.writeObject(names);


            while (!clientSocket.isClosed()) {
                System.out.println("Server reading from channel");

                String entry = br.readLine();

                if (entry == null)
                    return;
                if (entry.equalsIgnoreCase("quit")) {
                    System.out.println("Client initialize connections suicide ...");
                    out.flush();
                    break;
                }

                System.out.println("READ from client message - " + entry);
                System.out.println("Server try writing to channel");

                sendFile(entry);
                System.out.println("File was sent to client...\n\n");
                out.flush();
            }

            System.out.println("Client disconnected");
            System.out.println("Closing connections & channels.");
            in.close();
            out.close();
            oos.close();
            clientSocket.close();

            System.out.println("Closing connections & channels - DONE.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
