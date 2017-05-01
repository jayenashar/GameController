package ui.components;

import controller.ui.ui.components.TeamComponent;
import data.values.Side;
import org.junit.Test;

/**
 * Created by rkessler on 2017-04-25.
 */
public class TestTeamComponent {

    @Test
    public void testTeamComponent(){
        TeamComponent tc = new TeamComponent(Side.LEFT);
    }
}
