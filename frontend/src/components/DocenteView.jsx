import React, { useState, useEffect } from 'react';
import { getEstudiantes, getCursos, getProgresoEstudiante, saveProgreso } from '../services/api';

export default function DocenteView({ currentRole }) {
  const [estudiantes, setEstudiantes] = useState([]);
  const [cursos, setCursos] = useState([]);
  const [selectedId, setSelectedId] = useState('');
  const [progreso, setProgreso] = useState([]);
  const [loading, setLoading] = useState(true);
  const [loadingProg, setLoadingProg] = useState(false);
  const [form, setForm] = useState({ cursoId: '', avance: 0, observaciones: '' });

  const canEdit = currentRole === 'DOCENTE' || currentRole === 'ADMIN';

  useEffect(() => {
    loadBase();
  }, [currentRole]);

  const loadBase = async () => {
    setLoading(true);
    const [est, cur] = await Promise.all([getEstudiantes(), getCursos()]);
    const estList = Array.isArray(est) ? est : [];
    const curList = Array.isArray(cur) ? cur : [];
    setEstudiantes(estList);
    setCursos(curList);
    if (estList.length > 0) {
      const first = estList[0].id;
      setSelectedId(first);
      await loadProgreso(first);
    }
    setLoading(false);
  };

  const loadProgreso = async (estudianteId) => {
    if (!estudianteId) return;
    setLoadingProg(true);
    const data = await getProgresoEstudiante(estudianteId);
    setProgreso(Array.isArray(data) ? data : []);
    setLoadingProg(false);
  };

  const handleSelect = async (e) => {
    const id = e.target.value;
    setSelectedId(id);
    await loadProgreso(id);
  };

  const handleSave = async (e) => {
    e.preventDefault();
    await saveProgreso(selectedId, form);
    setForm({ cursoId: '', avance: 0, observaciones: '' });
    await loadProgreso(selectedId);
  };

  if (loading) {
    return (
      <div className="flat-panel">
        <p className="font-mono text-mute">Cargando panel docente...</p>
      </div>
    );
  }

  return (
    <div className="page-container">
      <div className="flat-panel" style={{ padding: '12px 18px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 12 }}>
          <div>
            <div style={{ fontSize: 15, fontWeight: 700, color: 'var(--ink)' }}>Panel Docente</div>
            <div style={{ fontSize: 12, color: 'var(--mute)' }}>
              Seguimiento de progreso por estudiante{canEdit ? '' : ' (solo lectura)'}.
            </div>
          </div>
          <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
            <span style={{ fontSize: 12, fontWeight: 600, color: 'var(--mute)' }}>Estudiante:</span>
            <select value={selectedId} onChange={handleSelect} className="role-select">
              {estudiantes.length === 0 && <option value="">Sin estudiantes</option>}
              {estudiantes.map(s => (
                <option key={s.id} value={s.id}>{s.nombre}{s.codigoEstudiante ? ` (${s.codigoEstudiante})` : ''}</option>
              ))}
            </select>
          </div>
        </div>
      </div>

      {loadingProg ? (
        <div className="flat-panel">
          <p className="font-mono text-mute">Cargando progreso del estudiante...</p>
        </div>
      ) : progreso.length === 0 ? (
        <div className="flat-panel">
          <p className="font-mono text-mute">Sin registros de progreso para este estudiante.</p>
        </div>
      ) : (
        <div className="terminal-table-wrap">
          <table className="terminal-table">
            <thead>
              <tr>
                <th>Curso</th>
                <th>Avance</th>
                <th>Observaciones</th>
              </tr>
            </thead>
            <tbody>
              {progreso.map((p, i) => (
                <tr key={p.id || i}>
                  <td style={{ fontWeight: 600, color: 'var(--ink)' }}>{p.curso || p.cursoId || '—'}</td>
                  <td>{p.avance != null ? `${p.avance}%` : '—'}</td>
                  <td style={{ fontSize: 12, color: 'var(--body)' }}>{p.observaciones || '—'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {canEdit && selectedId && (
        <div className="flat-panel" style={{ padding: '14px 18px' }}>
          <div style={{ fontSize: 13, fontWeight: 700, color: 'var(--ink)', marginBottom: 10 }}>Registrar / Actualizar Progreso</div>
          <form onSubmit={handleSave}>
            <div className="grid-2">
              <div className="cli-form-group">
                <label className="cli-label">Curso:</label>
                <select value={form.cursoId} onChange={(e) => setForm({ ...form, cursoId: e.target.value })} className="cli-select" required>
                  <option value="">Seleccione un curso</option>
                  {cursos.map(c => (
                    <option key={c.id} value={c.id}>{c.titulo}{c.codigo ? ` (${c.codigo})` : ''}</option>
                  ))}
                </select>
              </div>
              <div className="cli-form-group">
                <label className="cli-label">Avance (%):</label>
                <input type="number" min="0" max="100" value={form.avance} onChange={(e) => setForm({ ...form, avance: parseInt(e.target.value) || 0 })} className="cli-input" required />
              </div>
            </div>
            <div className="cli-form-group">
              <label className="cli-label">Observaciones:</label>
              <textarea rows={3} value={form.observaciones} onChange={(e) => setForm({ ...form, observaciones: e.target.value })} className="cli-textarea" placeholder="Observaciones pedagógicas..." />
            </div>
            <div className="modal-footer">
              <button type="submit" className="btn-cli btn-cli-primary">Guardar Progreso</button>
            </div>
          </form>
        </div>
      )}
    </div>
  );
}
