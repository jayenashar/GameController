package actions;

import common.Log;
import controller.action.ActionBoard;
import controller.action.ui.FreeKick;
import data.communication.GameControlData;
import data.states.AdvancedData;
import data.states.SecondaryState;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotSame;

/**
 * Created by rkessler on 2017-03-24.
 */
public class TestFreeKickAction {

    @BeforeClass
    public static void setUp(){
        System.setProperty("CONFIG_ROOT", "test_resources/");
        try {
            Log.init("dummy.log");
        } catch (Exception e){
            // TODO - make sure the logger is a proper singleton
        }
    }

    @Test
    public void testFreeKickChangesAdvancedDataCorrectly(){
        ActionBoard.init(); // TODO - get rid of this stupid static thing

        /// Create a free kick action
        FreeKick fk = new FreeKick(0);

        // Create an advanced data object to perform it on
        AdvancedData data = new AdvancedData();
        data.team[0].teamNumber = 14;

        // Perform the action and expect the state change
        fk.perform(data);

        assertEquals(SecondaryState.SECONDARY_STATE_FREEKICK, data.secGameState);
        assertEquals(14, data.secGameStateInfo.toByteArray()[0]);

        // Perform it again and expect the state change back
        fk.perform(data);

        assertNotSame(SecondaryState.SECONDARY_STATE_FREEKICK, data.secGameState);
        assertEquals(0, data.secGameStateInfo.toByteArray()[0]);

    }
}
