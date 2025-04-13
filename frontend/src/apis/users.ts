import {
  UserShow,
  PersonalityType,
  Drink,
  Smoke,
  RelationshipStatus,
} from "../types/user";
import { User } from "../rest/model";

export function parseUserFromBackend(raw: User): UserShow {
  return {
    email: raw.email,
    fullName: raw.info.fullName,
    age: raw.info.age,
    info: {
      description: raw.info.description,
      sleepSchedule: [
        raw.info.sleepSchedule.first,
        raw.info.sleepSchedule.second,
      ],
      hobbies: raw.info.hobbies.join(", "),
      smoke: raw.info.smoke ? Smoke.Smoker : Smoke.NonSmoker,
      drink: raw.info.drink ? Drink.SocialDrinker : Drink.NonDrinker,
      personalityType: raw.info.personalityType as PersonalityType,
      yearOfStudy: raw.info.yearOfStudy,
      faculty: raw.info.faculty,
      relationshipStatus: raw.info.relationshipStatus as RelationshipStatus,
    },
    preferences: raw.preferences,
  };
}

// Fetch and parse a fake user from backend
export const getFakeUser = async (userId: number): Promise<UserShow> => {
  try {
    const res = await fetch(`http://0.0.0.0:8080/user/${userId}`);

    if (!res.ok) {
      throw new Error(`Failed to fetch user: ${res.status}`);
    }

    const rawData: User = await res.json();
    return parseUserFromBackend(rawData);
  } catch (error) {
    console.error("Error in getFakeUser:", error);
    throw error;
  }
};
