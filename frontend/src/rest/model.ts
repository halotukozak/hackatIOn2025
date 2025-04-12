export interface User {
  email: string;
  info: Info;
  preferences: Preferences;
}

export interface Info {
  age: number;
  departament: Departament;
  description: string;
  smoke: boolean;
  drink: boolean;
}

export interface Preferences {
  smoke: boolean;
  drink: boolean;
  level: number;
}

export enum Departament {
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
  preferences: Preferences;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
}