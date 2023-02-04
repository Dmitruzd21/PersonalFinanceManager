import server.FinanceManager;
import server.Server;

public class Main {
    public static void main(String[] args) {
        FinanceManager manager = new FinanceManager();
        Server server = new Server(manager);
        server.start();
    }
}
