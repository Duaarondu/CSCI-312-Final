public class StateMachine {
    private GameState currentState;

    public void setInitialState(GameState initial) {
        currentState = initial;
        currentState.onStateEnter();
    }

    public void changeState(GameState next) {
        if (currentState != null) {
            currentState.onStateExit();
        }
        currentState = next;
        if (currentState != null) {
            currentState.onStateEnter();
        }
    }

    public void update() {
        if (currentState != null) {
            currentState.onStateExecution();
        }
    }

    public GameState getCurrentState() {
        return currentState;
    }
}
