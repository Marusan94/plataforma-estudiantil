import React, { useState } from 'react';
import { loginUser, registrarNuevoUsuario } from '../services/api';

const ICONS = {
  book: (
    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
      <path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z" />
      <path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z" />
    </svg>
  ),
  edit: (
    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
      <path d="M12 20h9" />
      <path d="M16.5 3.5a2.1 2.1 0 0 1 3 3L7 19l-4 1 1-4z" />
    </svg>
  ),
  archive: (
    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
      <polyline points="21 8 21 21 3 21 3 8" />
      <rect x="1" y="3" width="22" height="5" />
      <line x1="10" y1="12" x2="14" y2="12" />
    </svg>
  ),
  users: (
    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
      <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2" />
      <circle cx="9" cy="7" r="4" />
      <path d="M23 21v-2a4 4 0 0 0-3-3.87" />
      <path d="M16 3.13a4 4 0 0 1 0 7.75" />
    </svg>
  ),
};

const FEATURES = [
  { icon: 'book', title: 'Cursos y actividades' },
  { icon: 'edit', title: 'Evaluaciones y progreso' },
  { icon: 'archive', title: 'Biblioteca digital offline' },
  { icon: 'users', title: 'Panel docente' },
];

const SHOTS = [
  { img: '/shots/shot-dashboard.png', label: 'Dashboard del estudiante', url: 'educore / panel' },
  { img: '/shots/shot-lab-digital.png', label: 'Lab Digital interactivo', url: 'educore / lab-digital' },
  { img: '/shots/shot-academico.png', label: 'Registro academico', url: 'educore / academico' },
  { img: '/shots/shot-asistencia.png', label: 'Control de asistencia', url: 'educore / asistencia' },
];

export default function CommercialLoginView({ onLogin, onDemo, theme, onToggleTheme }) {
  const [mode, setMode] = useState('login'); // 'login' | 'register'
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [nombre, setNombre] = useState('');
  const [rol, setRol] = useState('ESTUDIANTE');
  const [error, setError] = useState('');
  const [busy, setBusy] = useState(false);

  const handleLogin = async (e) => {
    e.preventDefault();
    if (busy) return;
    setError('');
    setBusy(true);
    try {
      const user = await loginUser(email.trim(), password);
      onLogin(user);
    } catch (err) {
      setError(err.message || 'No se pudo iniciar sesion. Verifica tu correo y contrasena.');
    } finally {
      setBusy(false);
    }
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    if (busy) return;
    if (nombre.trim().length < 2) { setError('Escribe tu nombre completo.'); return; }
    if (password.length < 6) { setError('La contrasena debe tener minimo 6 caracteres.'); return; }
    setError('');
    setBusy(true);
    try {
      await registrarNuevoUsuario({ nombre: nombre.trim(), correo: email.trim(), contrasena: password, rol });
      const user = await loginUser(email.trim(), password);
      onLogin(user);
    } catch (err) {
      setError(err.message || 'No se pudo completar el registro.');
    } finally {
      setBusy(false);
    }
  };

  return (
    <div className="landing-root">
      <div className="stars" aria-hidden="true"><i className="s1" /><i className="s2" /></div>
      <header className="landing-topbar">
        <div className="landing-brand">
          <span className="landing-brand-mark">E</span>
          <span style={{ fontSize: 16, fontWeight: 800, letterSpacing: '-0.02em', color: 'var(--ink-strong)' }}>
            EDU.CORE // PLATFORM
          </span>
          <span className="brand-tag">Angeek Box</span>
        </div>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <button
            type="button"
            onClick={onToggleTheme}
            className="btn-cli btn-cli-secondary btn-cli-sm"
            title="Alternar entre modo claro y oscuro"
          >
            <span>{theme === 'dark' ? '☾ Oscuro' : '☼ Claro'}</span>
          </button>
          <button type="button" onClick={onDemo} className="btn-cli btn-cli-secondary btn-cli-sm">
            Modo prueba →
          </button>
        </div>
      </header>

      <main className="landing-main">
        <section>
          <div className="landing-kicker">Plataforma educativa offline-first</div>
          <h1 className="landing-title">Tu campus completo, sin internet.</h1>
          <p className="landing-sub">
            Cursos, actividades, evaluaciones, biblioteca digital y panel docente en un solo lugar.
            Funciona en la red local con PC, tablets y celulares, sin instalar nada.
          </p>
          <div className="landing-badges">
            <span className="landing-badge">Sin internet</span>
            <span className="landing-badge">Hasta 100 usuarios</span>
            <span className="landing-badge">PC, tablet y movil</span>
          </div>
          <div className="landing-features">
            {FEATURES.map(f => (
              <div key={f.title} className="landing-feature">
                <span className="landing-feature-ico">{ICONS[f.icon]}</span>
                <span>{f.title}</span>
              </div>
            ))}
          </div>
          <div className="landing-stats">
            <div className="landing-stat"><b>5</b><span>roles de acceso</span></div>
            <div className="landing-stat"><b>10+</b><span>modulos incluidos</span></div>
            <div className="landing-stat"><b>:8080</b><span>un solo puerto</span></div>
          </div>
        </section>

        <section className="landing-auth-wrap">
          <div className="landing-auth-card">
            <div style={{ textAlign: 'center', marginBottom: 14 }}>
              <div style={{ fontSize: 15, fontWeight: 800, color: 'var(--ink-strong)' }}>Accede a tu portal</div>
              <div style={{ fontSize: 12, color: 'var(--mute)' }}>Tu rol se activa automaticamente al entrar</div>
            </div>

        <div style={{ display: 'flex', gap: 8, marginBottom: 16 }}>
          <button
            type="button"
            onClick={() => { setMode('login'); setError(''); }}
            className={`btn-cli btn-cli-sm ${mode === 'login' ? 'btn-cli-primary' : 'btn-cli-secondary'}`}
            style={{ flex: 1 }}
          >
            Iniciar Sesion
          </button>
          <button
            type="button"
            onClick={() => { setMode('register'); setError(''); }}
            className={`btn-cli btn-cli-sm ${mode === 'register' ? 'btn-cli-primary' : 'btn-cli-secondary'}`}
            style={{ flex: 1 }}
          >
            Registrarse
          </button>
        </div>

        {error && (
          <div className="cli-diagnostic error" style={{ marginBottom: 12 }}>
            <div className="cli-diagnostic-title">No se pudo continuar</div>
            <div style={{ fontSize: 12 }}>{error}</div>
          </div>
        )}

        {mode === 'login' ? (
          <form onSubmit={handleLogin}>
            <div className="cli-form-group">
              <label className="cli-label">Correo Electronico:</label>
              <input
                type="email"
                className="cli-input"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="nombre@institucion.edu.co"
                required
              />
            </div>
            <div className="cli-form-group">
              <label className="cli-label">Contrasena:</label>
              <input
                type="password"
                className="cli-input"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Tu contrasena"
                required
              />
            </div>
            <button type="submit" className="btn-cli btn-cli-primary" style={{ width: '100%', height: 40 }} disabled={busy}>
              {busy ? 'Verificando...' : 'Entrar a mi portal'}
            </button>
          </form>
        ) : (
          <form onSubmit={handleRegister}>
            <div className="cli-form-group">
              <label className="cli-label">Nombre Completo:</label>
              <input
                type="text"
                className="cli-input"
                value={nombre}
                onChange={(e) => setNombre(e.target.value)}
                placeholder="Ej: Ana Torres"
                required
              />
            </div>
            <div className="cli-form-group">
              <label className="cli-label">Correo Electronico:</label>
              <input
                type="email"
                className="cli-input"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="nombre@institucion.edu.co"
                required
              />
            </div>
            <div className="cli-form-group">
              <label className="cli-label">Contrasena (minimo 6 caracteres):</label>
              <input
                type="password"
                className="cli-input"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Crea una contrasena"
                required
              />
            </div>
            <div className="cli-form-group">
              <label className="cli-label">Soy:</label>
              <select value={rol} onChange={(e) => setRol(e.target.value)} className="role-select" style={{ width: '100%' }}>
                <option value="ESTUDIANTE">Estudiante</option>
                <option value="DOCENTE">Docente</option>
                <option value="FAMILIAR">Familiar / Acudiente</option>
              </select>
            </div>
              <button type="submit" className="btn-cli btn-cli-primary" style={{ width: '100%', height: 44, fontSize: 14 }} disabled={busy}>
                {busy ? 'Creando cuenta...' : 'Crear mi cuenta gratis →'}
              </button>
              <p style={{ fontSize: 11, color: 'var(--mute)', textAlign: 'center', marginTop: 8 }}>
                Sin tarjeta · Sin instalacion · Tus datos quedan en tu institucion
              </p>
          </form>
        )}

        <div style={{ display: 'flex', alignItems: 'center', gap: 12, margin: '20px 0 12px', fontSize: 11.5, color: 'var(--mute)' }}>
          <div style={{ flex: 1, height: 1, background: 'var(--hairline)' }} />
          <span>Solo quiero mirar</span>
          <div style={{ flex: 1, height: 1, background: 'var(--hairline)' }} />
        </div>

        <button
          type="button"
          onClick={onDemo}
          className="btn-cli btn-cli-secondary"
          style={{ width: '100%', height: 40 }}
        >
          Entrar en modo prueba (demostracion)
        </button>
        <p style={{ fontSize: 11, color: 'var(--mute)', textAlign: 'center', marginTop: 8 }}>
          Explora todos los roles sin crear cuenta.
        </p>
          </div>
        </section>
      </main>

      <section className="landing-strip">
        <div className="landing-strip-title">Asi se ve por dentro · capturas reales del producto</div>
        <div className="landing-strip-inner">
          {SHOTS.map(s => (
            <div key={s.label}>
              <div className="shot-frame">
                <div className="shot-bar">
                  <span className="shot-dots"><i /><i /><i /></span>
                  <span className="shot-url">{s.url}</span>
                </div>
                <img src={s.img} alt={s.label} loading="lazy" />
              </div>
              <div className="mock-caption">{s.label}</div>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
}
