public class ValidateGuessState implements GameState {

    private final Menu ctx;
    private final StateMachine fsm;

    public ValidateGuessState(Menu ctx, StateMachine fsm) {
        this.ctx = ctx;
        this.fsm = fsm;
    }

    @Override
    public GameStateId getId() {
        return GameStateId.VALIDATE_GUESS;
    }

    @Override
    public void onStateEnter() { }

    @Override
    public void onStateExecution() {
        String guess = ctx.currentGuess;

        if (guess == null || guess.length() != 5) {
            System.out.println("Invalid guess. Must be exactly 5 letters.");
            fsm.changeState(new WaitForGuessState(ctx, fsm));
            return;
        }

        if (!ctx.isValidWord(guess)) {
            System.out.println("Invalid guess. Not in dictionary.");
            fsm.changeState(new WaitForGuessState(ctx, fsm));
        } else {
            fsm.changeState(new EvaluateGuessState(ctx, fsm));
        }
    }

    @Override
    public void onStateExit() { }
}
