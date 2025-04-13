type StepTwoProps = {
    form: {
        personalityPreference: number | null;
        smokingPreference: string;
        drinkingPreference: string;
    };
    onChange: (field: string, value: number | string | null) => void;
    onSubmit: () => void;
    onBack: () => void;
};

export default function StepTwo({ form, onChange, onSubmit, onBack }: StepTwoProps) {
    return (
        <div className="w-full max-w-sm mx-auto p-4 space-y-4">
            <h1 className="text-2xl font-bold text-center">Roommate Preferences</h1>
            <p className="text-sm text-center text-gray-500">Step 2 of 2: Lifestyle compatibility</p>

            <progress className="progress progress-primary w-full" value={100} max={100}></progress>

            {/* Personality Preference */}
            <div className="form-control">
                <span className="label-text mb-1">Preferred personality</span>
                <input
                    type="range"
                    min={0}
                    max={100}
                    step={1}
                    value={form.personalityPreference ?? 50}
                    onChange={(e) => onChange("personalityPreference", Number(e.target.value))}
                    className="range [--range-fill:0]"
                />
                <div className="w-full flex justify-between text-xs px-1">
                    <span>Introvert</span>
                    <span>Extravert</span>
                </div>
                <button
                    type="button"
                    className={form.personalityPreference ? "btn btn-xs mt-1 btn-outline" : "btn btn-xs mt-1 btn-outline btn-primary"}
                    onClick={() => onChange("personalityPreference", null)}
                >
                    I don't care
                </button>
            </div>

            {/* Smoking Preference */}
            <label className="form-control w-full">
                <span className="label-text mb-1">Smoking habits</span>
                <select
                    className="select select-bordered w-full"
                    value={form.smokingPreference}
                    onChange={(e) => onChange("smokingPreference", e.target.value)}
                >
                    <option value="">Don't care</option>
                    <option value="0">Non smoker</option>
                    <option value="1">Occasional smoker</option>
                    <option value="2">Regular smoker</option>
                </select>
            </label>

            {/* Drinking Preference */}
            <label className="form-control w-full">
                <span className="label-text mb-1">Drinking habits</span>
                <select
                    className="select select-bordered w-full"
                    value={form.drinkingPreference}
                    onChange={(e) => onChange("drinkingPreference", e.target.value)}
                >
                    <option value="">Don't care</option>
                    <option value="0">Non drinker</option>
                    <option value="1">Social drinker</option>
                    <option value="2">Regular drinker</option>
                </select>
            </label>

            <div className="flex justify-between pt-4">
                <button className="btn btn-outline" onClick={onBack}>Back</button>
                <button className="btn btn-primary" onClick={onSubmit}>Submit</button>
            </div>
        </div>
    );
}
