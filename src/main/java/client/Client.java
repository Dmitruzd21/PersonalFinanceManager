package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static String ip = "127.0.0.1";
    public static int port = 8989;

    public static void main(String[] args) {
        try (Socket socket = new Socket(ip, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
              out.println("{\"title\": \"булка\", \"date\": \"2022.02.08\", \"sum\": 200}");
            // out.println("{\"title\": \"мыло\", \"date\": \"2022.02.08\", \"sum\": 800}");
            // out.println("{\"title\": \"орехи\", \"date\": \"2022.02.08\", \"sum\": 1100}");
            // out.println("{\"title\": \"стул\", \"date\": \"2022.02.08\", \"sum\": 500}");
            out.println();
            System.out.println(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
