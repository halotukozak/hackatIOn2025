import {Hobby} from "../rest/model.ts";

type StepFiveProps = {
    hobbies: Hobby[];
    form: {
        interests: Hobby[];
        relationship: string;
    };
    onChange: (field: string, value: string | Hobby[]) => void;
    onBack: () => void;
    onSubmit: () => void;
};

export default function StepFive({ form, hobbies, onChange, onBack, onSubmit }: StepFiveProps) {
    const toggleInterest = (interest: Hobby) => {
        const updated = form.interests.includes(interest)
            ? form.interests.filter((i) => i !== interest)
            : [...form.interests, interest];
        onChange("interests", updated);
    };

    const isSubmitDisabled = form.interests.length === 0 || !form.relationship;

    return (
        <div className="w-full max-w-sm mx-auto p-4 space-y-4">
            <h1 className="text-2xl font-bold text-center">Create Your Profile</h1>
            <p className="text-sm text-center text-gray-500">Step 5 of 5: Interests & Relationships</p>

            <progress className="progress progress-primary w-full" value={100} max={100}></progress>

            {/* Interests */}
            <div className="form-control">
                <span className="label-text mb-2">Interests & Hobbies</span>
                <div className="grid grid-cols-2 gap-2">
                    {hobbies.map((interest) => (
                        <label key={interest} className="cursor-pointer label">
                            <input
                                type="checkbox"
                                className="checkbox mr-2"
                                checked={form.interests.includes(interest)}
                                onChange={() => toggleInterest(interest)}
                            />
                            {interest}
                        </label>
                    ))}
                </div>
            </div>

            {/* Relationship Status */}
            <label className="form-control w-full">
                <span className="label-text mb-1">Relationship Status</span>
                <select
                    className="select select-bordered w-full"
                    value={form.relationship}
                    onChange={(e) => onChange("relationship", e.target.value)}
                    required
                >
                    <option value="">Select status</option>
                    <option value="1">Single</option>
                    <option value="2">In a relationship</option>
                    <option value="3">It's complicated</option>
                </select>
            </label>

            <div className="flex justify-between pt-4">
                <button className="btn btn-outline" onClick={onBack}>Back</button>
                <button
                    className="btn btn-success"
                    onClick={onSubmit}
                    disabled={isSubmitDisabled}
                >
                    Finish
                </button>
            </div>
        </div>
    );
}
