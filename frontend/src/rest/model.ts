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
  smoke: boolean;
  drink: boolean;
}

export interface Preferences {
  smoke: boolean;
  drink: boolean;
  level: number | null;
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