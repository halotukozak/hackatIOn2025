import { useState } from "react";
import { Link } from "react-router-dom";

export default function RegisterPage() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirm, setConfirm] = useState("");

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (password !== confirm) {
            alert("Passwords do not match!");
            return;
        }
        console.log("Registering:", { email, password });
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-base-200 p-4">
            <form onSubmit={handleSubmit} className="card w-full max-w-sm bg-base-100 p-6 shadow space-y-4">
                <h2 className="text-2xl font-bold text-center">Register</h2>

                <input
                    type="email"
                    placeholder="Email"
                    className="input input-bordered w-full"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />

                <input
                    type="password"
                    placeholder="Password"
                    className="input input-bordered w-full"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />

                <input
                    type="password"
                    placeholder="Confirm Password"
                    className="input input-bordered w-full"
                    value={confirm}
                    onChange={(e) => setConfirm(e.target.value)}
                    required
                />

                <button type="submit" className="btn btn-primary w-full">
                    Register
                </button>

                <p className="text-center text-sm">
                    Already have an account?{" "}
                    <Link to="/log_in" className="link link-primary">
                        Log in
                    </Link>
                </p>
            </form>
        </div>
    );
}
