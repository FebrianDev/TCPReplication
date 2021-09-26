package client;

import shared.Player;
import shared.States;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import static shared.States.*;

public class Client {
    private Socket s;
    private DataOutputStream dos;
    private Scanner scn;

    private boolean run = true;

    public void start() {
        try {
            scn = new Scanner(System.in);

            // getting localhost ip
            InetAddress ip = InetAddress.getByName("localhost");

            // establish the connection with server port 5056
            s = new Socket(ip, 6666);

            // obtaining input and out streams
            dos = new DataOutputStream(s.getOutputStream());

            // the following loop performs the exchange of
            // information between client and client handler
            while (run) {
                System.out.println("What do you want?[" + LOGIN + " | " + PLAY + "]..\n" +
                        "Type " + EXIT + " to terminate connection.");

                try {
                    String input = scn.nextLine();
                    dos.writeUTF(input);
                    switch (States.valueOf(input)) {
                        case LOGIN -> login();
                        case PLAY -> play();
                        case EXIT -> exit();
                        default -> System.out.println("Invalid Input");
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    exit();
                }

            }
            // closing resources
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void play() throws IOException {
        dos.writeUTF("Ready For Play");
    }

    private void exit() throws IOException {
        // If client sends exit,close this connection
        // and then break from the while loop
        System.out.println("Closing this connection : " + s);
        s.close();
        run = false;
        System.out.println("Connection closed");
    }

    private void login() throws IOException {
        Player player = new Player();
        System.out.print("Input Username : ");
        player.setUsername(scn.nextLine());
        System.out.print("Input Password : ");
        player.setPassword(scn.nextLine());
        dos.writeUTF(player.getUsername() + "," + player.getPassword());
    }
}
