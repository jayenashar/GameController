package controller.action.ui.state;

import common.Log;
import controller.action.ActionType;
import controller.action.GCAction;
import data.states.AdvancedData;
import data.Rules;
import data.values.GameStates;


/**
 * @author Michel Bartsch
 * 
 * This action means that the state is to be set to finish.
 */
public class Finish extends GCAction
{
    /**
     * Creates a new Finish action.
     * Look at the ActionBoard before using this.
     */
    public Finish()
    {
        super(ActionType.UI);
    }

    /**
     * Performs this action to manipulate the data (model).
     * 
     * @param data      The current data to work on.
     */
    @Override
    public void perform(AdvancedData data)
    {
        if (data.gameState == GameStates.FINISHED) {
            return;
        }
        if (Rules.league.returnRobotsInGameStoppages) {
            data.resetPenaltyTimes();
        }
        data.addTimeInCurrentState();
        data.whenCurrentGameStateBegan = data.getTime();
        data.gameState = GameStates.FINISHED;
        Log.state(data, "Finished");
    }
    
    /**
     * Checks if this action is legal with the given data (model).
     * Illegal actions are not performed by the EventHandler.
     * 
     * @param data      The current data to check with.
     */
    @Override
    public boolean isLegal(AdvancedData data)
    {
        return (data.gameState == GameStates.READY)
            || (data.gameState == GameStates.SET)
            || (data.gameState == GameStates.PLAYING)
            || (data.gameState == GameStates.FINISHED)
            || data.testmode;
    }
}