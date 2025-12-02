public class EvaluateGuessState implements GameState {

    private final Menu ctx;
    private final StateMachine fsm;

    public EvaluateGuessState(Menu ctx, StateMachine fsm) {
        this.ctx = ctx;
        this.fsm = fsm;
    }

    @Override
    public GameStateId getId() {
        return GameStateId.EVALUATE_GUESS;
    }

    @Override
    public void onStateEnter() { }

    @Override
    public void onStateExecution() {
        ctx.attempts++;

        String guess = ctx.currentGuess;
        String secret = ctx.secretWord;

        //G = correct, Y = present, X = absent
        StringBuilder feedback = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            char g = guess.charAt(i);
            char s = secret.charAt(i);

            if (g == s) {
                feedback.append("G");
            } else if (secret.indexOf(g) >= 0) {
                feedback.append("Y");
            } else {
                feedback.append("X");
            }
        }

        System.out.println("Your guess : " + guess);
        System.out.println("Feedback   : " + feedback + " (G=correct, Y=present, X=absent)");

        if (guess.equals(secret)) {
            fsm.changeState(new WinState(ctx, fsm));
        } else if (ctx.attempts >= ctx.maxAttempts) {
            fsm.changeState(new LoseState(ctx, fsm));
        } else {
            fsm.changeState(new WaitForGuessState(ctx, fsm));
        }
    }

    @Override
    public void onStateExit() { }
}
