import React, { useState, useEffect } from 'react';
import Navbar from './components/Navbar';
import DashboardView from './components/DashboardView';
import LabDigitalView from './components/LabDigitalView';
import HojaVidaView from './components/HojaVidaView';
import AcademicoView from './components/AcademicoView';
import CursosView from './components/CursosView';
import EvaluacionesView from './components/EvaluacionesView';
import BibliotecaView from './components/BibliotecaView';
import DocenteView from './components/DocenteView';
import AsistenciaView from './components/AsistenciaView';
import BienestarView from './components/BienestarView';
import FamiliarView from './components/FamiliarView';
import RegistroView from './components/RegistroView';
import CommercialLoginView from './components/CommercialLoginView';
import PublicPortfolio from './components/PublicPortfolio';
import NewsSlider from './components/NewsSlider';
import CopilotWidget from './components/CopilotWidget';

export default function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [sessionUser, setSessionUser] = useState(null);
  const [demoMode, setDemoMode] = useState(false);
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

  const handleLogin = (user) => {
    setSessionUser(user);
    setCurrentRole(user.rol || 'ESTUDIANTE');
    setDemoMode(false);
    setIsLoggedIn(true);
    setActiveTab('dashboard');
  };

  const handleDemo = () => {
    setSessionUser(null);
    setCurrentRole('ESTUDIANTE');
    setDemoMode(true);
    setIsLoggedIn(true);
    setActiveTab('dashboard');
  };

  const handleLogout = () => {
    setSessionUser(null);
    setDemoMode(false);
    setIsLoggedIn(false);
    setActiveTab('dashboard');
  };

  if (!isLoggedIn) {
    return <CommercialLoginView onLogin={handleLogin} onDemo={handleDemo} theme={theme} onToggleTheme={toggleTheme} />;
  }

  const renderActiveView = () => {
    switch (activeTab) {
      case 'dashboard':
        return <DashboardView currentRole={currentRole} sessionUser={sessionUser} />;
      case 'lab-digital':
        return <LabDigitalView currentRole={currentRole} />;
      case 'hoja-vida':
        return <HojaVidaView currentRole={currentRole} />;
      case 'academico':
        return <AcademicoView currentRole={currentRole} />;
      case 'cursos':
        return <CursosView currentRole={currentRole} />;
      case 'evaluaciones':
        return <EvaluacionesView currentRole={currentRole} />;
      case 'biblioteca':
        return <BibliotecaView currentRole={currentRole} />;
      case 'docente':
        return <DocenteView currentRole={currentRole} />;
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
        return <DashboardView currentRole={currentRole} sessionUser={sessionUser} />;
    }
  };

  // Slider y mascota guia solo en modulos donde no estorban
  const showWidgets = ['dashboard', 'bienestar', 'lab-digital'].includes(activeTab);

  return (
    <div className="app-root">
      <Navbar 
        currentRole={currentRole}
        onRoleChange={setCurrentRole}
        activeTab={activeTab}
        onTabChange={setActiveTab}
        theme={theme}
        onToggleTheme={toggleTheme}
        onLogout={handleLogout}
        sessionUser={sessionUser}
        demoMode={demoMode}
      />
      
      <main className="main-content-area">
        {showWidgets && <NewsSlider currentRole={currentRole} />}
        {renderActiveView()}
      </main>

      {showWidgets && <CopilotWidget currentRole={currentRole} activeTab={activeTab} />}

      <footer className="footer-meta">
        <div>
          <span>EDU.CORE PLATFORM // THEME: {theme.toUpperCase()} // LICENCIA ENTERPRISE</span>
        </div>
        <div className="footer-links">
          <span>API + WEB: 8080 (ACTIVO)</span>
          <span>OFFLINE-READY</span>
          <span>STATUS: 200 OK</span>
        </div>
      </footer>
    </div>
  );
}