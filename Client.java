import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        System.out.println("~ Welcome to the IRConnect! ~");
        String serverAddress = "localhost"; // Change this to the IP address of the server
        int port = 7771; // Change this to the port the server is listening on

        // Generate a random nickname
        String nickname = "user-" + (int) (Math.random() * 1000);

        try {
            System.out.println("Connecting to server...");
            Socket socket = new Socket(serverAddress, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            try (Scanner console = new Scanner(System.in)) {
                Thread receiveThread = new Thread(new ReceiveHandler(socket));
                receiveThread.start();
                System.out.println("Connected to server!\n");

                while (true) {
                    // add nickname to message + >
                    String message = console.nextLine();

                    // start time for ping
                    long startTime = System.currentTimeMillis();

                    // Handle special commands
                    if (message.equals("/exit")) {
                        System.out.println("Goodbye!");
                        break;
                    } else if (message.equals("/help")) {
                        System.out.println("Commands:");
                        System.out.println("/nick <nickname> - Change your nickname");
                        System.out.println("/ping - Ping the server");
                        System.out.println("/exit - Exit the program");
                        System.out.println("/help - Display this help message");
                        continue;
                    } else if (message.startsWith("/nick ")) {
                        nickname = message.substring(6);
                        System.out.println("Changed nickname to " + nickname);
                        continue;
                    } else if (message.startsWith("/ping")) {
                        // Send ping message to server
                        long endTime = System.currentTimeMillis();
                        System.out.println("PONG! " + (endTime - startTime) + "ms");
                        continue;
                    }

                    // Send message to server
                    out.println(nickname + "> " + message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }
}

class ReceiveHandler implements Runnable {
    private Socket socket;
    private Scanner in;

    public ReceiveHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = new Scanner(socket.getInputStream());
            while (in.hasNextLine()) {
                String message = in.nextLine();
                System.out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}