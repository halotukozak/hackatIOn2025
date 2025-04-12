import {Link} from "react-router-dom";
import {UserIcon} from "@heroicons/react/24/solid";
// import React from "react";

export default function DiscoverPage() {
    // const handleLogin = (e: React.FormEvent<HTMLFormElement>) => {
    //     e.preventDefault();
    //     const form = e.currentTarget;
    //     const email = (form.elements.namedItem("email") as HTMLInputElement).value;
    //     const password = (form.elements.namedItem("password") as HTMLInputElement).value;
    //
    //     console.log("Logging in:", { email, password });
    // };

    return (
        <div className="min-h-screen bg-base-200">
                <div className="navbar bg-base-100 fixed top-0 left-0 z-10 shadow-sm w-full ">
                    <div className="navbar-start">
                        <a className="text-2xl font-bold text-primary pl-2">Roomie</a>
                    </div>

                    <div className="navbar-center lg:flex items-right ml-auto">
                        <div className="grow flex justify-right">
                            <ul className="menu menu-horizontal rounded-md">
                                <li><button disabled className="btn text-white disabled:!bg-[#00693C]">Discover</button></li>
                                <li><Link to="/matches" className="btn btn-base-300">
                                    Matches
                                </Link></li>
                                <li><Link to="/profile" className="btn btn-#B3B3B3 text-gray">
                                    Profile
                                </Link></li>
                            </ul>
                        </div>
                    </div>
                </div>
            <div className="pt-20 text-left ml-0 pl-4">
                <h1 className="text-2xl font-bold mb-4 ">Discover Potential Roommates</h1>
            </div>
            <div className="flex flex-col justify-center px-2 space-y-4">
                <div className="w-full bg-white shadow-lg rounded-lg flex flex-col p-4 relative">
                    <div className="flex justify-between">
                        <div className="flex space-x-4 flex-col">
                            <div className="flex space-x-4">
                                <UserIcon className="w-16 h-16 grow rounded-full border-2 border-neutral-content mb-4 justify-stretch" />
                            {/*<img  src="https://via.placeholder.com/150"*/}
                            {/*    className="w-15 h-15 rounded-full border-2 border-neutral-content mb-4 justify-left"/>*/}
                            <div className="flex w-full items-center mb-4">
                                <div>
                                    <h2 className="text-xl font-semibold">Emma, 21</h2>
                                    <p className="text-sm text-gray-600">WI • Year 3</p>
                                </div>
                            </div>
                            </div>

                        </div>
                        <div className="badge whitespace-nowrap text-nowrap bg-green-200 text-green-700 justify-end">87% Match</div>
                    </div>
                    <div className="flex space-x-4 justify-between">
                        <div className="flex space-x-2">
                            <h2 className="text-ml font-semibold">Sleep:</h2>
                            <p className="text-ml text-nowrap text-gray-600">21:00-6:00</p>
                        </div>
                        <div className="flex space-x-2">
                            <h2 className="text-ml font-semibold">Personality:</h2>
                            <p className="text-ml text-gray-600">Introverted</p>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    );
}
