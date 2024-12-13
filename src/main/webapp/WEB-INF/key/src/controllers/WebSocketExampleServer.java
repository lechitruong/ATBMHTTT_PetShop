package controllers;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class WebSocketExampleServer extends WebSocketServer {

    private final Set<WebSocket> clients = Collections.synchronizedSet(new HashSet<>());

    public WebSocketExampleServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        clients.add(conn);
        System.out.println("New connection: " + conn.getRemoteSocketAddress());
        conn.send("Welcome to the WebSocket server!"); // Gửi tin nhắn chào mừng
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        clients.remove(conn);
        System.out.println("Closed connection: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Received message: " + message);
        broadcast("[Client " + conn.getRemoteSocketAddress() + "]: " + message, conn);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
            clients.remove(conn);
        }
    }

    @Override
    public void onStart() {
        System.out.println("Server started successfully");
    }

    // Phương thức broadcast gửi tin nhắn đến tất cả các client
    private void broadcast(String message, WebSocket sender) {
        synchronized (clients) {
            for (WebSocket client : clients) {
                if (!client.equals(sender)) { // Không gửi lại cho client đã gửi tin nhắn
                    client.send(message);
                }
            }
        }
    }

    public static void main(String[] args) {
        WebSocketExampleServer server = new WebSocketExampleServer(new InetSocketAddress("localhost", 8081));
        server.start();
        System.out.println("WebSocket Server started on ws://localhost:8081");
    }
}