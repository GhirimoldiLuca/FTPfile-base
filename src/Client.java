import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.Principal;
import java.util.List;

/**
 * Client process for exchange the content of a file through the {@link Socket}
 * with the Server
 * 
 * @author Ghirimoldi Luca
 * 
 * @version 1.1.3
 * @since 2022-09-24
 */
public class Client {
    int attempts = 0;
    Socket client;
    InetAddress ip;
    BufferedReader keyboard;
    DataOutputStream out;
    BufferedReader in;
    String letters = "";
    int PORT = 6666;

    List<String> fileContent;

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
            while (attempts < 3 && client == null) {
                client = new Socket(ip, PORT);
                attempts++;
            }
            if (client == null) {
                throw new IOException("Can't estabilish connection with server!");
            } else {
                System.out.println("Connected to the server!");
            }

            out = new DataOutputStream(client.getOutputStream());
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Unknown local host address");
            System.exit(2);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(3);
        }

        return client;
    }

    /**
     * Send a file to Server
     */
    protected void communicate() {
        try {
            System.out.print("- Enter file name to transfer: ");
            String filename = keyboard.readLine();
            out.writeBytes(filename + "\n");
            File f = new File(filename);
            if (f.isFile()) {
                fileContent = Files.readAllLines(f.toPath(), Charset.defaultCharset());
                
                for (String line : fileContent) {
                    System.out.println(line); // todo debug
                    out.writeBytes(line + "\n");
                }
            }
            out.flush();
            String received = in.readLine();
            System.out.println("received: " + received);
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
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