public class PickWordState implements GameState {

    private final Menu ctx;
    private final StateMachine fsm;

    public PickWordState(Menu ctx, StateMachine fsm) {
        this.ctx = ctx;
        this.fsm = fsm;
    }

    @Override
    public GameStateId getId() {
        return GameStateId.PICK_WORD;
    }

    @Override
    public void onStateEnter() {
        ctx.attempts = 0;
        ctx.pickRandomSecretWord();
        System.out.println("A new secret 5-letter word has been chosen.");
    }

    @Override
    public void onStateExecution() {
        fsm.changeState(new WaitForGuessState(ctx, fsm));
    }

    @Override
    public void onStateExit() { }
}
