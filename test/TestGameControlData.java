import controller.action.ActionBoard;
import controller.action.ui.FreeKick;
import data.TeamInfo;
import data.communication.GameControlData;
import data.states.AdvancedData;
import data.states.GamePreparationData;
import data.states.SecondaryState;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.ByteBuffer;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by rkessler on 2017-03-24.
 */
public class TestGameControlData {

    @BeforeClass
    public static void setUp(){
        System.setProperty("CONFIG_ROOT", "test_resources/");
    }

    @Test
    public void test_secondary_game_state_send_correctly() {
        AdvancedData data = new AdvancedData();

        data.secGameState = SecondaryState.SECONDARY_STATE_FREEKICK;
        data.dropInTeam = GameControlData.TEAM_BLACK;

        ByteBuffer byteBuffer = data.toByteArray();
        byteBuffer.position(0);

        AdvancedData new_data = new AdvancedData();

        boolean done = new_data.fromByteArray(byteBuffer);

        assertTrue("could not parse stuff", done);
        assertEquals(data.secGameState, new_data.secGameState);
        assertEquals(data.dropInTeam, new_data.dropInTeam);
    }


}
