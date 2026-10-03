import React, { useState, useEffect } from 'react';
import { getPerfilEstudiante, updatePerfilEstudiante } from '../services/api';

export default function HojaVidaView() {
  const [perfil, setPerfil] = useState(null);
  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState(false);
  const [formData, setFormData] = useState({});
  const [previewPhoto, setPreviewPhoto] = useState('');
  const [saveAlert, setSaveAlert] = useState(false);

  useEffect(() => {
    loadPerfil();
  }, []);

  const loadPerfil = async () => {
    setLoading(true);
    const data = await getPerfilEstudiante(1);
    setPerfil(data);
    setFormData({
      resumen: data.resumen,
      intereses: data.intereses,
      experiencia: data.experiencia,
      fotoUrl: data.fotoUrl
    });
    setPreviewPhoto(data.fotoUrl);
    setLoading(false);
  };

  const handlePhotoUpload = (e) => {
    const file = e.target.files[0];
    if (file) {
      const url = URL.createObjectURL(file);
      setPreviewPhoto(url);
      setFormData(prev => ({ ...prev, fotoUrl: url }));
    }
  };

  const handleSave = async (e) => {
    e.preventDefault();
    await updatePerfilEstudiante(perfil.id, formData);
    setPerfil(prev => ({ ...prev, ...formData }));
    setEditing(false);
    setSaveAlert(true);
    setTimeout(() => setSaveAlert(false), 3000);
  };

  if (loading) {
    return (
      <div className="flat-panel">
        <p className="font-mono text-mute">Cargando perfil curricular y competencias...</p>
      </div>
    );
  }

  return (
    <div className="page-container">
      {saveAlert && (
        <div className="cli-diagnostic success">
          <div className="cli-diagnostic-title">Hoja de vida actualizada</div>
          <div>Los cambios en tu perfil profesional han sido persistidos en el sistema institucional.</div>
        </div>
      )}

      {/* Main Profile Panel */}
      <div className="flat-panel">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: 16 }}>
          <div style={{ display: 'flex', gap: 18, alignItems: 'flex-start' }}>
            <div style={{ 
              width: 88, 
              height: 88, 
              border: '1px solid var(--hairline-strong)', 
              borderRadius: 'var(--radius-md)',
              overflow: 'hidden',
              background: 'var(--canvas-soft)',
              flexShrink: 0
            }}>
              <img 
                src={previewPhoto || perfil.fotoUrl} 
                alt={perfil.nombreEstudiante} 
                style={{ width: '100%', height: '100%', objectFit: 'cover' }} 
              />
            </div>

            <div>
              <div style={{ display: 'flex', alignItems: 'center', gap: 10, flexWrap: 'wrap' }}>
                <h2 style={{ fontSize: 18, fontWeight: 700 }}>{perfil.nombreEstudiante}</h2>
                <span className="badge-status badge-neutral">ID: EST-2026-001</span>
                <span className="badge-status badge-success">● Activo</span>
              </div>

              <div style={{ fontSize: 12.5, color: 'var(--mute)', marginTop: 4 }}>
                {perfil.programa} • Semestre {perfil.semestre} • {perfil.correoEstudiante}
              </div>

              <div style={{ marginTop: 10, fontSize: 13, color: 'var(--body)', maxWidth: 650, lineHeight: 1.6 }}>
                {perfil.resumen}
              </div>
            </div>
          </div>

          <div>
            <button onClick={() => setEditing(true)} className="btn-cli btn-cli-secondary btn-cli-sm">
              Editar Perfil
            </button>
          </div>
        </div>
      </div>

      {/* Grid: Skills and Experience */}
      <div className="grid-2">
        {/* Habilidades */}
        <div className="flat-panel">
          <div className="flat-panel-header">
            <div style={{ fontSize: 13.5, fontWeight: 700 }}>Habilidades & Competencias</div>
            <span className="badge-status badge-neutral">{perfil.habilidades?.length || 0} registradas</span>
          </div>

          <div style={{ display: 'flex', flexWrap: 'wrap', gap: 8 }}>
            {perfil.habilidades?.map(h => (
              <span 
                key={h.id} 
                style={{ 
                  fontSize: 12,
                  border: '1px solid var(--hairline)',
                  background: 'var(--canvas-soft)',
                  padding: '4px 10px',
                  borderRadius: 'var(--radius-sm)',
                  color: 'var(--ink)'
                }}
              >
                <span style={{ color: h.tipo === 'Tecnica' ? 'var(--accent)' : 'var(--success)', fontWeight: 600 }}>
                  {h.tipo === 'Tecnica' ? 'Tech' : 'Soft'}:
                </span>{' '}
                {h.nombre} <span style={{ color: 'var(--mute)' }}>({h.nivel})</span>
              </span>
            ))}
          </div>

          <div style={{ marginTop: 20 }}>
            <div style={{ fontSize: 12, fontWeight: 600, color: 'var(--mute)', marginBottom: 4 }}>
              Áreas de Interés:
            </div>
            <p style={{ fontSize: 12.5, color: 'var(--body)' }}>{perfil.intereses}</p>
          </div>
        </div>

        {/* Experiencia y Trayectoria */}
        <div className="flat-panel">
          <div className="flat-panel-header">
            <div style={{ fontSize: 13.5, fontWeight: 700 }}>Trayectoria & Proyectos</div>
            <span className="badge-status badge-neutral">Expediente</span>
          </div>

          <div className="code-block" style={{ margin: 0, whiteSpace: 'pre-wrap', lineHeight: 1.6 }}>
            {perfil.experiencia || 'Sin experiencia previa registrada.'}
          </div>

          <div style={{ marginTop: 18, paddingTop: 12, borderTop: '1px solid var(--hairline)', fontSize: 11.5, color: 'var(--mute)' }}>
            <div>Expediente académico validado por Registro y Control Académico.</div>
          </div>
        </div>
      </div>

      {/* Edit Modal */}
      {editing && (
        <div className="modal-overlay">
          <div className="modal-dialog">
            <div className="modal-header">
              <span className="modal-title">Actualizar Hoja de Vida Estudiantil</span>
              <button 
                onClick={() => setEditing(false)} 
                style={{ background: 'none', border: 'none', cursor: 'pointer', fontSize: 16, color: 'var(--mute)' }}
              >
                ✕
              </button>
            </div>

            <form onSubmit={handleSave}>
              <div className="cli-form-group">
                <label className="cli-label">Fotografía de Perfil:</label>
                <input 
                  type="file" 
                  accept="image/*" 
                  onChange={handlePhotoUpload} 
                  className="cli-input"
                />
              </div>

              <div className="cli-form-group">
                <label className="cli-label">Resumen Profesional / Perfil:</label>
                <textarea 
                  rows={4}
                  value={formData.resumen}
                  onChange={(e) => setFormData({ ...formData, resumen: e.target.value })}
                  className="cli-textarea"
                />
              </div>

              <div className="cli-form-group">
                <label className="cli-label">Intereses y Metas:</label>
                <input 
                  type="text"
                  value={formData.intereses}
                  onChange={(e) => setFormData({ ...formData, intereses: e.target.value })}
                  className="cli-input"
                />
              </div>

              <div className="cli-form-group">
                <label className="cli-label">Experiencia y Proyectos Destacados:</label>
                <textarea 
                  rows={4}
                  value={formData.experiencia}
                  onChange={(e) => setFormData({ ...formData, experiencia: e.target.value })}
                  className="cli-textarea"
                />
              </div>

              <div className="modal-footer">
                <button type="button" onClick={() => setEditing(false)} className="btn-cli btn-cli-secondary">
                  Cancelar
                </button>
                <button type="submit" className="btn-cli btn-cli-primary">
                  Guardar Cambios
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}