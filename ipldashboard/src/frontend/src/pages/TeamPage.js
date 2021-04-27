import {React} from 'react'
import {MatchDetailCard} from "../components/MatchDetailCard";
import {MatchSallCard} from "../components/MatchSmallCard";

export const TeamPage = () => {
    return (
        <div className="TeamPage">
        <h1>Rajasthan Royals</h1>
        <MatchDetailCard/>
        <MatchSallCard/>
        <MatchSallCard/>
        <MatchSallCard/>
    </div>
);
}

