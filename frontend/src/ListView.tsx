import React, {useState} from "react";
import { UserIcon } from "@heroicons/react/24/solid";
import {
    User,
    personalityLabels,
} from "./types/user";
import UserDetailModal from "./UserDetailModal.tsx";
// import React from "react";

type ListViewProps = {
    user: User;
};


const ListView: React.FC<ListViewProps> = ({user}) => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    return (
        <div className=" w-full" onClick={() => setIsModalOpen(true)}>
        <div className="w-full bg-white shadow-lg rounded-lg flex flex-col p-2 relative">
            <div className="flex justify-between">
                <div className="flex space-x-4 flex-col">
                    <div className="flex items-start justify-start space-x-4">
                        <UserIcon className="w-16 h-16 grow rounded-full border-2 border-neutral-content mb-4 justify-stretch" />
                        <div className="flex w-full items-center justify-start mb-2">
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
            <div className="flex space-x-4 justify-between">
                <div className="flex space-x-2">
                    <h2 className="text-ml font-semibold">Sleep:</h2>
                    <p className="text-ml text-nowrap text-gray-600">{user.info.sleepSchedule[0]} - {user.info.sleepSchedule[1]}</p>
                </div>
                <div className="flex space-x-2">
                    <h2 className="text-ml font-semibold">Personality:</h2>
                    <p className="text-ml text-gray-600">{personalityLabels[user.info.personalityType]}</p>
                </div>
            </div>
        </div>

    <UserDetailModal
        user={user}
        isOpen={isModalOpen}
        onClose={() => {setIsModalOpen(false); console.log(isModalOpen)}}
    />
        </div>
    );
};

export default ListView;