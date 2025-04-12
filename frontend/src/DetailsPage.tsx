import React, { useState } from "react";
import UserDetailModal from "./UserDetailModal";
import {
  User,
  Smoke,
  Drink,
  PersonalityType,
  RelationshipStatus,
} from "./types/user";

const DetailsPage: React.FC = () => {
  const [isModalOpen, setIsModalOpen] = useState(true);

  const user: User = {
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

  return (
    <>
      <button className="btn btn-primary" onClick={() => setIsModalOpen(true)}>
        Show Profile
      </button>

      <UserDetailModal
        user={user}
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
      />
    </>
  );
};

export default DetailsPage;
