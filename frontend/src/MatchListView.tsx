import React, {useState} from "react";
import {CheckIcon, UserIcon, XMarkIcon} from "@heroicons/react/24/solid";
import {
    User
} from "./types/user";
import UserDetailModal from "./UserDetailModal.tsx";
import {MatchStatus} from "./types/match.ts";

type MatchViewProps = {
    user: User;
    match: MatchStatus
};


const MatchListView: React.FC<MatchViewProps> = ({user, match}) => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    return (
            <div className=" w-full relative" onClick={() => setIsModalOpen(true)}>
                <div className="w-full bg-white shadow-lg rounded-lg flex flex-col p-2">
                    <div className="flex justify-between">
                        <div className="flex space-x-4 flex-col">
                            <div className="flex items-start space-x-4">
                                <UserIcon className="w-16 h-16 grow rounded-full border-2 border-neutral-content justify-stretch" />
                                <div className="flex w-full items-center">
                                    <div>
                                        <h2 className="text-xl font-semibold">{user.name}, {user.age}</h2>
                                        <p className="text-sm text-gray-600">{user.info.faculty} • Year {user.info.yearOfStudy}</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="badge whitespace-nowrap text-nowrap bg-green-200 text-green-700 justify-end">
                            87% Match
                        </div>
                    </div>

                    <div className="flex justify-end">
                        {match === MatchStatus.ContactInfo && (
                            <div className="flex space-x-2">
                                <h2 className="text-ml font-semibold">Contact:</h2>
                                <p className="text-ml text-nowrap text-gray-600">{user.email}</p>
                            </div>
                        )}
                        {match === MatchStatus.Received && (
                            <div className="flex justify-center gap-4 ">
                                <button className="btn btn-circle btn-outline text-red-500 border-red-300 hover:border-red-500">
                                    <XMarkIcon className="h-6 w-6  hover:text-red-500" />
                                </button>
                                <button className="btn btn-circle btn-success text-white">
                                    <CheckIcon className="h-6 w-6 text-white-500" />
                                </button>
                            </div>
                        )}
                    </div>
            </div>

            <UserDetailModal
                user={user}
                isOpen={isModalOpen}
                onClose={() => setIsModalOpen(false)}
            />
        </div>
    );
};

export default MatchListView;