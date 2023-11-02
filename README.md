# IRConnect

IRConnect is a simple chat application written in Java. It uses TCP sockets for communication between clients and a server. The server is capable of handling multiple clients simultaneously. The server broadcasts messages from one client to all other connected clients. The app contains simple basic utilities such as a changeable usernames.

## Getting Started

### Clone the Repository:

```bash
git clone https://github.com/your-username/Java-IRC-Chat.git
```

### Compile the Code:

-   Open a terminal and navigate to the project directory.
-   Compile the server and client programs separately:

```bash
javac Server.java
javac Client.java
```

### Run the Server:

Start the server on one of the computers within your local network.

```bash
java Server
```

Note the server's IP address (e.g., localhost or 192.168.x.x) and the port number (default is 7771) to be used by clients.

### Run the Clients:

On other computers within the local network, run the client program by specifying the server's IP address and port number.

```bash
java Client
```

### Start Chatting:

Once the client is running, you can type messages in the client's terminal and press Enter to send messages to the server. The server will broadcast these messages to all connected clients.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

This project is a basic example and not suitable for production use without further improvements and security considerations.
