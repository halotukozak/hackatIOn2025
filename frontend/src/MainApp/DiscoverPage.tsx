import ListComponent from "./ListComponent.tsx";
import { UserShow } from "../types/user.ts";
import Navbar from "./Navbar.tsx";
import { MatchStatus } from "../types/match.ts";
import { getAllUsers } from "../apis/users.ts";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export default function DiscoverPage() {
  const [discoverList, setDiscoverList] = useState<UserShow[] | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const userId = localStorage.getItem("user_id");
  const navigate = useNavigate();

  const fetchUser = async () => {
    try {
      const fetchedUser = await getAllUsers(Number(userId));
      setDiscoverList(fetchedUser);
    } catch (err) {
      setError("Failed to load user");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (!userId) {
      navigate("/");
      return;
    } else {
      fetchUser();
    }
  }, [navigate, userId]);
  if (!userId) return null;
  if (loading)
    return (
      <div className="min-h-screen bg-base-200">
        <Navbar />
        <p className="text-gray-700 text-sm pt-20 text-left ml-0 pl-4">
          Loading...
        </p>
      </div>
    );
  if (error)
    return (
      <div className="min-h-screen bg-base-200">
        <Navbar />
        <p className="text-gray-700 text-sm pt-20 text-left ml-0 pl-4">
          {error}
        </p>
      </div>
    );

  return (
    <div className="min-h-screen bg-base-200">
      <Navbar />
      <div className="pt-20 text-left ml-0 pl-4">
        <h1 className="text-2xl font-bold mb-4 ">
          Discover Potential Roommates
        </h1>
      </div>
      <div className="flex flex-col justify-center px-2 space-y-4">
        {discoverList &&
          discoverList.map((user) => (
            <ListComponent
              key={user.id}
              user={user}
              match={MatchStatus.View}
              refetchUsers={fetchUser}
            />
          ))}
      </div>
    </div>
  );
}
