import { useEffect, useState } from "react";
import { getHobbies, getDepartments } from "../api.ts"
import StepOne from "./StepOne.tsx"
import StepTwo from "./StepTwo.tsx"
import StepThree from "./StepThree.tsx"
import StepFour from "./StepFour.tsx"
import StepFive from "./StepFive.tsx"

export default function ReferencesPage() {
    const [step, setStep] = useState(1);
    const [form, setForm] = useState({
        gender: "",
        fullName: "",
        age: "",
        year: "",
        department: "",
        sleepFrom: "",
        sleepTo: "",
        personality: 50,
        lifestyleBio: "",
        smoking: "",
        drinking: "",
        interests: [],
        relationship: ""
    });

    const [hobbies, setHobbies] = useState<string[]>([]);
    const [departments, setDepartments] = useState<string[]>([]);

    useEffect(() => {
        getHobbies().then(setHobbies).catch(console.error);
        getDepartments().then(setDepartments).catch(console.error);
    }, []);

    const handleChange = (key: string, value: string | number | string[]) => {
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
                    <StepTwo form={form} departments={departments} onChange={handleChange} onNext={() => setStep(3)} onBack={() => setStep(1)}/>
                )}

                {step === 3 && (
                    <StepThree form={form} onChange={handleChange} onNext={() => setStep(4)} onBack={() => setStep(2)}/>
                )}

                {step === 4 && (
                    <StepFour form={form} onChange={handleChange} onNext={() => setStep(5)} onBack={() => setStep(3)} />
                )}

                {step == 5 && (
                    <StepFive form={form} hobbies={hobbies} onChange={handleChange} onBack={() => setStep(4)} onSubmit={() => handleSubmit} />
                )}
            </form>
        </div>
    );
}
