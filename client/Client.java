package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;

/**
 * Client process for receive the content of a file through the {@link Socket}
 * with the Server
 * 
 * @author Ghirimoldi Luca
 * 
 * @version 1.1.3
 * @since 2022-10-10
 */
public class Client {
    Socket socket;
    InetAddress ip;
    BufferedReader keyboard;

    BufferedReader input;
    BufferedWriter output;

    List<String> fileContent;

    String letters = "";
    int PORT = 6666, attempts = 0;

    String dirPath = "client/";

    /**
     * Default constructor
     */
    public Client() {
    }

    /**
     * Create the {@link Socket} @param {@link InetAddress}, @param PORT}
     * which try to estabilish the connection with the { @link ServerSocket} // todo
     * not working 3 times.
     * 
     * Handling the {@link UnknownHostException} , {@link IOException}
     * 
     * @return {@link Socket}
     * 
     */
    protected Socket connect() {
        try {
            keyboard = new BufferedReader(new InputStreamReader(System.in));
            ip = InetAddress.getLocalHost();
            while (attempts < 3 && socket == null) {
                socket = new Socket(ip, PORT);
                attempts++;
            }
            if (socket == null) {
                throw new IOException("[CLIENT] Can't estabilish connection with server!");
            } else {
                System.out.println("[CLIENT] Connected!");
            }
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (UnknownHostException e) {
            System.err.println(e.getMessage() + "\nUnknown local host address");
            System.exit(2);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(3);
        }

        return socket;
    }

    /**
     * Receive a given file from the server
     */
    protected void communicate() {
        try {
            Scanner sc = new Scanner(System.in);
            int nFiles = Integer.parseInt(input.readLine());
            for (int i = 0; i < nFiles; i++) {
                System.out.println("[" + (i + 1) + "] " + input.readLine());
            }
            System.out.print("Choose a file: ");
            send(String.valueOf(sc.nextInt()));
            String filename = input.readLine();
            File f = new File("client/" + filename);
            f.delete();
            if (f.createNewFile()) {
                FileWriter fw = new FileWriter(f);
                int lines = Integer.parseInt(input.readLine());
                System.out.println("File content: ");
                for (int i = 0; i < lines; i++) {
                    System.out.println("\t"+i);
                    fw.append(input.readLine() + "\n");
                }
                fw.close();

                socket.close();
            } else {
                System.out.println("Error!");
                socket.close();
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private void send(String text) {
        try {
            output.write(text);
            output.newLine();
            output.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(4);
        }
    }

    /**
     * Start the client process
     *
     * @param args argument (no use)
     */
    public static void main(String[] args) {
        Client client = new Client();
        client.connect();
        client.communicate();
    }

}
