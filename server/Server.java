package server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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
    //BufferedWriter output = null;
    DataOutputStream out = null;
    //DataInputStream in = null;

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
            //output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out = new DataOutputStream(socket.getOutputStream());
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

                out.writeBytes("true" + "\n");
                //output.write("true" + "\n");
                List<String> fileContent = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

                out.writeBytes(fileContent.size() + "\n");
                //output.write(fileContent.size() + "\n");
                out.flush();
                //output.flush();

                for (String line : fileContent) {
                    line = line.replaceAll("[^a-zA-Z0-9 ]", "");
                    out.writeBytes(line + "\n");
                    //output.write(line+"\n");
                    out.flush();
                    //output.flush();
                }
                //output.write("[SERVER] File transer successfully!" + "\n");
                out.writeBytes("[SERVER] File transer successfully!" + "\n");
                System.out.println("[SERVER] File transer successfully!");
            } else {
                out.writeBytes("false" + "\n");
                //output.write("false" + "\n");
                out.writeBytes("[SERVER] - File not found" + "\n");
                //output.write("[SERVER] - File not found" + "\n");
                System.err.println("[SERVER] - File not found");
            }

            socket.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("[SERVER]\tError during connection");
            System.exit(1);
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