public class StartState implements GameState {

    private final Menu ctx;
    private final StateMachine fsm;

    public StartState(Menu ctx, StateMachine fsm) {
        this.ctx = ctx;
        this.fsm = fsm;
    }

    @Override
    public GameStateId getId() {
        return GameStateId.START;
    }

    @Override
    public void onStateEnter() {
        System.out.println("=== WORDLE FSM ===");
        System.out.println("Press Enter to start...");
    }

    @Override
    public void onStateExecution() {
        ctx.scanner.nextLine(); // wait for Enter
        fsm.changeState(new PickWordState(ctx, fsm));
    }

    @Override
    public void onStateExit() {
        // nothing special
    }
}
