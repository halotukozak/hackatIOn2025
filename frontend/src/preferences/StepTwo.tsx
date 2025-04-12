type StepTwoProps = {
    form: {
        year: string;
        department: string;
        faculty: string;
    };
    onChange: (field: string, value: string) => void;
    onNext: () => void;
    onBack: () => void;
};

export default function StepTwo({ form, onChange, onNext, onBack }: StepTwoProps) {
    const isNextDisabled = !form.year || !form.department || !form.faculty;

    return (
        <div className="w-full max-w-sm mx-auto p-4 space-y-4">
            <h1 className="text-2xl font-bold text-center">Create Your Profile</h1>
            <p className="text-sm text-center text-gray-500">Step 2 of 5: Academic Details</p>

            <progress className="progress progress-primary w-full" value={40} max={100}></progress>

            {/* Year of Study */}
            <label className="form-control w-full">
                <span className="label-text mb-1">Year of Study</span>
                <select
                    className="select select-bordered w-full"
                    value={form.year}
                    onChange={(e) => onChange("year", e.target.value)}
                    required
                >
                    <option value="">Select year</option>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5+">5+</option>
                </select>
            </label>

            {/* Department */}
            <label className="form-control w-full">
                <span className="label-text mb-1">Department</span>
                <select
                    className="select select-bordered w-full"
                    value={form.department}
                    onChange={(e) => onChange("department", e.target.value)}
                    required
                >
                    <option value="">Select department</option>
                    <option value="Computer Science">Computer Science</option>
                    <option value="Business">Business</option>
                    <option value="Engineering">Engineering</option>
                    <option value="Medicine">Medicine</option>
                    <option value="Arts">Arts</option>
                </select>
            </label>

            {/* Faculty */}
            <label className="form-control w-full">
                <span className="label-text mb-1">Faculty</span>
                <select
                    className="select select-bordered w-full"
                    value={form.faculty}
                    onChange={(e) => onChange("faculty", e.target.value)}
                    required
                >
                    <option value="">Select faculty</option>
                    <option value="Science">Science</option>
                    <option value="Humanities">Humanities</option>
                    <option value="Law">Law</option>
                    <option value="Health Sciences">Health Sciences</option>
                </select>
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
