import server.FinanceManager;
import server.Server;

public class Main {
    private static String binFile = "data.bin";
    public static void main(String[] args) {
        FinanceManager manager = new FinanceManager();
        Server server = new Server(manager);
        server.start();
    }
}
