package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
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
            File cwd = new File(System.getProperty("user.dir") + "/server/");

            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File f, String name) {
                    return name.contains(".");
                }
            };

            List<File> files = Arrays.asList(cwd.listFiles(filter));
            send(String.valueOf(files.size()));
            files.forEach(e -> send((e.getName()).replace(cwd.getName(), "")));

            int chosen = Integer.parseInt(input.readLine()) - 1;
            File fileChosen = new File(files.get(chosen).getName());
            send(fileChosen.getName());
            long lines = Files.lines(files.get(chosen).toPath()).count();
            send(String.valueOf(lines));

            BufferedReader brf = new BufferedReader(new FileReader("server/" + fileChosen.getName()));
            for (long i = 0; i < lines; i++) {
                send(brf.readLine().replaceAll("[^a-zA-Z0-9 ]", ""));
            }
            brf.close();
            System.out.println("File transfered!");
            socket.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("[SERVER]\tError during connection");
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
