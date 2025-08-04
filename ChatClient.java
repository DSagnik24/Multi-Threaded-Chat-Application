package MultiThreadedChatApplication;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, PORT);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        ) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("👤 Enter your name: ");
            String name = scanner.nextLine();
            out.println("📢 " + name + " has joined the chat!");

            // Thread to read messages from server
            new Thread(() -> {
                String msgFromServer;
                try {
                    while ((msgFromServer = in.readLine()) != null) {
                        System.out.println(msgFromServer);
                    }
                } catch (IOException e) {
                    System.err.println("🔌 Connection closed.");
                }
            }).start();

            // Main thread to send messages
            while (true) {
                String message = scanner.nextLine();
                if (message.equalsIgnoreCase("exit")) {
                    out.println("❌ " + name + " has left the chat.");
                    break;
                }
                out.println(name + ": " + message);
            }

        } catch (IOException e) {
            System.err.println("❌ Client error: " + e.getMessage());
        }
    }
}
