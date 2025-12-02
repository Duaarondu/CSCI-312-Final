public class WinState implements GameState {

    private final Menu ctx;
    private final StateMachine fsm;

    public WinState(Menu ctx, StateMachine fsm) {
        this.ctx = ctx;
        this.fsm = fsm;
    }

    @Override
    public GameStateId getId() {
        return GameStateId.WIN;
    }

    @Override
    public void onStateEnter() {
        System.out.println();
        System.out.println("🎉 You guessed the word: " + ctx.secretWord);
    }

    @Override
    public void onStateExecution() {
        askPlayAgain();
    }

    private void askPlayAgain() {
        System.out.print("Play again? (y/n): ");
        String answer = ctx.scanner.nextLine().trim().toLowerCase();
        if (answer.equals("y")) {
            fsm.changeState(new PickWordState(ctx, fsm));
        } else {
            fsm.changeState(new EndState(ctx, fsm));
        }
    }

    @Override
    public void onStateExit() { }
}
