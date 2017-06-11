package actions;

import common.Log;
import controller.action.ActionBoard;
import controller.action.ui.IndirectFreeKick;
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
        IndirectFreeKick fk = new IndirectFreeKick(0);

        // Create an advanced data object to perform it on
        AdvancedData data = new AdvancedData();
        data.team[0].teamNumber = 14;

        // Perform the action and expect the state change
        fk.perform(data);

        assertEquals(SecondaryGameStates.FREEKICK, data.secGameState);
        assertEquals(14, data.secGameStateInfo.toByteArray()[0]);

        // Perform it again and expect the state change back
        fk.perform(data);

        assertNotSame(SecondaryGameStates.FREEKICK, data.secGameState);
        assertEquals(0, data.secGameStateInfo.toByteArray()[0]);
    }

    @Test
    public void testFreeKickNotPossibleWhilePenaltyKickSecondaryGameState(){
        ActionBoard.init(); // TODO - get rid of this stupid static thing

        /// Create a free kick action
        IndirectFreeKick fk = new IndirectFreeKick(0);

        // Create an advanced data object to perform it on
        AdvancedData data = new AdvancedData();
        data.secGameState = SecondaryGameStates.PENALTYKICK;

        // Assert that it is not possible to execute a FreeKickAction during a PenaltyKick
        assertFalse(fk.isLegal(data));
    }
}
