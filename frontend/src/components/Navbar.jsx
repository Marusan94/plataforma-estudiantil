import React, { useState, useRef, useEffect } from 'react';

export default function Navbar({ currentRole, onRoleChange, activeTab, onTabChange, theme, onToggleTheme, onLogout }) {
  const [menuOpen, setMenuOpen] = useState(false);
  const menuRef = useRef(null);

  const personas = [
    { id: 'ESTUDIANTE', name: 'Santiago Pérez', roleLabel: 'Estudiante', initials: 'SP', email: 'santiago.perez@estudiante.edu.co' },
    { id: 'DOCENTE', name: 'Prof. Carlos Ruiz', roleLabel: 'Docente Titular', initials: 'CR', email: 'carlos.ruiz@docente.edu.co' },
    { id: 'FAMILIAR', name: 'Marta Morales', roleLabel: 'Acudiente (Madre)', initials: 'MM', email: 'marta.morales@familiar.edu.co' },
    { id: 'BIENESTAR', name: 'Mariana Restrepo', roleLabel: 'Bienestar Institucional', initials: 'MR', email: 'mariana.restrepo@apoyo.edu.co' },
    { id: 'ADMIN', name: 'Rectoría / Control Central', roleLabel: 'Administrador SaaS', initials: 'AD', email: 'admin@rectoria.edu.co' }
  ];

  const currentPersona = personas.find(p => p.id === currentRole) || personas[0];

  const allTabs = [
    { id: 'dashboard', label: 'Dashboard' },
    { id: 'lab-digital', label: 'Lab Digital' },
    { id: 'hoja-vida', label: 'Hoja de Vida' },
    { id: 'academico', label: 'Académico' },
    { id: 'asistencia', label: 'Asistencia' },
    { id: 'bienestar', label: 'Bienestar' },
    { id: 'familiar', label: 'Familiar' },
    { id: 'registro', label: 'Registro' }
  ];

  const roleTabs = {
    ESTUDIANTE: ['dashboard', 'lab-digital', 'hoja-vida', 'academico', 'asistencia', 'bienestar'],
    DOCENTE: ['dashboard', 'lab-digital', 'academico', 'asistencia', 'bienestar', 'registro'],
    FAMILIAR: ['dashboard', 'familiar', 'bienestar'],
    BIENESTAR: ['dashboard', 'bienestar', 'asistencia', 'registro'],
    ADMIN: ['dashboard', 'lab-digital', 'hoja-vida', 'academico', 'asistencia', 'bienestar', 'familiar', 'registro']
  };

  const tabs = allTabs.filter(t => roleTabs[currentRole]?.includes(t.id));

  useEffect(() => {
    const handleClickOutside = (e) => {
      if (menuRef.current && !menuRef.current.contains(e.target)) {
        setMenuOpen(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  return (
    <header className="navbar-container">
      <div className="navbar-top">
        <div className="brand-badge">
          <span className="brand-logo" aria-hidden="true">
            <svg viewBox="0 0 16 16" shapeRendering="crispEdges">
              <rect x="3" y="1" width="10" height="2" fill="#f59e0b"/>
              <rect x="2" y="3" width="12" height="10" fill="#0b1220"/>
              <rect x="4" y="4" width="8" height="7" fill="#f5ead0"/>
              <rect x="5" y="6" width="2" height="3" fill="#f59e0b"/>
              <rect x="9" y="6" width="2" height="3" fill="#f59e0b"/>
              <rect x="5" y="7" width="1" height="1" fill="#202020"/>
              <rect x="10" y="7" width="1" height="1" fill="#202020"/>
              <rect x="7" y="9" width="2" height="1" fill="#b45309"/>
              <rect x="13" y="8" width="2" height="1" fill="#78350f"/>
              <rect x="13" y="9" width="2" height="3" fill="#fbbf24"/>
              <rect x="2" y="13" width="12" height="1" fill="#f59e0b"/>
            </svg>
          </span>
          <span className="brand-text">
            <span className="brand-title">EDU.CORE</span>
            <span className="brand-tag">Campus Central</span>
          </span>
        </div>

        <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
          <button 
            type="button" 
            onClick={onToggleTheme} 
            className="theme-toggle-btn"
            title="Alternar entre modo claro y oscuro"
          >
            <span>{theme === 'dark' ? '☾ Dark' : '☼ Light'}</span>
          </button>

          {/* User Profile & Demo Switcher Dropdown */}
          <div className="user-menu-wrapper" ref={menuRef}>
            <button 
              type="button"
              onClick={() => setMenuOpen(!menuOpen)}
              className="user-profile-trigger"
            >
              <div className="avatar-badge">{currentPersona.initials}</div>
              <span style={{ fontWeight: 600 }}>{currentPersona.name}</span>
              <span style={{ color: 'var(--mute)', fontSize: 11 }}>({currentPersona.roleLabel})</span>
              <span style={{ fontSize: 10, color: 'var(--mute)' }}>▾</span>
            </button>

            {menuOpen && (
              <div className="user-dropdown-menu">
                <div className="dropdown-user-header">
                  <div className="dropdown-user-name">{currentPersona.name}</div>
                  <div className="dropdown-user-email">{currentPersona.email}</div>
                  <div style={{ fontSize: 10.5, color: 'var(--accent)', marginTop: 2 }}>
                    Organización: Campus Central // Licencia Institucional
                  </div>
                </div>

                <div className="dropdown-section-title">
                  Modo Demostración / Cambiar Rol:
                </div>

                <div style={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                  {personas.map(p => (
                    <button
                      key={p.id}
                      type="button"
                      onClick={() => {
                        onRoleChange(p.id);
                        setMenuOpen(false);
                      }}
                      className={`dropdown-role-btn ${currentRole === p.id ? 'selected' : ''}`}
                    >
                      <span>{p.name}</span>
                      <span style={{ fontSize: 10.5, color: 'var(--mute)' }}>{p.roleLabel}</span>
                    </button>
                  ))}
                </div>

                <div style={{ borderTop: '1px solid var(--hairline)', marginTop: 8, paddingTop: 8 }}>
                  <button
                    type="button"
                    onClick={() => {
                      setMenuOpen(false);
                      if (onLogout) onLogout();
                    }}
                    className="dropdown-role-btn"
                    style={{ color: 'var(--danger)' }}
                  >
                    <span>Cerrar Sesión / Ver Portal Público</span>
                    <span>→</span>
                  </button>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>

      <nav className="nav-tabs-bar">
        <div className="nav-tabs-inner">
          {tabs.map(tab => {
            const isActive = activeTab === tab.id;
            return (
              <button
                key={tab.id}
                onClick={() => onTabChange(tab.id)}
                className={`nav-tab-btn ${isActive ? 'active' : ''}`}
              >
                {tab.label}
              </button>
            );
          })}
        </div>
      </nav>
    </header>
  );
}