import React, { useState } from 'react';
import { registrarNuevoUsuario } from '../services/api';

export default function RegistroView({ onUserCreated }) {
  const [formData, setFormData] = useState({
    nombre: '',
    correo: '',
    contrasena: '',
    rol: 'ESTUDIANTE',
    programa: 'Desarrollo de Software',
    semestre: 1,
    parentesco: 'Madre',
    telefono: ''
  });

  const [touched, setTouched] = useState({});
  const [showPassword, setShowPassword] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [successMsg, setSuccessMsg] = useState(false);
  const [countdown, setCountdown] = useState(3);

  // Validaciones en tiempo real (HUF1)
  const isNameValid = formData.nombre.trim().length >= 2;
  const isEmailValid = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.correo);
  const isPasswordValid = formData.contrasena.length >= 6;
  const isFormValid = isNameValid && isEmailValid && isPasswordValid;

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!isFormValid || submitting) return;

    setSubmitting(true);
    try {
      await registrarNuevoUsuario(formData);
      setSuccessMsg(true);
      
      // Redireccion automatica en 3 segundos (HUF2)
      let timer = 3;
      setCountdown(timer);
      const interval = setInterval(() => {
        timer -= 1;
        setCountdown(timer);
        if (timer <= 0) {
          clearInterval(interval);
          setSuccessMsg(false);
          setSubmitting(false);
          if (onUserCreated) onUserCreated();
        }
      }, 1000);
    } catch (err) {
      alert("Error al registrar: " + err.message);
      setSubmitting(false);
    }
  };

  return (
    <div className="page-container" style={{ maxWidth: 600, margin: '0 auto' }}>
      <div className="flat-panel">
        <div className="flat-panel-header">
          <div>
            <div style={{ fontSize: 14.5, fontWeight: 700 }}>Módulo de Ingreso y Registro (HUF1 - HUF2)</div>
            <div style={{ fontSize: 11.5, color: 'var(--mute)' }}>Validaciones en tiempo real y asignación de credenciales</div>
          </div>
          <span className="badge-status badge-neutral">Registro</span>
        </div>

        {/* Confirmation & Redirect (HUF2) */}
        {successMsg && (
          <div className="cli-diagnostic success">
            <div className="cli-diagnostic-title">Usuario registrado exitosamente</div>
            <div>Las credenciales han sido provisionadas. Redirigiendo al panel principal en {countdown} segundos...</div>
          </div>
        )}

        <form onSubmit={handleSubmit}>
          {/* Nombre */}
          <div className="cli-form-group">
            <label className="cli-label">Nombre Completo:</label>
            <input 
              type="text" 
              placeholder="Ej. Juan Manuel Morales"
              value={formData.nombre}
              onChange={(e) => setFormData({ ...formData, nombre: e.target.value })}
              onBlur={() => setTouched({ ...touched, nombre: true })}
              className={`cli-input ${touched.nombre && !isNameValid ? 'cli-input-error' : ''}`}
              disabled={submitting}
            />
            {touched.nombre && !isNameValid && (
              <span className="cli-error-msg">El nombre debe contener al menos 2 caracteres.</span>
            )}
          </div>

          {/* Correo */}
          <div className="cli-form-group">
            <label className="cli-label">Correo Electrónico Institucional:</label>
            <input 
              type="email" 
              placeholder="usuario@dominio.edu.co"
              value={formData.correo}
              onChange={(e) => setFormData({ ...formData, correo: e.target.value })}
              onBlur={() => setTouched({ ...touched, correo: true })}
              className={`cli-input ${touched.correo && !isEmailValid ? 'cli-input-error' : ''}`}
              disabled={submitting}
            />
            {touched.correo && !isEmailValid && (
              <span className="cli-error-msg">Ingrese un correo electrónico válido.</span>
            )}
          </div>

          {/* Contraseña */}
          <div className="cli-form-group">
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <label className="cli-label">Contraseña de Acceso:</label>
              <button 
                type="button" 
                onClick={() => setShowPassword(!showPassword)}
                style={{ background: 'none', border: 'none', cursor: 'pointer', fontSize: 11.5, color: 'var(--mute)' }}
              >
                {showPassword ? 'Ocultar' : 'Mostrar'}
              </button>
            </div>
            <input 
              type={showPassword ? 'text' : 'password'} 
              placeholder="Mínimo 6 caracteres"
              value={formData.contrasena}
              onChange={(e) => setFormData({ ...formData, contrasena: e.target.value })}
              onBlur={() => setTouched({ ...touched, contrasena: true })}
              className={`cli-input ${touched.contrasena && !isPasswordValid ? 'cli-input-error' : ''}`}
              disabled={submitting}
            />
            {touched.contrasena && !isPasswordValid && (
              <span className="cli-error-msg">La contraseña debe tener al menos 6 caracteres.</span>
            )}
          </div>

          {/* Rol del Usuario */}
          <div className="cli-form-group">
            <label className="cli-label">Rol en la Plataforma:</label>
            <select 
              value={formData.rol}
              onChange={(e) => setFormData({ ...formData, rol: e.target.value })}
              className="cli-select"
              disabled={submitting}
            >
              <option value="ESTUDIANTE">Estudiante</option>
              <option value="DOCENTE">Docente</option>
              <option value="FAMILIAR">Familiar / Acudiente</option>
              <option value="BIENESTAR">Bienestar Institucional</option>
              <option value="ADMIN">Administrador</option>
            </select>
          </div>

          {/* Campos Dinámicos según Rol */}
          {formData.rol === 'ESTUDIANTE' && (
            <div className="grid-2">
              <div className="cli-form-group">
                <label className="cli-label">Programa Académico:</label>
                <input 
                  type="text"
                  value={formData.programa}
                  onChange={(e) => setFormData({ ...formData, programa: e.target.value })}
                  className="cli-input"
                  disabled={submitting}
                />
              </div>
              <div className="cli-form-group">
                <label className="cli-label">Semestre:</label>
                <select 
                  value={formData.semestre}
                  onChange={(e) => setFormData({ ...formData, semestre: parseInt(e.target.value) })}
                  className="cli-select"
                  disabled={submitting}
                >
                  {[1, 2, 3, 4, 5, 6].map(s => (
                    <option key={s} value={s}>Semestre {s}</option>
                  ))}
                </select>
              </div>
            </div>
          )}

          {formData.rol === 'FAMILIAR' && (
            <div className="grid-2">
              <div className="cli-form-group">
                <label className="cli-label">Parentesco:</label>
                <select 
                  value={formData.parentesco}
                  onChange={(e) => setFormData({ ...formData, parentesco: e.target.value })}
                  className="cli-select"
                  disabled={submitting}
                >
                  <option value="Madre">Madre</option>
                  <option value="Padre">Padre</option>
                  <option value="Tutor Legal">Tutor Legal</option>
                  <option value="Hermano/a">Hermano/a</option>
                </select>
              </div>
              <div className="cli-form-group">
                <label className="cli-label">Teléfono de Contacto:</label>
                <input 
                  type="tel"
                  placeholder="Ej. 3001234567"
                  value={formData.telefono}
                  onChange={(e) => setFormData({ ...formData, telefono: e.target.value })}
                  className="cli-input"
                  disabled={submitting}
                />
              </div>
            </div>
          )}

          <div style={{ marginTop: 20 }}>
            <button 
              type="submit" 
              disabled={!isFormValid || submitting}
              className={`btn-cli btn-cli-primary ${!isFormValid ? 'btn-cli-secondary' : ''}`}
              style={{ width: '100%', height: 40 }}
            >
              {submitting ? 'Provisionando cuenta...' : 'Crear Usuario'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}