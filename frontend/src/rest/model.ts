export interface User {
  email: string;
  info: Info;
  preferences: Preferences;
}

export interface Info {
  name: string;
  surname: string;
  age: number;
  description: string;
  sleepSchedule: Pair;
  hobbies: Hobby[];
  smoke: boolean;
  drink: boolean;
  personalityType: number;
  yearOfStudy: number;
  faculty: Faculty;
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

export enum Faculty {
  WILiGZ = "WILiGZ",
  WIMiIP = "WIMiIP",
  WEAIIB = "WEAIIB",
  WIET = "WIET",
  WIMiR = "WIMiR",
  WGGiOS = "WGGiOS",
  WGGiIS = "WGGiIS",
  WIMiC = "WIMiC",
  WO = "WO",
  WMN = "WMN",
  WWNiG = "WWNiG",
  WZ = "WZ",
  WEiP = "WEiP",
  WFiIS = "WFiIS",
  WMS = "WMS",
  WH = "WH",
  WI = "WI",
  WTK = "WTK",
}

export enum Hobby {
  basketball = "basketball",
  football = "football",
  volleyball = "volleyball",
  swimming = "swimming",
  running = "running",
  cycling = "cycling",
  hiking = "hiking",
  climbing = "climbing",
  skiing = "skiing",
  music = "music",
  dance = "dance",
  cooking = "cooking",
  painting = "painting",
  photography = "photography",
}

export interface AdditionalInfoRequest {
  userId: number;
  info: Info;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
}

export interface DeleteRequest {
  userId: number;
}