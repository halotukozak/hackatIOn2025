import { useState } from "react";

export default function RegisterPage() {
    const [step, setStep] = useState(1);
    const [form, setForm] = useState({
        gender: "",
        fullName: "",
        age: "",
        bio: "",
        hobbies: [] as string[],
    });

    const handleChange = (key: string, value: string | string[]) => {
        setForm((prev) => ({ ...prev, [key]: value }));
    };

    const toggleHobby = (hobby: string) => {
        setForm((prev) => ({
            ...prev,
            hobbies: prev.hobbies.includes(hobby)
                ? prev.hobbies.filter((h) => h !== hobby)
                : [...prev.hobbies, hobby],
        }));
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        console.log("Form data:", form);
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-base-200 p-4">
            <form
                onSubmit={handleSubmit}
                className="w-full max-w-sm bg-base-100 p-6 rounded shadow space-y-4"
            >
                {step === 1 && (
                    <>
                        <h2 className="text-2xl font-bold text-center">Step 1: Basic Info</h2>

                        <select
                            className="select select-bordered w-full"
                            value={form.gender}
                            onChange={(e) => handleChange("gender", e.target.value)}
                            required
                        >
                            <option value="">Select gender</option>
                            <option>Male</option>
                            <option>Female</option>
                            <option>Other</option>
                        </select>

                        <input
                            type="text"
                            placeholder="Full Name"
                            className="input input-bordered w-full"
                            value={form.fullName}
                            onChange={(e) => handleChange("fullName", e.target.value)}
                            required
                        />

                        <input
                            type="number"
                            placeholder="Age"
                            className="input input-bordered w-full"
                            value={form.age}
                            onChange={(e) => handleChange("age", e.target.value)}
                            required
                        />

                        <button
                            type="button"
                            className="btn btn-primary w-full"
                            onClick={() => setStep(2)}
                            disabled={!form.gender || !form.fullName || !form.age}
                        >
                            Next
                        </button>
                    </>
                )}

                {step === 2 && (
                    <>
                        <h2 className="text-2xl font-bold text-center">Step 2: About You</h2>

                        <textarea
                            className="textarea textarea-bordered w-full"
                            placeholder="Short bio"
                            value={form.bio}
                            onChange={(e) => handleChange("bio", e.target.value)}
                            required
                        />

                        <label className="font-semibold">Hobbies</label>
                        <div className="flex flex-wrap gap-2">
                            {["Music", "Sports", "Reading", "Gaming", "Cooking", "Travel"].map((hobby) => (
                                <label key={hobby} className="cursor-pointer label">
                                    <input
                                        type="checkbox"
                                        className="checkbox mr-2"
                                        checked={form.hobbies.includes(hobby)}
                                        onChange={() => toggleHobby(hobby)}
                                    />
                                    {hobby}
                                </label>
                            ))}
                        </div>

                        <button type="submit" className="btn btn-success w-full mt-4">
                            Register
                        </button>
                        <button
                            type="button"
                            className="btn btn-ghost w-full"
                            onClick={() => setStep(1)}
                        >
                            Back
                        </button>
                    </>
                )}
            </form>
        </div>
    );
}
