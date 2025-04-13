import { useState } from "react";
import StepOne from "./StepOne.tsx"
import StepTwo from "./StepTwo.tsx"
import {setPreferences} from "../apis/preferences.ts";
import {useNavigate} from "react-router-dom";

export default function RoommateReferencesPage() {
    const navigate = useNavigate();
    const [step, setStep] = useState(1);
    const [form, setForm] = useState({
        matchYear: true,
        matchDepartment: true,
        matchSleepSchedule: true,
        matchHobbies: true,
        relationshipPreference: "",
        personalityPreference: 50,
        smokingPreference: "",
        drinkingPreference: "",
    });

    const handleChange = (key: string, value: string | number | boolean | null) => {
        setForm((prev) => ({ ...prev, [key]: value }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await setPreferences(form);
            navigate('/discover');
        } catch (error) {
            console.error("Error submitting preferences:", error);
            alert("Failed to submit preferences");
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-base-200 p-4">
            <form
                onSubmit={handleSubmit}
                className="w-full max-w-sm bg-base-100 p-6 rounded shadow space-y-4"
            >
                {step === 1 && (
                    <StepOne form={form} onChange={handleChange} onNext={() => setStep(2)}/>
                )}

                {step === 2 && (
                    <StepTwo form={form} onChange={handleChange} onBack={() => setStep(1)} onSubmit={() => handleSubmit}/>
                )}
            </form>
        </div>
    );
}
