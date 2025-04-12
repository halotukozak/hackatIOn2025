// src/components/Navbar.tsx
import { Link, useLocation } from "react-router-dom";

export default function Navbar() {
  const location = useLocation();
  const currentPath = location.pathname;

  return (
    <div className="navbar bg-base-100 fixed top-0 left-0 z-10 shadow-sm w-full">
      <div className="navbar-start">
        <Link to="/discover" className="text-2xl font-bold text-primary pl-2">
          Roomie
        </Link>
      </div>

      <div className="navbar-center lg:flex items-right ml-auto">
        <div className="grow flex justify-right">
          <ul className="menu menu-horizontal rounded-md">
            <li>
              <Link
                to="/discover"
                className={`btn ${
                  currentPath === "/discover" ? "btn-primary" : "btn-base-300"
                }`}
              >
                Discover
              </Link>
            </li>
            <li>
              <Link
                to="/matches"
                className={`btn ${
                  currentPath === "/matches" ? "btn-primary" : "btn-base-300"
                }`}
              >
                Matches
              </Link>
            </li>
            <li>
              <Link
                to="/profile"
                className={`btn ${
                  currentPath === "/profile" ? "btn-primary" : "btn-base-300"
                }`}
              >
                Profile
              </Link>
            </li>
          </ul>
        </div>
      </div>
    </div>
  );
}
