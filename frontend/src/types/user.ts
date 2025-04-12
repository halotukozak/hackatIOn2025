// src/types/user.ts

export enum Smoke {
  NonSmoker = 0,
  Smoker = 1,
}

export enum Drink {
  NonDrinker = 0,
  SocialDrinker = 1,
  FrequentDrinker = 2,
}

export enum PersonalityType {
  Introverted = 0,
  Ambiverted = 1,
  Extraverted = 2,
}

export enum RelationshipStatus {
  Single = 0,
  InRelationship = 1,
}

// Interfaces
export interface Info {
  description: string;
  sleepSchedule: [String, String];
  hobbies: string;
  smoke: Smoke;
  drink: Drink;
  personalityType: PersonalityType;
  yearOfStudy: number;
  faculty: string;
  relationshipStatus: RelationshipStatus;
}

export interface User {
  name: string;
  surname: string;
  email: string;
  age: number;
  info: Info;
}

export const smokeLabels = {
  [Smoke.NonSmoker]: "Non-smoker",
  [Smoke.Smoker]: "Smoker",
};

export const drinkLabels = {
  [Drink.NonDrinker]: "Non-drinker",
  [Drink.SocialDrinker]: "Social Drinker",
  [Drink.FrequentDrinker]: "Frequent Drinker",
};

export const personalityLabels = {
  [PersonalityType.Introverted]: "Introverted",
  [PersonalityType.Ambiverted]: "Ambiverted",
  [PersonalityType.Extraverted]: "Extraverted",
};

export const relationshipLabels = {
  [RelationshipStatus.Single]: "Single",
  [RelationshipStatus.InRelationship]: "In a Relationship",
};
