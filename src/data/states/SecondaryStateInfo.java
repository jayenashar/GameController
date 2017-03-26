package data.states;

import java.io.Serializable;

/**
 * Created by rkessler on 2017-03-24.
 */
public class SecondaryStateInfo implements Serializable {

    private byte[] secondaryStateInfo;

    public SecondaryStateInfo(){
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
