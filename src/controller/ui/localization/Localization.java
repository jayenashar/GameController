package controller.ui.localization;

/**
 * Created by rkessler on 2017-03-23.
 */
public class Localization {

    public static Localization getDefault(){
        return new Localization();
    }

    public final String FREE_KICK_PREPARE = "Prepare Free Kick";
    public final String FREE_KICK_EXECUTE = "Execute Free Kick";

    public final String PENALTY_KICK_PREPARE = "Prepare Penalty Kick";
    public final String PENALTY_KICK_EXECUTE = "Execute Penalty Kick";
}
