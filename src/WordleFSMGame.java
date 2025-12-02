import java.util.Scanner;

public class WordleFSMGame {

    public static void main(String[] args) {
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
}
