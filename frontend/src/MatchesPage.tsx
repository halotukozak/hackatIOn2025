import ListComponent from "./ListComponent.tsx";
import {
    User,
    Smoke,
    Drink,
    PersonalityType,
    RelationshipStatus,
} from "./types/user";
import Navbar from "./Navbar";
import {MatchStatus} from "./types/match.ts";

const user1: User = {
    name: "Emma",
    surname: "Johnson",
    email: "emma@example.com",
    age: 21,
    info: {
        description:
            "Psychology major with a passion for understanding people. I'm an early riser who keeps things tidy and enjoys quiet evenings with a good book.",
        sleepSchedule: ["18:00", "04:00"],
        hobbies: "Reading, Yoga, Photography, Travel",
        smoke: Smoke.NonSmoker,
        drink: Drink.SocialDrinker,
        personalityType: PersonalityType.Extraverted,
        yearOfStudy: 3,
        faculty: "Psychology",
        relationshipStatus: RelationshipStatus.Single,
    },
};

const user2: User = {
    name: "Emma2",
    surname: "Johnson",
    email: "emma@example.com",
    age: 21,
    info: {
        description:
            "Psychology major with a passion for understanding people. I'm an early riser who keeps things tidy and enjoys quiet evenings with a good book.",
        sleepSchedule: ["18:00", "04:00"],
        hobbies: "Reading, Yoga, Photography, Travel",
        smoke: Smoke.NonSmoker,
        drink: Drink.SocialDrinker,
        personalityType: PersonalityType.Extraverted,
        yearOfStudy: 3,
        faculty: "Psychology",
        relationshipStatus: RelationshipStatus.Single,
    },
};

export default function MatchesPage() {

    return (
        <div className="min-h-screen bg-base-200">
            <Navbar />
            <div className="pt-20 text-left ml-0 pl-4">
                <h1 className="text-2xl font-bold mb-4 ">
                    Your Matches
                </h1>
            </div>
            <div className="flex flex-col justify-center px-2 space-y-4">
                <ListComponent user={user2} match={MatchStatus.ContactInfo} />
                <ListComponent user={user1} match={MatchStatus.ContactInfo}/>
            </div>
            <div className="pt-10 text-left ml-0 pl-4">
                <h1 className="text-2xl font-bold mb-4 ">
                    Sent Requests
                </h1>
            </div>
            <div className="flex flex-col justify-center px-2 space-y-4">
                <ListComponent user={user2} match={MatchStatus.Send}/>
                <ListComponent user={user1} match={MatchStatus.Send}/>
            </div>
            <div className="pt-10 text-left ml-0 pl-4">
                <h1 className="text-2xl font-bold mb-4 ">
                    Received Requests
                </h1>
            </div>
            <div className="flex flex-col justify-center px-2 space-y-4">
                <ListComponent user={user2} match={MatchStatus.Received} />
                <ListComponent user={user1} match={MatchStatus.Received}/>
            </div>
        </div>
    );
}
