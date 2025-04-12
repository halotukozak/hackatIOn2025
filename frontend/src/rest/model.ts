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