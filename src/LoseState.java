public class LoseState implements GameState {

    private final Menu ctx;
    private final StateMachine fsm;

    public LoseState(Menu ctx, StateMachine fsm) {
        this.ctx = ctx;
        this.fsm = fsm;
    }

    @Override
    public GameStateId getId() {
        return GameStateId.LOSE;
    }

    @Override
    public void onStateEnter() {
        System.out.println();
        System.out.println("Out of guesses! The word was: " + ctx.secretWord);
    }

    @Override
    public void onStateExecution() {
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
