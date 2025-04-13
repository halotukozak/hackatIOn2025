import React from "react";
import {XMarkIcon, HeartIcon} from "@heroicons/react/24/solid";
import {
  UserShow,
  smokeLabels,
  drinkLabels,
  personalityLabels, relationshipLabels,
} from "./types/user";
import {MatchStatus} from "./types/match.ts";

type UserDetailModalProps = {
  user: UserShow;
  isOpen: boolean;
  onClose: () => void;
  match: MatchStatus;
};

const UserDetailModal: React.FC<UserDetailModalProps> = ({
  user,
  isOpen,
  onClose,
    match,
}) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-40">
      <div className="bg-white rounded-2xl p-6 w-[95%] max-w-md shadow-xl relative">
        {/* Header */}
        <div className="flex justify-between items-center mb-4">
          <div>
            <h2 className="text-xl font-semibold">
              {user.name}, {user.age}
            </h2>
            <p className="text-sm text-gray-600">
              {user.info.faculty} • Year {user.info.yearOfStudy}
            </p>
          </div>
          <span className="bg-green-200 text-green-700 px-3 py-1 rounded-full text-sm font-medium">
            {user.match}% Match
          </span>
        </div>

        {/* Image Placeholder */}
        <div className="w-full h-48 bg-gray-200 rounded-lg flex items-center justify-center mb-4">
          <span className="text-gray-400">[Image Placeholder]</span>
        </div>

        {/* Info Grid */}
        <div className="grid grid-cols-2 gap-y-3 text-sm mb-4">
          <div>
            <p className="font-semibold">Sleep Schedule</p>
            <p className="text-gray-600">
              {user.info.sleepSchedule[0]} - {user.info.sleepSchedule[1]}
            </p>
          </div>
          <div>
            <p className="font-semibold">Personality</p>
            <p className="text-gray-600">
              {personalityLabels[user.info.personalityType]}
            </p>
          </div>
          <div>
            <p className="font-semibold">Smoking</p>
            <p className="text-gray-600">{smokeLabels[user.info.smoke]}</p>
          </div>
          <div>
            <p className="font-semibold">Drinking</p>
            <p className="text-gray-600">{drinkLabels[user.info.drink]}</p>
          </div>
          <div>
            <p className="font-semibold">Relationship</p>
            <p className="text-gray-600">{relationshipLabels[user.info.relationshipStatus]}</p>
          </div>
        </div>

        {/* Interests */}
        <div className="mb-4">
          <p className="font-semibold mb-1">Interests</p>
          <div className="flex flex-wrap gap-2">
            {user.info.hobbies.split(", ").map((interest, index) => (
              <span key={index} className="badge badge-ghost">
                {interest}
              </span>
            ))}
          </div>
        </div>

        {/* Bio */}
        <div className="mb-4">
          <p className="font-semibold mb-1">Bio</p>
          <p className="text-gray-700 text-sm">{user.info.description}</p>
        </div>

        {/* Action Buttons */}
        {match === MatchStatus.View && (
            <div className="flex justify-center gap-6 mt-4">
              <button className="btn btn-circle btn-outline text-red-500 border-red-300 hover:border-red-500">
                <XMarkIcon className="h-6 w-6  hover:text-red-500" />
              </button>
              <button className="btn btn-circle btn-success text-white">
                <HeartIcon className="h-6 w-6 text-white-500" />
              </button>
            </div>
        )}

        <div className="flex justify-center mt-6">
          <button
            onClick={onClose}
            className="btn btn-outline btn-wide btn-error"
          >
            Close
          </button>
        </div>
      </div>
    </div>
  );
};

export default UserDetailModal;
