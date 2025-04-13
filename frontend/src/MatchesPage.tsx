import ListComponent from "./ListComponent.tsx";
import {
  UserShow,
} from "./types/user";
import Navbar from "./Navbar";
import { MatchStatus } from "./types/match.ts";
import {getAllUsers } from "./apis/users";
import {useEffect, useState} from "react";


export default function MatchesPage() {
  const [matchList, setMatchList] = useState<UserShow[] | null>(null);
  const [sendList, setSendList] = useState<UserShow[] | null>(null);
  const [receivedList, setReceivedList] = useState<UserShow[] | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const fetchedUser = await getAllUsers();
        setReceivedList(fetchedUser.slice(0, 2));
        setSendList(fetchedUser.slice(0, 3));
        setMatchList(fetchedUser.slice(0, 1));
      } catch (err) {
        setError("Failed to load user");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchUser();
  }, []);
  if (loading)
    return (
        <div>
          <Navbar />
          <p className="text-gray-700 text-sm">Loading...</p>
        </div>
    );
  if (error)
    return (
        <div>
          <Navbar />
          <p className="text-gray-700 text-sm">{error}</p>
        </div>
    );


  return (
    <div className="min-h-screen bg-base-200">
      <Navbar />
      <div className="pt-20 text-left ml-0 pl-4">
        <h1 className="text-2xl font-bold mb-4 ">Your Matches</h1>
      </div>
      <div className="flex flex-col justify-center px-2 space-y-4">
        {matchList &&  (
            matchList.map((user) => (
                <ListComponent key={user.id} user={user} match={MatchStatus.ContactInfo} />
            ))
        )};
      </div>
      <div className="pt-10 text-left ml-0 pl-4">
        <h1 className="text-2xl font-bold mb-4 ">Sent Requests</h1>
      </div>
      <div className="flex flex-col justify-center px-2 space-y-4">
        {sendList &&  (
            sendList.map((user) => (
                <ListComponent key={user.id} user={user} match={MatchStatus.Send} />
            ))
        )};
      </div>
      <div className="pt-10 text-left ml-0 pl-4">
        <h1 className="text-2xl font-bold mb-4 ">Received Requests</h1>
      </div>
      <div className="flex flex-col justify-center px-2 space-y-4">
        {receivedList &&  (
            receivedList.map((user) => (
                <ListComponent key={user.id} user={user} match={MatchStatus.Received} />
            ))
        )};
      </div>
    </div>
  );
}
