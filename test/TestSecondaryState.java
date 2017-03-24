import data.states.SecondaryState;
import org.junit.BeforeClass;
import org.junit.Test;


import static junit.framework.TestCase.assertEquals;

/**
 * Created by rkessler on 2017-03-24.
 */
public class TestSecondaryState {
    @BeforeClass
    public static void setUp(){
        System.setProperty("CONFIG_ROOT", "test_resources/");
    }

    @Test
    public void test_secondary_state_buffer_works() {
        SecondaryState secState = new SecondaryState();
        secState.switchToFreeKick((byte) 14);

        byte[] result = secState.toByteArray();
        assertEquals(14, result[0]);
        assertEquals(0, result[1]);
        assertEquals(0, result[2]);
        assertEquals(0, result[3]);

    }

}
