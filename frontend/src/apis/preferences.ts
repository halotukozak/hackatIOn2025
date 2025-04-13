import {Info, Faculty, Hobby, Preferences} from "../rest/model.ts";


export const getHobbies = async (): Promise<Hobby[]> => {
    const res = await fetch("http://127.0.0.1:8080/registration/available-hobbies");
    if (!res.ok) throw new Error("Failed to fetch hobbies");
    return res.json();
};

export const getDepartments = async (): Promise<Faculty[]> => {
    const res = await fetch("http://127.0.0.1:8080/registration/available-departments");
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
        full_name: form.fullName,
        age: parseInt(form.age),
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

    const res = await fetch("http://localhost:8080/registration/additional-data", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(info),
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

    const res = await fetch("http://localhost:8080/registration/additional-info", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(preferences),
    });

    if (!res.ok) {
        throw new Error("Request failed");
    }
}