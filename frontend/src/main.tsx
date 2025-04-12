import { BrowserRouter, Routes, Route } from "react-router-dom";
import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";

import HomePage from "./HomePage.tsx";

import DiscoverPage from "./DiscoverPage.tsx";
import LoginPage from "./authentication/LoginPage.tsx";
import PreferencesPage from "./preferences/PreferencesPage.tsx";
import RegisterPage from "./authentication/RegisterPage.tsx";
import DetailsPage from "./DetailsPage.tsx";
import MatchesPage from "./MatchesPage.tsx";
import ProfilePage from "./ProfilePage.tsx";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/log_in" element={<LoginPage />} />

        <Route path="/sign_in" element={<RegisterPage />} />
        <Route path="/get_started" element={<PreferencesPage />} />
        <Route path="/discover" element={<DiscoverPage />} />
        <Route path="/profile" element={<ProfilePage />} />
        <Route path="/matches" element={<MatchesPage />} />
      </Routes>
    </BrowserRouter>
  </StrictMode>
);
