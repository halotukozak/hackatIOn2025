import { Link } from "react-router-dom";

export default function HomePage() {
    return (
        <div className="min-h-screen flex flex-col items-center justify-center bg-base-200 text-center px-4">
            <h1 className="text-4xl font-bold mb-4">Find Your Perfect Roommate</h1>

            <p className="text-lg max-w-xl mb-6">
                Swipe, match, and connect with compatible roommates based on your lifestyle, preferences,
                and personality. Designed specifically for students.
            </p>

            <Link to="/log_in" className="btn btn-primary">
                Get Started
            </Link>
        </div>
    );
}
