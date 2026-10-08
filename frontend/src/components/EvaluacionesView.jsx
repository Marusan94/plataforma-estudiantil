import React, { useState, useEffect } from 'react';
import { getEvaluaciones, createEvaluacion } from '../services/api';

export default function EvaluacionesView({ currentRole }) {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showAddModal, setShowAddModal] = useState(false);
  const [form, setForm] = useState({ titulo: '', curso: '', tipo: 'Parcial', fecha: '', ponderacion: 20 });

  const canEdit = currentRole === 'DOCENTE' || currentRole === 'ADMIN';

  useEffect(() => {
    loadData();
  }, [currentRole]);

  const loadData = async () => {
    setLoading(true);
    const data = await getEvaluaciones();
    setItems(Array.isArray(data) ? data : []);
    setLoading(false);
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    await createEvaluacion(form);
    setShowAddModal(false);
    setForm({ titulo: '', curso: '', tipo: 'Parcial', fecha: '', ponderacion: 20 });
    loadData();
  };

  if (loading) {
    return (
      <div className="flat-panel">
        <p className="font-mono text-mute">Cargando evaluaciones...</p>
      </div>
    );
  }

  return (
    <div className="page-container">
      <div className="flat-panel" style={{ padding: '12px 18px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 12 }}>
          <div>
            <div style={{ fontSize: 15, fontWeight: 700, color: 'var(--ink)' }}>Evaluaciones</div>
            <div style={{ fontSize: 12, color: 'var(--mute)' }}>
              {items.length === 0 ? 'Sin evaluaciones registradas por el momento.' : `${items.length} evaluación(es) programada(s).`}
            </div>
          </div>
          {canEdit && (
            <button onClick={() => setShowAddModal(true)} className="btn-cli btn-cli-primary btn-cli-sm">
              Crear Evaluación
            </button>
          )}
        </div>
      </div>

      {items.length === 0 ? (
        <div className="flat-panel">
          <p className="font-mono text-mute">No hay evaluaciones para mostrar. {canEdit ? 'Cree la primera con el botón "Crear Evaluación".' : 'Vuelva más tarde.'}</p>
        </div>
      ) : (
        <div className="terminal-table-wrap">
          <table className="terminal-table">
            <thead>
              <tr>
                <th>Título</th>
                <th>Curso</th>
                <th>Tipo</th>
                <th>Fecha</th>
                <th>Ponderación</th>
              </tr>
            </thead>
            <tbody>
              {items.map(ev => (
                <tr key={ev.id}>
                  <td style={{ fontWeight: 600, color: 'var(--ink)' }}>{ev.titulo}</td>
                  <td>{ev.curso || '—'}</td>
                  <td>{ev.tipo || '—'}</td>
                  <td style={{ color: 'var(--mute)', fontSize: 12 }}>{ev.fecha || '—'}</td>
                  <td>{ev.ponderacion != null ? `${ev.ponderacion}%` : '—'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {showAddModal && (
        <div className="modal-overlay">
          <div className="modal-dialog">
            <div className="modal-header">
              <span className="modal-title">Crear Nueva Evaluación</span>
              <button onClick={() => setShowAddModal(false)} style={{ background: 'none', border: 'none', cursor: 'pointer', fontSize: 16, color: 'var(--mute)' }}>✕</button>
            </div>
            <form onSubmit={handleCreate}>
              <div className="cli-form-group">
                <label className="cli-label">Título:</label>
                <input type="text" value={form.titulo} onChange={(e) => setForm({ ...form, titulo: e.target.value })} className="cli-input" required />
              </div>
              <div className="grid-2">
                <div className="cli-form-group">
                  <label className="cli-label">Curso:</label>
                  <input type="text" value={form.curso} onChange={(e) => setForm({ ...form, curso: e.target.value })} className="cli-input" />
                </div>
                <div className="cli-form-group">
                  <label className="cli-label">Tipo:</label>
                  <select value={form.tipo} onChange={(e) => setForm({ ...form, tipo: e.target.value })} className="cli-select">
                    <option value="Parcial">Parcial</option>
                    <option value="Quiz">Quiz</option>
                    <option value="Taller">Taller</option>
                    <option value="Proyecto">Proyecto</option>
                    <option value="Final">Final</option>
                  </select>
                </div>
              </div>
              <div className="grid-2">
                <div className="cli-form-group">
                  <label className="cli-label">Fecha:</label>
                  <input type="date" value={form.fecha} onChange={(e) => setForm({ ...form, fecha: e.target.value })} className="cli-input" />
                </div>
                <div className="cli-form-group">
                  <label className="cli-label">Ponderación (%):</label>
                  <input type="number" min="1" max="100" value={form.ponderacion} onChange={(e) => setForm({ ...form, ponderacion: parseInt(e.target.value) || 0 })} className="cli-input" />
                </div>
              </div>
              <div className="modal-footer">
                <button type="button" onClick={() => setShowAddModal(false)} className="btn-cli btn-cli-secondary">Cancelar</button>
                <button type="submit" className="btn-cli btn-cli-primary">Guardar Evaluación</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
