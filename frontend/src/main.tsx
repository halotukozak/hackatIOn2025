import { BrowserRouter, Routes, Route } from "react-router-dom";
import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";

import HomePage from "./home/HomePage.tsx";

import DiscoverPage from "./MainApp/DiscoverPage.tsx";
import LoginPage from "./authentication/LoginPage.tsx";
import PreferencesPage from "./preferences/PreferencesPage.tsx";
import RegisterPage from "./authentication/RegisterPage.tsx";
import RoommateReferencesPage from "./RoommatePreferences/RoommatePreferencesPage.tsx";
import MatchesPage from "./MainApp/MatchesPage.tsx";
import ProfilePage from "./MainApp/ProfilePage.tsx";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/log_in" element={<LoginPage />} />
        <Route path="/sign_in" element={<RegisterPage />} />
        <Route path="/get_started" element={<PreferencesPage />} />
        <Route path="/preferences" element={<RoommateReferencesPage />} />
        <Route path="/discover" element={<DiscoverPage />} />
        <Route path="/matches" element={<MatchesPage />} />
        <Route path="/profile" element={<ProfilePage />} />
      </Routes>
    </BrowserRouter>
  </StrictMode>
);
