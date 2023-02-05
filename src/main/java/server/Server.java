package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {  // один обьект
    private FinanceManager financeManager;
    private int port = 8989;
    private File binFile = new File("data.bin");

    public Server(FinanceManager financeManager) {
        this.financeManager = financeManager;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port);) { // стартуем сервер один(!) раз
            if (binFile.exists()) {
                financeManager.loadPurchasesListFromBin();
            }
            System.out.println("Сервер запустился");
            while (true) { // в цикле(!) принимаем подключения
                try (Socket socket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream())) {
                    System.out.println("Новый клиент подключился");
                    String clientJSONRequest = in.readLine();
                    Purchase newPurchase = Purchase.createFromJSON(clientJSONRequest);
                    financeManager.setCategoryAndAddPurchase(newPurchase).findMaxSumCategoriesForDifferentPeriods();
                    String response = financeManager.convertCategoriesWithMaxSumToJSON();
                    financeManager.savePurchasesListToBin();
                    out.println(response);
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}
