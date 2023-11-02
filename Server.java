import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
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
                clients.add(out);

                Thread thread = new Thread(new ClientHandler(socket));
                thread.start();
                System.out.println("Client connected: " + socket.getInetAddress());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcastMessage(String message) {
        for (PrintWriter client : clients) {
            client.println(message);
            client.flush();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private Scanner in;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = new Scanner(socket.getInputStream());
            while (in.hasNextLine()) {
                String message = in.nextLine();
                System.out.println("Received message: " + message);
                Server.broadcastMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                socket.close();
                Server.clients.remove(new PrintWriter(socket.getOutputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}