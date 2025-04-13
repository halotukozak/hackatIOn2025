type StepThreeProps = {
    form: {
        sleepFrom: string;
        sleepTo: string;
        personality: number;
        lifestyleBio: string;
    };
    onChange: (field: string, value: string | number) => void;
    onNext: () => void;
    onBack: () => void;
};

export default function StepThree({ form, onChange, onNext, onBack }: StepThreeProps) {
    const isNextDisabled = !form.sleepFrom || !form.sleepTo;

    return (
        <div className="w-full max-w-sm mx-auto p-4 space-y-4">
            <h1 className="text-2xl font-bold text-center">Provide Your Information</h1>
            <p className="text-sm text-center text-gray-500">Step 3 of 5: Lifestyle</p>

            <progress className="progress progress-primary w-full" value={60} max={100}></progress>

            {/* Sleep Schedule */}
            <label className="form-control w-full">
                <span className="label-text mb-1">When do you usually go to bed?</span>
                <input
                    type="time"
                    className="input input-bordered w-full"
                    value={form.sleepFrom}
                    onChange={(e) => onChange("sleepFrom", e.target.value)}
                    required
                />
            </label>

            <label className="form-control w-full">
                <span className="label-text mb-1">When do you usually wake up?</span>
                <input
                    type="time"
                    className="input input-bordered w-full"
                    value={form.sleepTo}
                    onChange={(e) => onChange("sleepTo", e.target.value)}
                    required
                />
            </label>

            {/* Personality */}
            <label className="form-control w-full">
                <span className="label-text mb-1">Personality Type</span>
                <input
                    type="range"
                    min={0}
                    max={100}
                    value={form.personality}
                    onChange={(e) => onChange("personality", Number(e.target.value))}
                    className="range [--range-fill:0] text-primary"
                />
                <div className="w-full flex justify-between text-xs px-1">
                    <span>Introvert</span>
                    <span>Extravert</span>
                </div>
            </label>

            {/* Brief Bio */}
            <label className="form-control w-full">
                <span className="label-text mb-1">Brief Bio</span>
                <textarea
                    rows={3}
                    className="textarea textarea-bordered w-full"
                    value={form.lifestyleBio}
                    onChange={(e) => onChange("lifestyleBio", e.target.value)}
                    required
                />
            </label>

            <div className="flex justify-between pt-4">
                <button className="btn btn-outline" onClick={onBack}>Back</button>
                <button
                    className="btn btn-primary"
                    onClick={onNext}
                    disabled={isNextDisabled}
                >
                    Next
                </button>
            </div>
        </div>
    );
}
