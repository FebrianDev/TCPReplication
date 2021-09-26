package server;

import shared.Player;
import shared.States;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

// server.ClientHandler class
class ClientHandler extends Thread {
    private final DataInputStream dis;
    private final DataOutputStream dos;
    private final Socket s;

    boolean run = true;

    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run() {
        States received;
        while (run) {
            try {
                received = States.valueOf(dis.readUTF());

                // receive the answer from client
                System.out.println(received);

                switch (received) {
                    case LOGIN -> login();
                    case PLAY -> play();
                    case EXIT -> exit();
                    default -> dos.writeUTF("Invalid input");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void login() throws IOException {
        String data = dis.readUTF();
        String[] list = data.split(",", 2);

        Player player = new Player();
        player.setUsername(list[0]);
        player.setPassword(list[1]);

        System.out.println("Username : " + player.getUsername());
        System.out.println("Passowrd : " + player.getPassword());
    }

    private void play() throws IOException {
        String data = dis.readUTF();
        System.out.println(data);
    }

    private void exit() throws IOException {
        System.out.println("client.Client " + this.s + " sends exit...");
        System.out.println("Closing this connection.");
        this.s.close();
        run = false;
        System.out.println("Connection closed");
    }
}

