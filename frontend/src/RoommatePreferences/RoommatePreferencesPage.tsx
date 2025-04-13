import { useState } from "react";
import StepOne from "./StepOne.tsx"
import StepTwo from "./StepTwo.tsx"

export default function RoommateReferencesPage() {
    const [step, setStep] = useState(1);
    const [form, setForm] = useState({
        matchYear: true,
        matchDepartment: true,
        matchSleepSchedule: true,
        MatchHobbies: true,
        relationshipPreference: "",
        personalityPreference: 50,
        smokingPreference: "",
        drinkingPreference: "",
    });

    const handleChange = (key: string, value: string | number | boolean | null) => {
        setForm((prev) => ({ ...prev, [key]: value }));
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
                    <StepOne form={form} onChange={handleChange} onNext={() => setStep(2)}/>
                )}

                {step === 2 && (
                    <StepTwo form={form} onChange={handleChange} onBack={() => setStep(1)} onSubmit={() => handleSubmit}/>
                )}

                {/*{step === 3 && (*/}
                {/*    <StepThree form={form} onChange={handleChange} onNext={() => setStep(4)} onBack={() => setStep(2)}/>*/}
                {/*)}*/}

                {/*{step === 4 && (*/}
                {/*    <StepFour form={form} onChange={handleChange} onNext={() => setStep(5)} onBack={() => setStep(3)} />*/}
                {/*)}*/}

                {/*{step == 5 && (*/}
                {/*    <StepFive form={form} hobbies={hobbies} onChange={handleChange} onBack={() => setStep(4)} onSubmit={() => handleSubmit} />*/}
                {/*)}*/}
            </form>
        </div>
    );
}
