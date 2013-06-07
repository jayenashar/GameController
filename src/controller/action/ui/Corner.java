package controller.action.ui;

import common.Log;
import controller.action.ActionType;
import controller.action.GCAction;
import data.AdvancedData;

/**
 * @author: Michel Bartsch
 * 
 * *** This action is not to be used in a normal Game! ***
 * It sets the secGameState to undefined values which
 * indicates a corner to b-human robots during an open challange.
 */
public class Corner extends GCAction
{
    /* The secGameState will be set to this value. */
    int secGameStateValue;
    /**
     * Creates a new Corner action.
     * Look at the ActionBoard before using this.
     */
    public Corner(int secGameStateValue)
    {
        super(ActionType.UI);
        this.secGameStateValue = secGameStateValue;
    }
    
    
    /**
     * Performs this action to manipulate the data (model).
     * 
     * @param data      The current data to work on.
     */
    @Override
    public void perform(AdvancedData data)
    {
        data.secGameState = (byte)secGameStateValue;
        Log.state(data, "Corner ("+secGameStateValue+")");
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
        return data.testmode;
    }
}