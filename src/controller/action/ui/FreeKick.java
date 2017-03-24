package controller.action.ui;

import common.Log;
import controller.action.ActionBoard;
import controller.action.ActionType;
import controller.action.GCAction;
import data.Rules;
import data.communication.GameControlData;
import data.states.AdvancedData;
import data.states.SecondaryState;

/**
 * @author Robert Kessler
 * 
 * This action performs the switch to the Secondary Game State: Free Kick
 * It stops the global clock and prepares the secondary clock to run down to zero
 * When the action is resolved then - the normal clock continues and the previous state
 * is taken again. Based on which side the action is executed for the FreeKick Mode in the
 * Secondary State Object is updated
 */
public class FreeKick extends GCAction
{
    /** On which side (0:left, 1:right) */
    private int side;

    public FreeKick(int side)
    {
        super(ActionType.UI);
        this.side = side;
    }

    @Override
    public void perform(AdvancedData data)
    {
        System.out.println("Attempting to perform FreeKick action");
        if (!data.freeKickActive[side]) {
            data.previousSecGameState = data.secGameState;
            data.secGameState = SecondaryState.SECONDARY_STATE_FREEKICK;
            System.out.println("Switching to freekick for team with team number " + data.team[side].teamNumber);
            data.secGameStateInfo.switchToFreeKick(data.team[side].teamNumber);
            data.whenFreeKick = data.getTime();
            data.freeKickActive[side] = true;
            data.gameClock.setSecondaryClock(Rules.league.free_kick_preparation_time);
            Log.setNextMessage("FreeKick " + Rules.league.teamColorName[data.team[side].teamColor]);
            ActionBoard.clockPause.perform(data);
        } else {
            data.secGameState = data.previousSecGameState;
            data.previousSecGameState = SecondaryState.SECONDARY_STATE_FREEKICK;
            data.secGameStateInfo.reset();
            data.freeKickActive[side] = false;
            Log.setNextMessage("End FreeKick " + Rules.league.teamColorName[data.team[side].teamColor]);
            ActionBoard.clockPause.perform(data);
        }
    }

    @Override
    public boolean isLegal(AdvancedData data)
    {
      return data.gameState == GameControlData.STATE_PLAYING && !data.freeKickActive[1-side];
    }
}