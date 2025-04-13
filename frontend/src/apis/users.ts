import {
  UserShow,
  PersonalityType,
  Drink,
  Smoke,
  RelationshipStatus,
} from "../types/user";
import { User } from "../rest/model";

// Convert backend raw user data to strongly typed User object
// Converts number (e.g., 22) to "22:00" format
const formatHour = (hour: number): string => {
  const h = hour % 24;
  return `${h.toString().padStart(2, "0")}:00`;
};

export function parseUserFromBackend(raw: User): UserShow {
  return {
    email: raw.email,
    name: raw.info.name,
    surname: raw.info.surname,
    age: raw.info.age,
    info: {
      description: raw.info.description,
      sleepSchedule: [
        formatHour(raw.info.sleepSchedule.first),
        formatHour(raw.info.sleepSchedule.second),
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
export const getUserById = async (userId: number): Promise<UserShow> => {
  try {
    const res = await fetch(`http://0.0.0.0:8080/user/${userId}`);

    if (!res.ok) {
      throw new Error(`Failed to fetch user: ${res.status}`);
    }

    const rawData: User = await res.json();
    return parseUserFromBackend(rawData);
  } catch (error) {
    console.error("Error in getUserById:", error);
    throw error;
  }
};

export const getAllUsers = async (): Promise<UserShow[]> => {
  try {
    const res = await fetch("http://0.0.0.0:8080/users");

    if (!res.ok) {
      throw new Error(`Failed to fetch users: ${res.status}`);
    }

    const rawUsers: User[] = await res.json();
    return rawUsers.map(parseUserFromBackend);
  } catch (error) {
    console.error("Error in getAllUsers:", error);
    throw error;
  }
};
