package data;

/**
 *
 * @author Michel-Zen
 * 
 * This class sets attributes given by the humanoid-league rules.
 */
public class HLAdult extends HL
{
    HLAdult()
    {
        /** The league´s name this rules are for. */
        leagueName = "HL Adult";
        /** The league´s directory name with it´s teams and icons. */
        leagueDirectory = "hl_adult";
        /** How many robots are in a team. */
        teamSize = 1;
        /** If the game starts with penalty-shoots. */
        startWithPenalty = true;
       /** Time in seconds one penalty shoot is long. */
        penaltyShootTime = (int)(2.5*60);
    }
}