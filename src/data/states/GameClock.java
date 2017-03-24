package data.states;

import java.io.Serializable;

/**
 * Created by rkessler on 2017-03-23.
 */
public class GameClock implements Serializable {

    private int secondaryTime;
    private long secondaryTimeStart;

    public Integer getSecondaryTime() {
        return getRemainingSeconds(secondaryTimeStart, secondaryTime);
    }

    public void setSecondaryClock(int time_in_seconds){
        secondaryTimeStart = System.currentTimeMillis();
        secondaryTime = time_in_seconds;
    }



    public long getTime()
    {
        return System.currentTimeMillis();
    }

    public int getSecondsSince(long millis) {
        return millis == 0 ? 100000 : (int) (getTime() - millis) / 1000;
    }

    public int getRemainingSeconds(long millis, int durationInSeconds) {
        return durationInSeconds - getSecondsSince(millis);
    }


}
