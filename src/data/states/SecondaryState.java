package data.states;

import java.io.Serializable;

/**
 * Created by rkessler on 2017-03-24.
 */
public class SecondaryState implements Serializable {

    /** If the secondary state is free kick the additional info on the secondary state
     * will include as a first byte the team who should perform the free kick
     */
    public static final byte SECONDARY_STATE_FREEKICK = 4;

    /** If the secondary state is the penalty kick the additional info on the secondary
     * state will include as a first byte the team who should perform the penalty kick
     */
    public static final byte SECONDARY_STATE_PENALTYKICK = 5;


    private byte[] secondaryStateInfo;

    public SecondaryState(){
        secondaryStateInfo = new byte[]{0, 0, 0, 0};
    }

    public byte[] toByteArray() {
        return secondaryStateInfo;
    }

    public void fromByteArray(byte[] byte_array) {
        assert byte_array.length == 4: "Could not convert secondary state from byte array - not 4 bytes";
        secondaryStateInfo = byte_array;
    }

    /** Switches the mode to free kick and records the team performing it **/
    public void switchToFreeKick(byte teamPerforming){
        secondaryStateInfo[0] = teamPerforming;
    }

    public void reset() {
        secondaryStateInfo = new byte[]{0, 0, 0, 0};
    }

    /** Switches the mode to penalty kick and records the team performing it **/
    public void switchToPenaltyKick(byte teamPerforming) {
        secondaryStateInfo[0] = teamPerforming;
    }
}
