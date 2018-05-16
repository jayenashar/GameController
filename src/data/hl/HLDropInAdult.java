package data.hl;

/**
 * This class sets attributes given by the humanoid-league rules.
 *
 * @author Michel-Zen
 */
public class HLDropInAdult extends HL
{
    public HLDropInAdult()
    {
        /** The league´s name this rules are for. */
        leagueName = "HL Drop In - Adult";
        /** The league´s directory name with it´s teams and icons. */
        leagueDirectory = "hl_dropin";
        /** If true, the drop-in player competition is active */
        dropInPlayerMode = true;

        robotsPlaying = 2;

        teamSize = 3;
    }
}
