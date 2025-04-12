export interface User {
  name: string;
  surname: string;
  email: string;
  age: number;
  info: Info;
  preferences: Preferences;
}

export interface Info {
  description: string;
  sleepSchedule: Pair;
  hobbies: string;
  smoke: number;
  drink: number;
  personalityType: number;
  yearOfStudy: number;
  faculty: string;
  relationshipStatus: number;
}

export interface Preferences {
  sleepScheduleMatters: boolean;
  hobbiesMatters: boolean;
  smokingImportance: number;
  drinkImportance: number;
  personalityTypeImportance: number;
  yearOfStudyMatters: boolean;
  facultyMatters: boolean;
  relationshipStatusImportance: number;
}

export interface Pair {
  first: number;
  second: number;
}