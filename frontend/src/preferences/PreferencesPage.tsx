import { useEffect, useState } from "react";
import {getHobbies, getDepartments, setInfo} from "../apis/preferences.ts"
import StepOne from "./StepOne.tsx"
import StepTwo from "./StepTwo.tsx"
import StepThree from "./StepThree.tsx"
import StepFour from "./StepFour.tsx"
import StepFive from "./StepFive.tsx"
import {Faculty, Hobby} from "../rest/model.ts";
import {useNavigate} from "react-router-dom";

export default function ReferencesPage() {
    const navigate = useNavigate();
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

    const [hobbies, setHobbies] = useState<Hobby[]>([]);
    const [departments, setDepartments] = useState<Faculty[]>([]);

    useEffect(() => {
        getHobbies().then(setHobbies).catch(console.error);
        getDepartments().then(setDepartments).catch(console.error);
    }, []);

    const handleChange = (key: string, value: string | number | Hobby[]) => {
        setForm((prev) => ({ ...prev, [key]: value }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await setInfo(form);
            navigate('/preferences');
        } catch (error) {
            console.error("Error submitting preferences:", error);
            alert("Failed to submit information");
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
