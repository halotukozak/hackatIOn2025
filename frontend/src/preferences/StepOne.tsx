type StepOneProps = {
    form: { gender: string; fullName: string; age: string };
    onChange: (field: string, value: string) => void;
    onNext: () => void;
};

export default function StepOne({ form, onChange, onNext }: StepOneProps) {
    const isNextDisabled = !form.gender || !form.fullName || !form.age;

    return (
        <div className="w-full max-w-sm mx-auto p-4 space-y-4">
            <h1 className="text-2xl font-bold text-center">Create Your Profile</h1>
            <p className="text-sm text-center text-gray-500">Step 1 of 5: Basic information</p>

            {/* Progress Bar */}
            <progress className="progress progress-primary w-full" value={20} max={100}></progress>

            {/* Gender */}
            <label className="form-control w-full">
                <span className="label-text mb-1">Gender</span>
                <select
                    className="select select-bordered w-full"
                    value={form.gender}
                    onChange={(e) => onChange("gender", e.target.value)}
                    required
                >
                    <option value="">Select gender</option>
                    <option value="1">Male</option>
                    <option value="2">Female</option>
                </select>
            </label>

            {/* Full Name */}
            <label className="form-control w-full">
                <span className="label-text mb-1">Full Name</span>
                <input
                    type="text"
                    className="input input-bordered w-full"
                    value={form.fullName}
                    onChange={(e) => onChange("fullName", e.target.value)}
                    required
                />
            </label>

            {/* Age */}
            <label className="form-control w-full">
                <span className="label-text mb-1">Age</span>
                <input
                    type="number"
                    className="input input-bordered w-full"
                    value={form.age}
                    onChange={(e) => onChange("age", e.target.value)}
                    required
                />
            </label>

            {/* Buttons */}
            <div className="flex justify-between pt-4">
                <button className="btn btn-disabled">Back</button>
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
