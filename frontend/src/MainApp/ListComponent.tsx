import React, { useState } from "react";
import { CheckIcon, UserIcon, XMarkIcon } from "@heroicons/react/24/solid";
import { personalityLabels, UserShow } from "../types/user.ts";
import UserDetailModal from "./UserDetailModal.tsx";
import { MatchStatus } from "../types/match.ts";
import { swipeAck, swipeNack } from "../apis/matches.ts";

type ViewProps = {
  user: UserShow;
  match: MatchStatus;
  refetchUsers: () => void;
};

const ListComponent: React.FC<ViewProps> = ({ user, match, refetchUsers }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const handleAck = async (candidate_id: number) => {
    try {
      const userId = localStorage.getItem("user_id");
      if (!userId) throw new Error("User ID not found");
      await swipeAck(Number(userId), candidate_id);
      await refetchUsers();
    } catch (err) {
      console.error("Failed to send accept request:", err);
      alert("Something went wrong while send accept.");
    }
  };

  const handleNack = async (candidate_id: number) => {
    try {
      const userId = localStorage.getItem("user_id");
      if (!userId) throw new Error("User ID not found");
      await swipeNack(Number(userId), candidate_id);
      await refetchUsers();
    } catch (err) {
      console.error("Failed to send accept request:", err);
      alert("Something went wrong while send accept.");
    }
  };
  return (
    <div className=" w-full">
      <div
        className="w-full bg-white shadow-lg rounded-lg flex flex-col p-2"
        onClick={() => {
          if (!isModalOpen) setIsModalOpen(true);
        }}
      >
        <div className="flex justify-between">
          <div className="flex space-x-4 flex-col">
            <div className="flex items-start space-x-4">
              <UserIcon className="w-16 h-16 grow rounded-full border-2 border-neutral-content justify-stretch" />
              <div className="flex w-full items-center">
                <div>
                  <h2 className="text-xl font-semibold">
                    {user.name}, {user.age}
                  </h2>
                  <p className="text-sm text-gray-600">
                    {user.info.faculty} • Year {user.info.yearOfStudy}
                  </p>
                </div>
              </div>
            </div>
          </div>
          <div className="badge whitespace-nowrap text-nowrap bg-green-200 text-green-700 justify-end">
            {user.match}% Match
          </div>
        </div>

        {match === MatchStatus.ContactInfo && (
          <div className="flex justify-end">
            <div className="flex space-x-2">
              <h2 className="text-ml font-semibold">Contact:</h2>
              <p className="text-ml text-nowrap text-gray-600">{user.email}</p>
            </div>
          </div>
        )}
        {match === MatchStatus.Received && (
          <div className="flex justify-end">
            <div className="flex justify-center gap-4 -mt-5">
              <button
                onClick={(e) => {
                  e.stopPropagation();
                  handleNack(user.id);
                }}
                className="btn btn-circle btn-outline text-red-500 border-red-300 hover:border-red-500"
              >
                <XMarkIcon className="h-6 w-6  hover:text-red-500" />
              </button>
              <button
                onClick={(e) => {
                  e.stopPropagation();
                  handleAck(user.id);
                }}
                className="btn btn-circle btn-success text-white"
              >
                <CheckIcon className="h-6 w-6 text-white-500" />
              </button>
            </div>
          </div>
        )}
        {match === MatchStatus.View && (
          <div className="flex space-x-4 justify-between">
            <div className="flex space-x-2">
              <h2 className="text-ml font-semibold">Sleep:</h2>
              <p className="text-ml text-nowrap text-gray-600">
                {user.info.sleepSchedule[0]} - {user.info.sleepSchedule[1]}
              </p>
            </div>
            <div className="flex space-x-2">
              <h2 className="text-ml font-semibold">Personality:</h2>
              <p className="text-ml text-gray-600">
                {personalityLabels[user.info.personalityType]}
              </p>
            </div>
          </div>
        )}
      </div>

      <UserDetailModal
        user={user}
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        match={match}
        handleAck={handleAck}
        handleNack={handleNack}
      />
    </div>
  );
};

export default ListComponent;
