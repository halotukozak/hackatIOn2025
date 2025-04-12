import { UserIcon } from "@heroicons/react/24/solid";
import Navbar from "./Navbar";

export default function DiscoverPage() {
  return (
    <div className="min-h-screen bg-base-200">
      <Navbar />
      <div className="pt-20 text-left ml-0 pl-4">
        <h1 className="text-2xl font-bold mb-4 ">
          Discover Potential Roommates
        </h1>
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
            <div className="badge whitespace-nowrap text-nowrap bg-green-200 text-green-700 justify-end">
              87% Match
            </div>
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
