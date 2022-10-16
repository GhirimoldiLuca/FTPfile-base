package client;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

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
            System.err.println(e.getMessage()+"\nUnknown local host address");
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
            System.out.print("- Enter file name to transfer: ");
            String filename = keyboard.readLine().replace(".txt","")+".txt";

            output.write(filename);
            output.newLine();
            output.flush();

            String response = input.readLine();
            if (Boolean.parseBoolean(response)) {
                int fileLen = Integer.parseInt(input.readLine());
                FileWriter fw = new FileWriter(dirPath + filename);
                System.out.println("\n- File content:");
                for (int i = 0; i < fileLen; i++) {
                    String lineReceived = input.readLine();
                    fw.append(lineReceived + "\n");
                    System.out.println("\t"+lineReceived);
                }
                fw.close();
                System.out.println("\n"+input.readLine());
            } else {
                System.out.println(input.readLine());
            }
            socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
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