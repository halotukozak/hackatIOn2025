export default function LoginPage() {
    const handleLogin = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const form = e.currentTarget;
        const email = (form.elements.namedItem("email") as HTMLInputElement).value;
        const password = (form.elements.namedItem("password") as HTMLInputElement).value;

        console.log("Logging in:", { email, password });
    };

    return (
        <div className="flex items-center justify-center min-h-screen bg-base-200">
            <form onSubmit={handleLogin} className="card w-96 bg-base-100 shadow-xl p-6 space-y-4">
                <h2 className="text-2xl font-bold text-center">Login</h2>

                <input
                    name="email"
                    type="email"
                    placeholder="Email"
                    className="input input-bordered w-full"
                    required
                />

                <input
                    name="password"
                    type="password"
                    placeholder="Password"
                    className="input input-bordered w-full"
                    required
                />

                <button type="submit" className="btn btn-primary w-full">
                    Log In
                </button>

                <p className="text-center text-sm">
                    Don’t have an account? <a href="/register" className="link">Register</a>
                </p>
            </form>
        </div>
    );
}
