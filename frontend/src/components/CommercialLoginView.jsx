import React, { useState } from 'react';

export default function CommercialLoginView({ onLoginAs }) {
  const [email, setEmail] = useState('santiago.perez@estudiante.edu.co');
  const [password, setPassword] = useState('••••••••••••');

  const demoAccounts = [
    { role: 'ESTUDIANTE', name: 'Santiago Pérez', title: 'Portal Estudiante', desc: 'Calificaciones, asistencia y lab digital' },
    { role: 'DOCENTE', name: 'Prof. Carlos Ruiz', title: 'Portal Docente', desc: 'Control de asistencia y calificaciones' },
    { role: 'ADMIN', name: 'Rectoría / Gerencia', title: 'Consola Ejecutiva SaaS', desc: 'Retención, analítica global y registro' },
    { role: 'FAMILIAR', name: 'Marta Morales', title: 'Portal Familiar', desc: 'Seguimiento pedagógico de acudidos' },
    { role: 'BIENESTAR', name: 'Mariana Restrepo', title: 'Módulo de Bienestar', desc: 'Acompañamiento psicológico y alertas' }
  ];

  const handleRegularLogin = (e) => {
    e.preventDefault();
    onLoginAs('ESTUDIANTE');
  };

  return (
    <div className="auth-landing-root">
      <div className="auth-landing-card">
        <div style={{ textAlign: 'center', marginBottom: 24 }}>
          <div style={{ display: 'inline-flex', alignItems: 'center', gap: 8, marginBottom: 8 }}>
            <span style={{ fontSize: 18, fontWeight: 700, letterSpacing: '-0.02em', color: 'var(--ink-strong)' }}>
              EDU.CORE // PLATFORM
            </span>
            <span className="brand-tag">v2.4 SaaS</span>
          </div>
          <p style={{ fontSize: 12.5, color: 'var(--mute)' }}>
            Ecosistema de gestión curricular, retención y analítica para instituciones de educación superior y técnica.
          </p>
        </div>

        {/* Regular Login Form */}
        <form onSubmit={handleRegularLogin}>
          <div className="cli-form-group">
            <label className="cli-label">Correo Electrónico Institucional:</label>
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
            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
              <label className="cli-label">Contraseña:</label>
              <a href="#forgot" style={{ fontSize: 11, color: 'var(--mute)' }}>¿Olvidaste tu clave?</a>
            </div>
            <input 
              type="password" 
              className="cli-input"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 18, fontSize: 12, color: 'var(--body)' }}>
            <input type="checkbox" id="remember" defaultChecked style={{ cursor: 'pointer' }} />
            <label htmlFor="remember" style={{ cursor: 'pointer' }}>Mantener sesión iniciada en este equipo</label>
          </div>

          <button type="submit" className="btn-cli btn-cli-primary" style={{ width: '100%', height: 40 }}>
            Iniciar Sesión Institucional
          </button>
        </form>

        <div style={{ display: 'flex', alignItems: 'center', gap: 12, margin: '20px 0', fontSize: 11.5, color: 'var(--mute)' }}>
          <div style={{ flex: 1, height: 1, background: 'var(--hairline)' }} />
          <span>Acceso Institucional SSO</span>
          <div style={{ flex: 1, height: 1, background: 'var(--hairline)' }} />
        </div>

        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 10, marginBottom: 24 }}>
          <button 
            type="button" 
            onClick={() => onLoginAs('ADMIN')} 
            className="btn-cli btn-cli-secondary btn-cli-sm"
          >
            Google Workspace
          </button>
          <button 
            type="button" 
            onClick={() => onLoginAs('ADMIN')} 
            className="btn-cli btn-cli-secondary btn-cli-sm"
          >
            Microsoft 365 Edu
          </button>
        </div>

        {/* Demo Fast Access Bar (For Buyers & Evaluators) */}
        <div style={{ 
          borderTop: '1px solid var(--hairline)', 
          paddingTop: 16, 
          background: 'var(--canvas-soft)', 
          padding: 14, 
          borderRadius: 'var(--radius-sm)' 
        }}>
          <div style={{ fontSize: 11, fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.04em', color: 'var(--accent)', marginBottom: 8 }}>
            Acceso Rápido Demo para Evaluadores:
          </div>

          <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
            {demoAccounts.map(demo => (
              <button
                key={demo.role}
                type="button"
                onClick={() => onLoginAs(demo.role)}
                style={{
                  display: 'flex',
                  justifyContent: 'space-between',
                  alignItems: 'center',
                  padding: '7px 10px',
                  background: 'var(--surface-card)',
                  border: '1px solid var(--hairline)',
                  borderRadius: 'var(--radius-sm)',
                  cursor: 'pointer',
                  textAlign: 'left'
                }}
              >
                <div>
                  <div style={{ fontSize: 12, fontWeight: 600, color: 'var(--ink)' }}>{demo.title}</div>
                  <div style={{ fontSize: 10.5, color: 'var(--mute)' }}>{demo.desc}</div>
                </div>
                <span style={{ fontSize: 11, color: 'var(--accent)', fontWeight: 600 }}>Entrar →</span>
              </button>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
