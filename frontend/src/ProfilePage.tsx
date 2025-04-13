import { useEffect, useRef, useState } from "react";

import Navbar from "./Navbar";
import {
  UserShow,
  personalityLabels,
  drinkLabels,
  smokeLabels,
} from "./types/user";
import { Link } from "react-router-dom";
import { getUserById, getAllUsers } from "./apis/users";

export default function ProfilePage() {
  const [user, setUser] = useState<UserShow | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fileInputRef = useRef<HTMLInputElement>(null);
  const [photo, setPhoto] = useState<string | null>(null);

  const handlePhotoClick = () => {
    fileInputRef.current?.click();
  };
  const handlePhotoChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setPhoto(reader.result as string);
      };
      reader.readAsDataURL(file);
    }
  };

  useEffect(() => {
    const fetchUser = async () => {
      try {
        // const fetchedUser = await getUserById(1);
        const fetchedUser = await getAllUsers();
        setUser(fetchedUser[0]);
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
      <div className="pt-20 px-6 max-w-2xl mx-auto">
        {/* Header */}
        <div className="flex items-center gap-4 mb-6">
          {/* Clickable Profile Photo */}
          <div
            className="w-20 h-20 rounded-full bg-gray-200 overflow-hidden flex items-center justify-center text-gray-500 cursor-pointer border border-gray-300"
            onClick={handlePhotoClick}
            title="Click to change photo"
          >
            {photo ? (
              <img
                src={photo}
                alt="Profile"
                className="w-full h-full object-cover"
              />
            ) : (
              <span>👤</span>
            )}
            <input
              ref={fileInputRef}
              type="file"
              accept="image/*"
              onChange={handlePhotoChange}
              className="hidden"
            />
          </div>
          <div>
            <h1 className="text-2xl font-semibold">
              {user!.name} {user!.surname}
            </h1>
            <p className="text-sm text-gray-600">
              {user!.info.faculty} • Year {user!.info.yearOfStudy}
            </p>
          </div>
        </div>

        {/* About Section */}
        <div className="bg-white rounded-lg p-4 shadow mb-4">
          <h2 className="font-semibold mb-2">About You</h2>
          <p className="text-gray-700 text-sm">{user!.info.description}</p>
        </div>

        {/* Lifestyle Section */}
        <div className="bg-white rounded-lg p-4 shadow mb-4">
          <h2 className="font-semibold mb-2">Lifestyle</h2>
          <div className="grid grid-cols-2 gap-y-2 text-sm text-gray-700">
            <div>
              <span className="font-semibold block">Sleep Schedule</span>
              {user!.info.sleepSchedule[0]} - {user!.info.sleepSchedule[1]}
            </div>
            <div>
              <span className="font-semibold block">Personality</span>
              {personalityLabels[user!.info.personalityType]}
            </div>
            <div>
              <span className="font-semibold block">Smoking</span>
              {smokeLabels[user!.info.smoke]}
            </div>
            <div>
              <span className="font-semibold block">Drinking</span>
              {drinkLabels[user!.info.drink]}
            </div>
          </div>
        </div>

        {/* Interests Section */}
        <div className="bg-white rounded-lg p-4 shadow mb-4">
          <h2 className="font-semibold mb-2">Interests</h2>
          <div className="flex flex-wrap gap-2">
            {user!.info.hobbies.split(", ").map((hobby, idx) => (
              <span key={idx} className="badge badge-ghost">
                {hobby}
              </span>
            ))}
          </div>
        </div>

        {/* Buttons */}
        <div className="flex justify-between mt-4">
          <Link to="/get_started">
            <button className="btn btn-outline btn-success bg-white">
              Edit Profile
            </button>
          </Link>
          <button className="btn btn-error text-white">Deactivate</button>
        </div>
      </div>
    </div>
  );
}
