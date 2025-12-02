public class EndState implements GameState {

    private final Menu ctx;
    private final StateMachine fsm;

    public EndState(Menu ctx, StateMachine fsm) {
        this.ctx = ctx;
        this.fsm = fsm;
    }

    @Override
    public GameStateId getId() {
        return GameStateId.END;
    }

    @Override
    public void onStateEnter() {
        System.out.println("Thanks for playing!");
    }

    @Override
    public void onStateExecution() {
        // nothing; game will exit
    }

    @Override
    public void onStateExit() { }
}
