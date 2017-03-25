package actions;

import common.Log;
import controller.action.ActionBoard;
import controller.action.ui.TimeOut;
import data.states.AdvancedData;
import data.values.GameStates;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by rkessler on 2017-03-25.
 */
public class TestTimeOut {

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
    public void testTimeoutIsPossibleOnlyInNotPlayingStates() {
        ActionBoard.init(); // TODO - get rid of this stupid static thing

        TimeOut to = new TimeOut(0);
        AdvancedData data = new AdvancedData();

        data.gameState = GameStates.PLAYING;
        assertFalse(to.isLegal(data));
    }

    @Test
    public void testTimeoutPossibleInReadyState() {
        ActionBoard.init(); // TODO - get rid of this stupid static thing

        TimeOut to = new TimeOut(0);
        AdvancedData data = new AdvancedData();

        data.gameState = GameStates.READY;
        assertTrue(to.isLegal(data));
    }


}
