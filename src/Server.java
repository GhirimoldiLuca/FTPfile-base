import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server process for exchange File content through the {@link Socket} with the client
 * 
 * @author Ghirimoldi Luca
 * 
 * @version 1.1.3
 * @since 2022-09-25
 */
public class Server{
    ServerSocket server = null;
    Socket client = null;
    BufferedReader input = null;
    DataOutputStream output = null;
    static int vowels = 0;
    static int consonants = 0;
    int attempts = 0;
    int PORT = 6666;
    File f;

    String dirPath = "out/";

    /**
     * Default constructor
     */
    public Server(){}
    
    /**
     * Launch the { @link ServerSocket} // todo not working
     * Wait for a client {@link Socket} request
     * Saving the { @link InputStream} { @link OutputStream} // todo not working
     * Handling {@link IOException} { @link ServerSocket} // todo not working// port already used or in/out stream error
     * 
     * @return {@link Socket}
     */
    public Socket launch() {
        try {
            server = new ServerSocket(PORT);
            client = server.accept();
            server.close();

            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new DataOutputStream(client.getOutputStream());
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(3);
        }
        return client;
    }

    /**
     * Receive a String from the Client
     * Invocation count() function, by passing the lowercase received String
     * Send a String to the Client and close the Client {@link Socket}
     * 
     * Handling the {@link Exception}
     * 
     */
    public void comunica() {
        try {
            String filename = input.readLine();
            f = new File(dirPath+filename);
            if(f.createNewFile()){
                FileWriter fw = new FileWriter(f);
                while (input.readLine() != null) {
                    String lineReceived = input.readLine();
                    fw.append(lineReceived);
                    System.out.println(lineReceived);
                }
                fw.close();
                output.writeBytes("File transer successfully!");
            }else{
                output.writeBytes("File transer Failed!");
            }


            client.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("[SERVER]\tErrore durante la connessione");
            System.exit(1);
        }
    }

    /**
     * Count the number of the vowels and consonants
     *
     * @param String s - messagge
     * 
     */
    private void count(String s) {
        int start = s.length();
        int numbers = s.replaceAll("\\D", "").length();
        int vowelsCount = (start - s.replaceAll("[aeiou]", "").length());
        vowels += vowelsCount;
        consonants += start - vowelsCount - numbers;
    }

    /**
     * Start the server process
     * @param args argument (no use)
     */
    public static void main(String args[]) {
        Server server = new Server();
        server.launch();
        server.comunica();
    }
}
