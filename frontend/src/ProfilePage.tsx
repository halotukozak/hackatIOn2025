// src/pages/ProfilePage.tsx

import Navbar from "./Navbar";
import {
  User,
  PersonalityType,
  Drink,
  Smoke,
  personalityLabels,
  drinkLabels,
  smokeLabels,
} from "./types/user";

const mockUser: User = {
  name: "Jane",
  surname: "Doe",
  email: "jane@example.com",
  age: 20,
  info: {
    description:
      "I'm a second-year Computer Science student who enjoys coding, gaming, and hiking on weekends. Looking for a clean and respectful roommate with similar interests.",
    sleepSchedule: ["00:00", "08:00"],
    hobbies: "Coding, Gaming, Hiking, Movies, Music",
    smoke: Smoke.NonSmoker,
    drink: Drink.SocialDrinker,
    personalityType: PersonalityType.Ambiverted,
    yearOfStudy: 2,
    faculty: "Computer Science",
    relationshipStatus: 0,
  },
};

export default function ProfilePage() {
  const hobbies = mockUser.info.hobbies.split(", ");

  return (
    <div className="min-h-screen bg-base-100">
      <Navbar />
      <div className="pt-20 px-6 max-w-2xl mx-auto">
        {/* Header */}
        <div className="flex items-center gap-4 mb-6">
          <div className="w-20 h-20 rounded-full bg-gray-200 flex items-center justify-center text-gray-500">
            {/* Image Placeholder */}
            <span>👤</span>
          </div>
          <div>
            <h1 className="text-2xl font-semibold">Your Profile</h1>
            <p className="text-sm text-gray-600">
              {mockUser.info.faculty} • Year {mockUser.info.yearOfStudy}
            </p>
          </div>
        </div>

        {/* About Section */}
        <div className="bg-white rounded-lg p-4 shadow mb-4">
          <h2 className="font-semibold mb-2">About You</h2>
          <p className="text-gray-700 text-sm">{mockUser.info.description}</p>
        </div>

        {/* Lifestyle Section */}
        <div className="bg-white rounded-lg p-4 shadow mb-4">
          <h2 className="font-semibold mb-2">Lifestyle</h2>
          <div className="grid grid-cols-2 gap-y-2 text-sm text-gray-700">
            <div>
              <span className="font-semibold block">Sleep Schedule</span>
              {mockUser.info.sleepSchedule[0]} -{" "}
              {mockUser.info.sleepSchedule[1]}
            </div>
            <div>
              <span className="font-semibold block">Personality</span>
              {personalityLabels[mockUser.info.personalityType]}
            </div>
            <div>
              <span className="font-semibold block">Smoking</span>
              {smokeLabels[mockUser.info.smoke]}
            </div>
            <div>
              <span className="font-semibold block">Drinking</span>
              {drinkLabels[mockUser.info.drink]}
            </div>
          </div>
        </div>

        {/* Interests Section */}
        <div className="bg-white rounded-lg p-4 shadow mb-4">
          <h2 className="font-semibold mb-2">Interests</h2>
          <div className="flex flex-wrap gap-2">
            {hobbies.map((hobby, idx) => (
              <span key={idx} className="badge badge-ghost">
                {hobby}
              </span>
            ))}
          </div>
        </div>

        {/* Buttons */}
        <div className="flex justify-between mt-4">
          <button className="btn btn-outline btn-success">Edit Profile</button>
          <button className="btn btn-error text-white">Deactivate</button>
        </div>
      </div>
    </div>
  );
}
