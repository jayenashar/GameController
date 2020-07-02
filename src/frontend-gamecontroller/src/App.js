import React from 'react';
import './App.scss';

function GamePeriods() {
    return (
        <div className='game-periods'>
            <div className='game-periods-periods'>
                <button data-active={true}>1st Half</button>
                <button disabled>2nd Half</button>
                {/*<button disabled>1st Extra</button>*/}
                {/*<button disabled>2nd Extra</button>*/}
                <button disabled>Penalty</button>
                <button>Referee Timeout</button>
                <button>Drop Ball</button>
            </div>
            <GamePeriod/>
        </div>
    );
}

function GamePeriod() {
    return (
        <div className='game-period'>
            <div className='game-period-states'>
                <button disabled>Initial</button>
                <button disabled>Ready</button>
                <button disabled>Set</button>
                <button data-active={true}>Playing</button>
                <button>Finished</button>
            </div>
            <div className='game-period-time'>
                18m05s
            </div>
            <div className='game-period-kickoff' data-team={2}>
                Kickoff
            </div>
        </div>
    );
}

function Teams() {
    return (
        <div className='teams'>
            <Team index={1}/>
            <Team index={2}/>
        </div>
    );
}

function Team({index}) {
    const teamName = index === 1 ? 'Rhoban' : 'Starkit';
    return (
        <div className='team' data-team-index={index}>
            <div className='team-header'>
                <div className='team-name'>
                    {teamName}
                </div>
                <img alt={teamName} className='team-icon' src={`/images/hl_kid/${index === 1 ? '11.png' : '12.png'}`}/>
                <button className='team-score'>
                    {index === 1 ? 18 : 5}
                </button>
            </div>
            <div className='team-actions'>
                {/*could/should we visually group the kicks?*/}
                <button>Team Timeout</button>
                <button>Penalty Kick</button>
                <button>Direct Free Kick</button>
                <button>Indirect Free Kick</button>
                <button>Corner Kick</button>
                <button>Goal Kick</button>
                <button>Throw-in</button>
                <button>Retake</button>
            </div>
            <Players teamIndex={index}/>
        </div>
    );
}

function Players({teamIndex}) {
    return (
        <div className='players'>
            <div className='players-players'>
                <Player teamIndex={teamIndex} index={1}/>
                <Player teamIndex={teamIndex} index={2}/>
                <Player teamIndex={teamIndex} index={3}/>
                <Player teamIndex={teamIndex} index={4}/>
                <Player teamIndex={teamIndex} index={5}/>
                <Player teamIndex={teamIndex} index={6}/>
            </div>
            <div className='player-actions'>
                <button>Pushing</button>
                <button>Ball handling</button>
                <button>Pickup/Incapable</button>
            </div>
        </div>
    );
}

function Player({teamIndex, index}) {
    const numCardsRed             = Math.floor(2 * Math.random());
    const numCardsYellow          = Math.floor(2 * Math.random());
    const numCardsGreen           = Math.floor(2 * Math.random());
    const isPenalised             = Math.random() < 2 / 12;
    const PENALTY_SECONDS         = 30;
    const penaltyRemainingSeconds = Math.floor(PENALTY_SECONDS * Math.random());
    const penaltyRemainingPercent = penaltyRemainingSeconds / PENALTY_SECONDS * 100;
    return (
        <div className='player'>
            <img alt='grey' src='/images/icons/wlan_status_grey.png'/>
            <div className='player-player' style={{position: 'relative'}}>
                <button>
                    <div style={isPenalised ?
                                {background: `linear-gradient(to right, yellow ${penaltyRemainingPercent}%, transparent ${penaltyRemainingPercent}%)`} :
                                {}}>
                        {index}{index > 4 && ' (sub)'}
                        {isPenalised && <><br/>{penaltyRemainingSeconds}s</>}
                    </div>
                </button>
            </div>
            <button className='player-goalkeeper'>
                <img src='/images/icons/goalkeeper.png' data-active={teamIndex === 1 && index === 3}/>
            </button>
            <button className='player-card player-card-red' data-value={numCardsRed}>{numCardsRed}</button>
            <button className='player-card player-card-yellow' data-value={numCardsYellow}>{numCardsYellow}</button>
            <button className='player-card player-card-green' data-value={numCardsGreen}>{numCardsGreen}</button>
        </div>
    );
}

function ActionList() {
    return (
        <div className='action-list'>
            <button style={{background: 'red'}}>2 - Red card</button>
            <button style={{background: 'red'}}>3 - Yellow card</button>
            <button style={{background: 'blue'}}>3 - Yellow card</button>
            <button style={{background: 'blue', opacity: 0.5}}>1 - Red card</button>
            <button style={{background: 'red'}}>Goal</button>
            <button>Set</button>
            <button>Playing</button>
        </div>
    );
}

function App() {
    return (
        <div className='game'>
            <GamePeriods/>
            <Teams/>
            <ActionList/>
        </div>
    );
}

export default App;
