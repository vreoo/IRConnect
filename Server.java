import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {
    static List<PrintWriter> clients = new ArrayList<>();

    public static void main(String[] args) {
        int port = 7771;
        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                PrintWriter out = new PrintWriter(socket.getOutputStream());

                // Send a message to all clients that a new client has connected
                broadcastMessage("\nNew client connected: " + socket.getInetAddress(), out);
                clients.add(out);

                Thread thread = new Thread(new ClientHandler(socket, out));
                thread.start();
                System.out.println("\nClient connected: " + socket.getInetAddress());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcastMessage(String message, PrintWriter sender) {
        for (PrintWriter client : clients) {
            if (client != sender) {
                client.println(message);
            }
            client.flush();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private Scanner in;
    private PrintWriter sender;

    public ClientHandler(Socket socket, PrintWriter sender) {
        this.socket = socket;
        this.sender = sender;
    }

    @Override
    public void run() {
        try {
            in = new Scanner(socket.getInputStream());
            while (in.hasNextLine()) {
                String message = in.nextLine();
                System.out.println("Received message: " + message);
                Server.broadcastMessage(message, sender);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                // Gracefully close the socket and remove the client from the list
                System.out.println("\nClient disconnected: " + socket.getInetAddress());
                Server.clients.remove(new PrintWriter(socket.getOutputStream()));
                socket.close();
            } catch (SocketException e) {
                // Ignore
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}