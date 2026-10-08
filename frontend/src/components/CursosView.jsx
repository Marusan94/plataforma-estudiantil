import React, { useState, useEffect } from 'react';
import { getCursos, createCurso, deleteCurso, getActividades, createActividad } from '../services/api';

export default function CursosView({ currentRole }) {
  const [cursos, setCursos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [expandedId, setExpandedId] = useState(null);
  const [actividades, setActividades] = useState({});
  const [showAddModal, setShowAddModal] = useState(false);
  const [showActModal, setShowActModal] = useState(false);
  const [activeCurso, setActiveCurso] = useState(null);
  const [newCurso, setNewCurso] = useState({
    titulo: '',
    codigo: '',
    categoria: '',
    nivel: 'Principiante',
    horas: 20
  });
  const [newAct, setNewAct] = useState({ titulo: '', descripcion: '', fecha: '' });

  const canEdit = currentRole === 'DOCENTE' || currentRole === 'ADMIN';

  useEffect(() => {
    loadCursos();
  }, [currentRole]);

  const loadCursos = async () => {
    setLoading(true);
    const data = await getCursos();
    setCursos(Array.isArray(data) ? data : []);
    setLoading(false);
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    await createCurso(newCurso);
    setShowAddModal(false);
    setNewCurso({ titulo: '', codigo: '', categoria: '', nivel: 'Principiante', horas: 20 });
    loadCursos();
  };

  const handleDelete = async (id) => {
    await deleteCurso(id);
    loadCursos();
  };

  const toggleExpand = async (curso) => {
    if (expandedId === curso.id) {
      setExpandedId(null);
      return;
    }
    setExpandedId(curso.id);
    const data = await getActividades(curso.id);
    setActividades(prev => ({ ...prev, [curso.id]: Array.isArray(data) ? data : [] }));
  };

  const openActModal = (curso) => {
    setActiveCurso(curso);
    setNewAct({ titulo: '', descripcion: '', fecha: '' });
    setShowActModal(true);
  };

  const handleCreateAct = async (e) => {
    e.preventDefault();
    await createActividad(activeCurso.id, newAct);
    setShowActModal(false);
    const data = await getActividades(activeCurso.id);
    setActividades(prev => ({ ...prev, [activeCurso.id]: Array.isArray(data) ? data : [] }));
  };

  if (loading) {
    return (
      <div className="flat-panel">
        <p className="font-mono text-mute">Cargando catálogo de cursos...</p>
      </div>
    );
  }

  return (
    <div className="page-container">
      <div className="flat-panel" style={{ padding: '12px 18px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 12 }}>
          <div>
            <div style={{ fontSize: 15, fontWeight: 700, color: 'var(--ink)' }}>Cursos</div>
            <div style={{ fontSize: 12, color: 'var(--mute)' }}>
              {cursos.length === 0 ? 'Sin cursos registrados por el momento.' : `${cursos.length} curso(s) disponible(s).`}
            </div>
          </div>
          {canEdit && (
            <button onClick={() => setShowAddModal(true)} className="btn-cli btn-cli-primary btn-cli-sm">
              Crear Curso
            </button>
          )}
        </div>
      </div>

      {cursos.length === 0 ? (
        <div className="flat-panel">
          <p className="font-mono text-mute">No hay cursos para mostrar. {canEdit ? 'Cree el primero con el botón "Crear Curso".' : 'Vuelva más tarde.'}</p>
        </div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
          {cursos.map(c => (
            <div key={c.id} className="flat-panel" style={{ padding: '14px 18px' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 10 }}>
                <div>
                  <div style={{ fontWeight: 700, color: 'var(--ink)' }}>{c.titulo}</div>
                  <div style={{ fontSize: 12, color: 'var(--mute)' }}>
                    {c.codigo}{c.categoria ? ` · ${c.categoria}` : ''}{c.nivel ? ` · ${c.nivel}` : ''}{c.horas ? ` · ${c.horas} h` : ''}
                  </div>
                </div>
                <div style={{ display: 'flex', gap: 8 }}>
                  <button onClick={() => toggleExpand(c)} className="btn-cli btn-cli-secondary btn-cli-sm">
                    {expandedId === c.id ? 'Ocultar actividades' : 'Ver actividades'}
                  </button>
                  {canEdit && (
                    <>
                      <button onClick={() => openActModal(c)} className="btn-cli btn-cli-secondary btn-cli-sm">
                        Añadir actividad
                      </button>
                      <button onClick={() => handleDelete(c.id)} className="btn-cli btn-cli-sm" style={{ color: 'var(--danger)' }}>
                        Eliminar
                      </button>
                    </>
                  )}
                </div>
              </div>
              {expandedId === c.id && (
                <div style={{ marginTop: 10, borderTop: '1px solid var(--hairline)', paddingTop: 10 }}>
                  {(actividades[c.id] || []).length === 0 ? (
                    <p className="font-mono text-mute" style={{ fontSize: 12 }}>Sin actividades registradas para este curso.</p>
                  ) : (
                    <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
                      {(actividades[c.id] || []).map(a => (
                        <div key={a.id} style={{ fontSize: 12.5 }}>
                          • <span style={{ fontWeight: 600 }}>{a.titulo}</span>
                          {a.fecha ? <span style={{ color: 'var(--mute)' }}> ({a.fecha})</span> : null}
                          {a.descripcion ? <div style={{ color: 'var(--body)' }}>{a.descripcion}</div> : null}
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              )}
            </div>
          ))}
        </div>
      )}

      {showAddModal && (
        <div className="modal-overlay">
          <div className="modal-dialog">
            <div className="modal-header">
              <span className="modal-title">Crear Nuevo Curso</span>
              <button onClick={() => setShowAddModal(false)} style={{ background: 'none', border: 'none', cursor: 'pointer', fontSize: 16, color: 'var(--mute)' }}>✕</button>
            </div>
            <form onSubmit={handleCreate}>
              <div className="cli-form-group">
                <label className="cli-label">Título:</label>
                <input type="text" value={newCurso.titulo} onChange={(e) => setNewCurso({ ...newCurso, titulo: e.target.value })} className="cli-input" required />
              </div>
              <div className="grid-2">
                <div className="cli-form-group">
                  <label className="cli-label">Código:</label>
                  <input type="text" value={newCurso.codigo} onChange={(e) => setNewCurso({ ...newCurso, codigo: e.target.value })} className="cli-input" required />
                </div>
                <div className="cli-form-group">
                  <label className="cli-label">Categoría:</label>
                  <input type="text" value={newCurso.categoria} onChange={(e) => setNewCurso({ ...newCurso, categoria: e.target.value })} className="cli-input" />
                </div>
              </div>
              <div className="grid-2">
                <div className="cli-form-group">
                  <label className="cli-label">Nivel:</label>
                  <select value={newCurso.nivel} onChange={(e) => setNewCurso({ ...newCurso, nivel: e.target.value })} className="cli-select">
                    <option value="Principiante">Principiante</option>
                    <option value="Intermedio">Intermedio</option>
                    <option value="Avanzado">Avanzado</option>
                  </select>
                </div>
                <div className="cli-form-group">
                  <label className="cli-label">Horas:</label>
                  <input type="number" min="1" value={newCurso.horas} onChange={(e) => setNewCurso({ ...newCurso, horas: parseInt(e.target.value) || 0 })} className="cli-input" required />
                </div>
              </div>
              <div className="modal-footer">
                <button type="button" onClick={() => setShowAddModal(false)} className="btn-cli btn-cli-secondary">Cancelar</button>
                <button type="submit" className="btn-cli btn-cli-primary">Guardar Curso</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {showActModal && activeCurso && (
        <div className="modal-overlay">
          <div className="modal-dialog">
            <div className="modal-header">
              <span className="modal-title">Nueva Actividad · {activeCurso.titulo}</span>
              <button onClick={() => setShowActModal(false)} style={{ background: 'none', border: 'none', cursor: 'pointer', fontSize: 16, color: 'var(--mute)' }}>✕</button>
            </div>
            <form onSubmit={handleCreateAct}>
              <div className="cli-form-group">
                <label className="cli-label">Título:</label>
                <input type="text" value={newAct.titulo} onChange={(e) => setNewAct({ ...newAct, titulo: e.target.value })} className="cli-input" required />
              </div>
              <div className="cli-form-group">
                <label className="cli-label">Descripción:</label>
                <textarea rows={3} value={newAct.descripcion} onChange={(e) => setNewAct({ ...newAct, descripcion: e.target.value })} className="cli-textarea" />
              </div>
              <div className="cli-form-group">
                <label className="cli-label">Fecha:</label>
                <input type="date" value={newAct.fecha} onChange={(e) => setNewAct({ ...newAct, fecha: e.target.value })} className="cli-input" />
              </div>
              <div className="modal-footer">
                <button type="button" onClick={() => setShowActModal(false)} className="btn-cli btn-cli-secondary">Cancelar</button>
                <button type="submit" className="btn-cli btn-cli-primary">Guardar Actividad</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
