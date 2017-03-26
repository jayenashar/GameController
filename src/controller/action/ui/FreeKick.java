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
        if (!data.freeKickActive[side]) {
            data.previousSecGameState = data.secGameState;
            data.secGameState = SecondaryGameStates.FREEKICK;
            data.secGameStateInfo.switchToFreeKick(data.team[side].teamNumber);
            data.whenFreeKick = data.getTime();
            data.freeKickActive[side] = true;
            data.gameClock.setSecondaryClock(Rules.league.free_kick_preparation_time);
            Log.setNextMessage("FreeKick " + data.team[side].teamColor.toString());
            ActionBoard.clockPause.perform(data);
        } else {
            data.secGameState = data.previousSecGameState;
            data.previousSecGameState = SecondaryGameStates.FREEKICK;
            data.secGameStateInfo.reset();
            data.freeKickActive[side] = false;
            Log.setNextMessage("End FreeKick " + data.team[side].teamColor.toString());
            ActionBoard.clockPause.perform(data);
        }
    }

    @Override
    public boolean isLegal(AdvancedData data)
    {
      return data.gameState == GameStates.PLAYING
              && !data.freeKickActive[1-side]
              && data.secGameState != SecondaryGameStates.PENALTYKICK;
    }
}