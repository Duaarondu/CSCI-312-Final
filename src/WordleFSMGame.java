import java.sql.SQLException;
import java.util.Scanner;

public class WordleFSMGame {
    public static void main(String[] args) {
        Database.connect();
        setupClosingDBConnection();

        Scanner scanner = new Scanner(System.in);

        Menu ctx = new Menu(scanner);
        StateMachine fsm = new StateMachine();

        GameState start = new StartState(ctx, fsm);
        fsm.setInitialState(start);

        while (fsm.getCurrentState().getId() != GameStateId.END) {
            fsm.update();
        }

        scanner.close();
    }

    public static void setupClosingDBConnection() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                try {
                    if (Database.connection != null && !Database.connection.isClosed()) {
                        Database.connection.close();
                        System.out.println("Application Closed - DB Connection Closed");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }, "Shutdown-thread"));
    }
}
