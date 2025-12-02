public interface GameState {
    GameStateId getId();
    void onStateEnter();
    void onStateExecution();
    void onStateExit();
}
