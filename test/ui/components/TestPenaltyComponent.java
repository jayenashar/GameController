package ui.components;

import controller.action.ui.penalty.Penalty;
import controller.ui.ui.components.PenaltyComponent;
import data.values.Penalties;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by rkessler on 2017-03-29.
 */
public class TestPenaltyComponent {

    @Test
    public void testPenaltyComponentReturnsNoSelectionToBeginWith(){
        PenaltyComponent pc = new PenaltyComponent();
        assertEquals(null, pc.selectedPenalty());
    }

    @Test
    public void testPenaltyComponentReturnsCorrectPenaltyForSelectedButton(){
        PenaltyComponent pc = new PenaltyComponent();
        boolean selected = pc.selectPenalty(Penalties.SPL_LEAVING_THE_FIELD);
        assertTrue(selected);
        assertEquals(Penalties.SPL_LEAVING_THE_FIELD, pc.selectedPenalty());
    }

    @Test
    public void testPenaltyComponentRestsCorrectly(){
        PenaltyComponent pc = new PenaltyComponent();
        boolean selected = pc.selectPenalty(Penalties.SPL_LEAVING_THE_FIELD);
        assertTrue(selected);
        pc.reset();
        assertEquals(null, pc.selectedPenalty());
    }



}
