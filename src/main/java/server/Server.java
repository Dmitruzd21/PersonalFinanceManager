package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {  // один обьект
    private FinanceManager financeManager;
    private int port = 8989;

    public Server(FinanceManager financeManager) {
        this.financeManager = financeManager;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port);) { // стартуем сервер один(!) раз
            System.out.println("Сервер запустился");
            while (true) { // в цикле(!) принимаем подключения
                try (Socket socket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream())) {
                    System.out.println("Новый клиент подключился");
                    String clientJSONRequest = in.readLine();
                    Purchase newPurchase = Purchase.createFromJSON(clientJSONRequest);
                    String response = financeManager.setCategoryAndAddPurchase(newPurchase)
                            .calculateEachCategorySum()
                            .findCategoryWithMaxSum()
                            .convertCategoryWithMaxSumToJSON();
                    out.println(response);
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}
