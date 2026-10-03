import React, { useState, useEffect } from 'react';
import Navbar from './components/Navbar';
import DashboardView from './components/DashboardView';
import LabDigitalView from './components/LabDigitalView';
import HojaVidaView from './components/HojaVidaView';
import AcademicoView from './components/AcademicoView';
import AsistenciaView from './components/AsistenciaView';
import BienestarView from './components/BienestarView';
import FamiliarView from './components/FamiliarView';
import RegistroView from './components/RegistroView';
import CommercialLoginView from './components/CommercialLoginView';
import PublicPortfolio from './components/PublicPortfolio';
import NewsSlider from './components/NewsSlider';
import CopilotWidget from './components/CopilotWidget';

export default function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(true);
  const [currentRole, setCurrentRole] = useState('ESTUDIANTE');
  const [activeTab, setActiveTab] = useState('dashboard');
  const [theme, setTheme] = useState(() => {
    return localStorage.getItem('app_theme') || 'dark';
  });

  useEffect(() => {
    document.documentElement.setAttribute('data-theme', theme);
    localStorage.setItem('app_theme', theme);
  }, [theme]);

  const toggleTheme = () => {
    setTheme(prev => (prev === 'dark' ? 'light' : 'dark'));
  };

  const handleLoginAs = (role) => {
    setCurrentRole(role);
    setIsLoggedIn(true);
    setActiveTab('dashboard');
  };

  if (!isLoggedIn) {
    return <CommercialLoginView onLoginAs={handleLoginAs} />;
  }

  const renderActiveView = () => {
    switch (activeTab) {
      case 'dashboard':
        return <DashboardView currentRole={currentRole} />;
      case 'lab-digital':
        return <LabDigitalView currentRole={currentRole} />;
      case 'hoja-vida':
        return <HojaVidaView currentRole={currentRole} />;
      case 'academico':
        return <AcademicoView currentRole={currentRole} />;
      case 'asistencia':
        return <AsistenciaView currentRole={currentRole} />;
      case 'bienestar':
        return <BienestarView currentRole={currentRole} />;
      case 'familiar':
        return <FamiliarView currentRole={currentRole} />;
      case 'registro':
        return <RegistroView onUserCreated={() => setActiveTab('dashboard')} />;
      case 'portfolio':
        return <PublicPortfolio />;
      default:
        return <DashboardView currentRole={currentRole} />;
    }
  };

  return (
    <div className="app-root">
      <Navbar 
        currentRole={currentRole}
        onRoleChange={setCurrentRole}
        activeTab={activeTab}
        onTabChange={setActiveTab}
        theme={theme}
        onToggleTheme={toggleTheme}
        onLogout={() => setIsLoggedIn(false)}
      />
      
      <main className="main-content-area">
        <NewsSlider currentRole={currentRole} />
        {renderActiveView()}
      </main>

      <CopilotWidget currentRole={currentRole} activeTab={activeTab} />

      <footer className="footer-meta">
        <div>
          <span>EDU.CORE PLATFORM // THEME: {theme.toUpperCase()} // LICENCIA ENTERPRISE</span>
        </div>
        <div className="footer-links">
          <span>API: 8080 (ACTIVO)</span>
          <span>CLIENT: 5173 (ONLINE)</span>
          <span>STATUS: 200 OK</span>
        </div>
      </footer>
    </div>
  );
}