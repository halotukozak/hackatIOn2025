import { BrowserRouter, Routes, Route } from "react-router-dom";
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'

import HomePage from "./HomePage.tsx";


import DiscoverPage from "./DiscoverPage.tsx";
import LoginPage from "./authentication/LoginPage.tsx";
import PreferencesPage from "./preferences/PreferencesPage.tsx"
import RegisterPage from "./authentication/RegisterPage.tsx";

createRoot(document.getElementById('root')!).render(
  <StrictMode>
      <BrowserRouter>
          <Routes>
              <Route path="/" element={<HomePage />} />
              <Route path="/log_in" element={<LoginPage />} />

              <Route path="/sign_in" element={<RegisterPage />} />
              <Route path="/get_started" element={<PreferencesPage />} />
              <Route path="/discover" element={<DiscoverPage /> }/>
          </Routes>
      </BrowserRouter>
  </StrictMode>,
)
