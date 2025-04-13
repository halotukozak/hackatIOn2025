type StepOneProps = {
    form: {
        matchYear: boolean;
        matchDepartment: boolean;
        matchSleepSchedule: boolean;
        matchHobbies: boolean;
        relationshipPreference: string;
    };
    onChange: (field: string, value: boolean | string) => void;
    onNext: () => void;
};

export default function StepOne({ form, onChange, onNext }: StepOneProps) {
    const isNextDisabled = false; // everything optional

    return (
        <div className="w-full max-w-sm mx-auto p-4 space-y-4">
            <h1 className="text-2xl font-bold text-center">Roommate Preferences</h1>
            <p className="text-sm text-center text-gray-500">Step 1 of 2: What matters to you?</p>

            <progress className="progress progress-primary w-full" value={50} max={100}></progress>

            <div className="form-control">
                <label className="cursor-pointer label flex justify-between">
                    <span className="label-text">Match year of study</span>
                    <input
                        type="checkbox"
                        className="checkbox"
                        checked={form.matchYear}
                        onChange={(e) => onChange("matchYear", e.target.checked)}
                    />
                </label>

                <label className="cursor-pointer label flex justify-between">
                    <span className="label-text">Match department</span>
                    <input
                        type="checkbox"
                        className="checkbox"
                        checked={form.matchDepartment}
                        onChange={(e) => onChange("matchDepartment", e.target.checked)}
                    />
                </label>

                <label className="cursor-pointer label flex justify-between">
                    <span className="label-text">Match sleep schedule</span>
                    <input
                        type="checkbox"
                        className="checkbox"
                        checked={form.matchSleepSchedule}
                        onChange={(e) => onChange("matchSleepSchedule", e.target.checked)}
                    />
                </label>

                <label className="cursor-pointer label flex justify-between">
                    <span className="label-text">Match hobbies</span>
                    <input
                        type="checkbox"
                        className="checkbox"
                        checked={form.matchSleepSchedule}
                        onChange={(e) => onChange("matchHobbies", e.target.checked)}
                    />
                </label>

                <label className="form-control w-full">
                    <span className="label-text mb-1">Preferred relationship status</span>
                    <select
                        className="select select-bordered w-full"
                        value={form.relationshipPreference}
                        onChange={(e) => onChange("relationshipPreference", e.target.value)}
                    >
                        <option value="">Don't care</option>
                        <option value="1">Single</option>
                        <option value="2">In a relationship</option>
                    </select>
                </label>
            </div>

            <div className="flex justify-end pt-4">
                <button className="btn btn-primary" onClick={onNext} disabled={isNextDisabled}>
                    Next
                </button>
            </div>
        </div>
    );
}
