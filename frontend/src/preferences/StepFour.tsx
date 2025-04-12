type StepFourProps = {
    form: {
        smoking: string;
        drinking: string;
    };
    onChange: (field: string, value: string) => void;
    onNext: () => void;
    onBack: () => void;
};

export default function StepFour({ form, onChange, onNext, onBack }: StepFourProps) {
    const isNextDisabled = !form.smoking || !form.drinking;

    return (
        <div className="w-full max-w-sm mx-auto p-4 space-y-4">
            <h1 className="text-2xl font-bold text-center">Create Your Profile</h1>
            <p className="text-sm text-center text-gray-500">Step 4 of 5: Habits</p>

            <progress className="progress progress-primary w-full" value={80} max={100}></progress>

            {/* Smoking Habits */}
            <div className="form-control">
                <span className="label-text mb-2 block">Smoking Habits</span>
                {['Non smoker', 'Occasional smoker', 'Regular smoker'].map((option) => (
                    <label key={option} className="label cursor-pointer">
                        <input
                            type="radio"
                            name="smoking"
                            className="radio mr-2"
                            value={option}
                            checked={form.smoking === option}
                            onChange={(e) => onChange("smoking", e.target.value)}
                        />
                        <span className="label-text">{option}</span>
                    </label>
                ))}
            </div>

            {/* Drinking Habits */}
            <div className="form-control">
                <span className="label-text mb-2 block">Drinking Habits</span>
                {['Non drinker', 'Social drinker', 'Regular drinker'].map((option) => (
                    <label key={option} className="label cursor-pointer">
                        <input
                            type="radio"
                            name="drinking"
                            className="radio mr-2"
                            value={option}
                            checked={form.drinking === option}
                            onChange={(e) => onChange("drinking", e.target.value)}
                        />
                        <span className="label-text">{option}</span>
                    </label>
                ))}
            </div>

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
