package controller.action.ui;

import common.Log;
import controller.action.ActionBoard;
import controller.action.ActionType;
import controller.action.GCAction;
import data.Rules;
import data.states.AdvancedData;
import data.values.GameStates;
import data.values.SecondaryGameStates;

/**
 * @author Michel Bartsch
 * 
 * This action means that a timeOut is to be taken or ending.
 */
public class PenaltyKick extends GCAction
{
    /** On which side (0:left, 1:right) */
    private int side;

    /**
     * Creates a new TimeOut action.
     * Look at the ActionBoard before using this.
     *
     * @param side      On which side (0:left, 1:right)
     */
    public PenaltyKick(int side)
    {
        super(ActionType.UI);
        this.side = side;
    }

    /**
     * Performs this action to manipulate the data (model).
     * 
     * @param data      The current data to work on.
     */
    @Override
    public void perform(AdvancedData data)
    {
        if (!data.penaltyKickActive[side]) {
            data.previousSecGameState = data.secGameState;
            data.secGameState = SecondaryGameStates.PENALTYKICK;
            data.secGameStateInfo.switchToPenaltyKick(data.team[side].teamNumber);
            data.whenPenaltyKick = data.getTime();
            data.penaltyKickActive[side] = true;
            data.gameClock.setSecondaryClock(Rules.league.penalty_kick_preparation_time);
            Log.setNextMessage("PenaltyKick " + data.team[side].teamColor);
            ActionBoard.clockPause.perform(data);
        } else {
            data.secGameState = data.previousSecGameState;
            data.previousSecGameState = SecondaryGameStates.PENALTYKICK;
            data.secGameStateInfo.reset();
            data.penaltyKickActive[side] = false;
            Log.setNextMessage("End PenaltyKick " + data.team[side].teamColor);
            ActionBoard.clockPause.perform(data);
        }
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
      return data.gameState == GameStates.PLAYING && !data.penaltyKickActive[1-side];
    }
}