package actions;

import common.Log;
import controller.action.ActionBoard;
import controller.action.ui.FreeKick;
import controller.action.ui.PenaltyKick;
import data.states.AdvancedData;
import data.values.SecondaryGameStates;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotSame;

/**
 * Created by rkessler on 2017-03-24.
 */
public class TestPenaltyKickAction {

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
        PenaltyKick pk = new PenaltyKick(1);

        // Create an advanced data object to perform it on
        AdvancedData data = new AdvancedData();
        data.team[1].teamNumber = 3;

        // Perform the action and expect the state change
        pk.perform(data);

        assertEquals(SecondaryGameStates.PENALTYKICK, data.secGameState);
        assertEquals(3, data.secGameStateInfo.toByteArray()[0]);

        // Perform it again and expect the state change back
        pk.perform(data);

        assertNotSame(SecondaryGameStates.PENALTYKICK, data.secGameState);
        assertEquals(0, data.secGameStateInfo.toByteArray()[0]);
    }

    @Test
    public void testPenaltyKickNotPossibleWhileFreeKickSecondaryGameState(){
        ActionBoard.init(); // TODO - get rid of this stupid static thing

        /// Create a free kick action
        PenaltyKick fk = new PenaltyKick(0);

        // Create an advanced data object to perform it on
        AdvancedData data = new AdvancedData();
        data.secGameState = SecondaryGameStates.FREEKICK;

        // Assert that it is not possible to execute a FreeKickAction during a PenaltyKick
        assertFalse(fk.isLegal(data));
    }
}
