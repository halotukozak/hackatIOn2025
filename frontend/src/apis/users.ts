import {
  UserShow,
  PersonalityType,
  Drink,
  Smoke,
  RelationshipStatus,
} from "../types/user";
import { User } from "../rest/model";
import {base_url} from "./base.ts";

// Convert backend raw user data to strongly typed User object

export function parseRelationship(relationship: number | null): RelationshipStatus {
  if (relationship == null) {
    return RelationshipStatus.ItsComplicated;
  }
  return relationship as RelationshipStatus;
}

export function parsePersonalityType(personalityType: number): PersonalityType {
  if (personalityType <= 33) {
    return PersonalityType.Introverted;
  }
  if (personalityType <= 64) {
    return PersonalityType.Ambiverted;
  }
  return PersonalityType.Extraverted;
}

export function parseUserFromBackend(raw: User): UserShow {
  return {
    id: raw.id,
    email: raw.email,
    name: raw.info.fullName.split(" ")[0],
    surname: raw.info.fullName.split(" ")[1],
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
      personalityType: parsePersonalityType(raw.info.personalityType),
      yearOfStudy: raw.info.yearOfStudy,
      faculty: raw.info.faculty,
      relationshipStatus: parseRelationship(raw.info.relationshipStatus),
    },
    preferences: raw.preferences,
  };
}

// Fetch and parse a fake user from backend
export const getUserById = async (userId: number): Promise<UserShow> => {
  try {
    const res = await fetch(base_url() + `/user/${userId}`);

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
    const res = await fetch(base_url() + "/users");

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
