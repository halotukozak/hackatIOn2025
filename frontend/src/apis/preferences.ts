export const getHobbies = async (): Promise<string[]> => {
    const res = await fetch("http://127.0.0.1:8080/registration/available-hobbies");
    if (!res.ok) throw new Error("Failed to fetch hobbies");
    return res.json();
};

export const getDepartments = async (): Promise<string[]> => {
    const res = await fetch("http://127.0.0.1:8080/registration/available-departments");
    if (!res.ok) throw new Error("Failed to fetch departments");
    return res.json();
};