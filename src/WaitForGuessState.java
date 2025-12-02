
public class WaitForGuessState implements GameState {

    private final Menu ctx;
    private final StateMachine fsm;

    public WaitForGuessState(Menu ctx, StateMachine fsm) {
        this.ctx = ctx;
        this.fsm = fsm;
    }

    @Override
    public GameStateId getId() {
        return GameStateId.WAIT_FOR_GUESS;
    }

    @Override
    public void onStateEnter() {
        System.out.println();
        System.out.println("Attempt " + (ctx.attempts + 1) + " of " + ctx.maxAttempts);
        System.out.print("Enter a 5-letter guess: ");
    }

    @Override
    public void onStateExecution() {
        String line = ctx.scanner.nextLine().trim().toUpperCase();
        ctx.currentGuess = line;
        fsm.changeState(new ValidateGuessState(ctx, fsm));
    }

    @Override
    public void onStateExit() { }
}
