package data.values;

/**
 * Created by rkessler on 2017-03-25.
 */
public enum GameStates implements DocumentableHtml {

    IMPOSSIBLE(-1, "Impossible"),
    INITIAL(0, "Initial"),
    READY(1, "Ready"),
    SET(2, "Set"),
    PLAYING(3, "Playing"),
    FINISHED(4, "Finished");

    private byte byte_value;
    private String humanReadable;

    GameStates(int byte_value, String humanReadable) {

        this.byte_value = (byte) byte_value;
        this.humanReadable = humanReadable;
    }

    public byte value() {
        return byte_value;
    }

    public static GameStates fromValue(byte b) {
        for(GameStates gameState : GameStates.values()){
            if (gameState.value() == b){
                return gameState;
            }
        }
        System.out.println("This should never happen! Could not resolve GameState byte.");
        return null;
    }

    public String toString(){
        return this.humanReadable;
    }
}
