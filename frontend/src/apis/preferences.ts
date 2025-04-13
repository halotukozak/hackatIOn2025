import {Info, Faculty, Hobby, Preferences} from "../rest/model.ts";
import {base_url} from "./base.ts";


export const getHobbies = async (): Promise<Hobby[]> => {
    const res = await fetch(base_url() + "/registration/available-hobbies");
    if (!res.ok) throw new Error("Failed to fetch hobbies");
    return res.json();
};

export const getDepartments = async (): Promise<Faculty[]> => {
    const res = await fetch(base_url() + "/registration/available-departments");
    if (!res.ok) throw new Error("Failed to fetch departments");
    return res.json();
};

export async function setInfo(form: {
    gender: string;
    fullName: string;
    age: string;
    year: string;
    department: string;
    sleepFrom: string;
    sleepTo: string;
    personality: number;
    lifestyleBio: string;
    smoking: string;
    drinking: string;
    interests: string[];
    relationship: string
}): Promise<void> {
    const info: Info = {
        fullName: form.fullName,
        gender: +form.gender,
        age: +form.age,
        description: form.lifestyleBio,
        sleepSchedule: {
            first: form.sleepFrom,
            second: form.sleepTo
        },
        hobbies: form.interests.map((hobby) => hobby as Hobby),
        smoke: +form.smoking,
        drink: +form.drinking,
        personalityType: form.personality,
        yearOfStudy: +form.year,
        faculty: form.department as Faculty,
        relationshipStatus: +form.relationship
    }

    const res = await fetch(base_url() + "/registration/additional-info", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({userId: localStorage.getItem("user_id"), info: info}),
    });

    if (!res.ok) {
        throw new Error("Request failed");
    }
}

export async function setPreferences(form: {
    matchYear: boolean;
    matchDepartment: boolean;
    matchSleepSchedule: boolean;
    matchHobbies: boolean;
    relationshipPreference: string;
    personalityPreference: number | null;
    smokingPreference: string;
    drinkingPreference: string;
}): Promise<void> {
    const preferences: Preferences = {
        sleepScheduleMatters: form.matchSleepSchedule,
        hobbiesMatters: form.matchHobbies,
        smokingImportance: form.smokingPreference ? +form.smokingPreference : null,
        drinkImportance: form.drinkingPreference ? +form.drinkingPreference : null,
        personalityTypeImportance: form.personalityPreference ? +form.personalityPreference : null,
        yearOfStudyMatters: form.matchYear,
        facultyMatters: form.matchDepartment,
        relationshipStatusImportance: form.relationshipPreference ? +form.relationshipPreference : null
    }

    const res = await fetch(base_url() + "/registration/additional-preferences", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({userId: localStorage.getItem("user_id"), preferences: preferences}),
    });

    if (!res.ok) {
        throw new Error("Request failed");
    }
}