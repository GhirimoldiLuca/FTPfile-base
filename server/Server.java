package server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

/**
 * Server process for send the file content through the {@link Socket} with the
 * Client
 * 
 * @author Ghirimoldi Luca
 * 
 * @version 1.1.3
 * @since 2022-10-10
 */
public class Server {
    ServerSocket server = null;
    Socket socket = null;
    BufferedReader input = null;
    BufferedWriter output = null;

    File file;
    int attempts = 0;
    int PORT = 6666;

    String dirPath = "server/";

    /**
     * Default constructor
     */
    public Server() {
    }

    /**
     * Launch the { @link ServerSocket}
     * Wait for a client {@link Socket} request
     * Saving the { @link InputStream} { @link outStream}
     * Handling {@link IOException} { @link ServerSocket} port already used or
     * in/out stream error
     * 
     * @return {@link Socket}
     */
    public Socket launch() {
        try {
            server = new ServerSocket(PORT);
            socket = server.accept();
            server.close();

            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("[SERVER] Connected!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("[CLIENT] Connection failed!");
            System.exit(3);
        }
        return socket;
    }

    /**
     * Send a given file content to the client
     * Handling the {@link Exception}
     * 
     */
    public void communicate() {
        try {
            String filename = input.readLine(); 
            file = new File(dirPath + filename);

            if (file.exists()) {
                send("true");
                List<String> fileContent = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
                send(String.valueOf(fileContent.size()));
                for (String line : fileContent) {
                    if(line.length()>0){
                        line = line.replaceAll("[^a-zA-Z0-9 ]", "");
                        send(line);
                    }else{
                        continue;
                    }
                }
                send("[SERVER] File transfered successfully!");
                System.out.println("[SERVER] File transfered successfully!");
            } else {
                send("false");
                send("[SERVER] - File not found");
                System.err.println("[SERVER] - File not found");
            }

            socket.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("[SERVER]\tError during connection");
            System.exit(1);
        }
    }

    private void send(String text){
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
     * Start the server process
     * 
     * @param args argument (no use)
     */
    public static void main(String args[]) {
        Server server = new Server();
        server.launch();
        server.communicate();
    }
}
