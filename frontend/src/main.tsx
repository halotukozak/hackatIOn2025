import { BrowserRouter, Routes, Route } from "react-router-dom";
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'

import HomePage from "./HomePage.tsx";
import LoginPage from "./LoginPage";
import PreferencesPage from "./preferences/PreferencesPage.tsx"

createRoot(document.getElementById('root')!).render(
  <StrictMode>
      <BrowserRouter>
          <Routes>
              <Route path="/" element={<HomePage />} />
              <Route path="/log_in" element={<LoginPage />} />
              <Route path="/get_started" element={<PreferencesPage />} />
          </Routes>
      </BrowserRouter>
  </StrictMode>,
)
