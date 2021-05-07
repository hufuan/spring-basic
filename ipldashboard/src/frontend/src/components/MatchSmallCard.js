import {React} from 'react';


export const MatchSmallCard = ({match, teamName}) => {
    if (!match) return null;
    const otherTeam = match.team1 === teamName ? match.team2: match.team1;

    return (
        <div className="MatchSallCard">
        <h3>vs {otherTeam}</h3>
        <p>{match.matchWinner} won by {match.resultMargin} {match.result} </p>
    </div>
);
}