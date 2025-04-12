import {Link, useNavigate} from "react-router-dom";
import { register } from "../apis/authentication";

export default function RegisterPage() {
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const form = e.currentTarget;
        const email = (form.elements.namedItem("email") as HTMLInputElement).value;
        const password = (form.elements.namedItem("password") as HTMLInputElement).value;
        const password_confirmation = (form.elements.namedItem("password_confirmation") as HTMLInputElement).value;

        if (password !== password_confirmation) {
            alert("Passwords do not match!");
            return;
        }

        try {
            await register(email, password);
            navigate("/get_started");
        } catch (err) {
            console.error("Registration error:", err);
            alert("Registration failed");
        }


        console.log("Registering:", {email, password});
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-base-200 p-4">
            <form onSubmit={handleSubmit} className="card w-full max-w-sm bg-base-100 p-6 shadow space-y-4">
                <h2 className="text-2xl font-bold text-center">Register</h2>

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

                <input
                    name="password_confirmation"
                    type="password"
                    placeholder="Confirm Password"
                    className="input input-bordered w-full"
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
