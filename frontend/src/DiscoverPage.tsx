import {Link} from "react-router-dom";

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
                        {/*<a className="text-2xl font-bold text-primary">Roomie</a>*/}
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
            <div className="flex justify-center px-2">
                <div className="w-full  bg-white shadow-lg rounded-lg flex flex-col items-center p-4">
                    {/*/!* Zdjęcie w kwadracie *!/*/}
                    {/*<img*/}
                    {/*    src="https://via.placeholder.com/150"*/}
                    {/*    alt="Person"*/}
                    {/*    className="w-32 h-32 rounded-full border-4 border-primary mb-4"*/}
                    {/*/>*/}
                    {/*/!* Imię i wiek *!/*/}
                    {/*<div className="text-center">*/}
                    {/*    <h2 className="text-xl font-bold">John Doe</h2>*/}
                    {/*    <p className="text-sm text-gray-600">Age: 25</p>*/}
                    {/*</div>*/}

                </div>

            </div>
        </div>
    );
}
