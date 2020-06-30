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
                10:02
            </div>
        </div>
    );
}

function Teams() {
    return (
        <div className='teams'>

        </div>
    );
}

function ActionList() {
    return (
        <div className='action-list'>

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
