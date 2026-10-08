import React, { useState, useEffect } from 'react';
import { getBiblioteca, createRecurso } from '../services/api';

export default function BibliotecaView({ currentRole }) {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showAddModal, setShowAddModal] = useState(false);
  const [form, setForm] = useState({ titulo: '', autor: '', tipo: 'Libro', categoria: '', url: '' });

  const canEdit = currentRole === 'DOCENTE' || currentRole === 'ADMIN';

  useEffect(() => {
    loadData();
  }, [currentRole]);

  const loadData = async () => {
    setLoading(true);
    const data = await getBiblioteca();
    setItems(Array.isArray(data) ? data : []);
    setLoading(false);
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    await createRecurso(form);
    setShowAddModal(false);
    setForm({ titulo: '', autor: '', tipo: 'Libro', categoria: '', url: '' });
    loadData();
  };

  if (loading) {
    return (
      <div className="flat-panel">
        <p className="font-mono text-mute">Cargando catálogo de biblioteca...</p>
      </div>
    );
  }

  return (
    <div className="page-container">
      <div className="flat-panel" style={{ padding: '12px 18px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 12 }}>
          <div>
            <div style={{ fontSize: 15, fontWeight: 700, color: 'var(--ink)' }}>Biblioteca</div>
            <div style={{ fontSize: 12, color: 'var(--mute)' }}>
              {items.length === 0 ? 'Sin recursos registrados por el momento.' : `${items.length} recurso(s) disponible(s).`}
            </div>
          </div>
          {canEdit && (
            <button onClick={() => setShowAddModal(true)} className="btn-cli btn-cli-primary btn-cli-sm">
              Añadir Recurso
            </button>
          )}
        </div>
      </div>

      <div className="cli-diagnostic">
        <div className="cli-diagnostic-title">Nota de medios locales</div>
        <div style={{ marginTop: 4, fontSize: 12.5, color: 'var(--body)' }}>
          Los archivos de la biblioteca se sirven desde la ruta local <span className="font-mono">/media</span> del mismo servidor. No se requiere conexión externa.
        </div>
      </div>

      {items.length === 0 ? (
        <div className="flat-panel">
          <p className="font-mono text-mute">No hay recursos para mostrar. {canEdit ? 'Añada el primero con el botón "Añadir Recurso".' : 'Vuelva más tarde.'}</p>
        </div>
      ) : (
        <div className="terminal-table-wrap">
          <table className="terminal-table">
            <thead>
              <tr>
                <th>Título</th>
                <th>Autor</th>
                <th>Tipo</th>
                <th>Categoría</th>
                <th>Enlace</th>
              </tr>
            </thead>
            <tbody>
              {items.map(r => (
                <tr key={r.id}>
                  <td style={{ fontWeight: 600, color: 'var(--ink)' }}>{r.titulo}</td>
                  <td>{r.autor || '—'}</td>
                  <td>{r.tipo || '—'}</td>
                  <td>{r.categoria || '—'}</td>
                  <td style={{ fontSize: 12 }}>
                    {r.url ? <span className="font-mono">{r.url}</span> : '—'}
                  </td>
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
              <span className="modal-title">Añadir Recurso a Biblioteca</span>
              <button onClick={() => setShowAddModal(false)} style={{ background: 'none', border: 'none', cursor: 'pointer', fontSize: 16, color: 'var(--mute)' }}>✕</button>
            </div>
            <form onSubmit={handleCreate}>
              <div className="cli-form-group">
                <label className="cli-label">Título:</label>
                <input type="text" value={form.titulo} onChange={(e) => setForm({ ...form, titulo: e.target.value })} className="cli-input" required />
              </div>
              <div className="grid-2">
                <div className="cli-form-group">
                  <label className="cli-label">Autor:</label>
                  <input type="text" value={form.autor} onChange={(e) => setForm({ ...form, autor: e.target.value })} className="cli-input" />
                </div>
                <div className="cli-form-group">
                  <label className="cli-label">Tipo:</label>
                  <select value={form.tipo} onChange={(e) => setForm({ ...form, tipo: e.target.value })} className="cli-select">
                    <option value="Libro">Libro</option>
                    <option value="Artículo">Artículo</option>
                    <option value="Video">Video</option>
                    <option value="Guía">Guía</option>
                    <option value="Otro">Otro</option>
                  </select>
                </div>
              </div>
              <div className="cli-form-group">
                <label className="cli-label">Categoría:</label>
                <input type="text" value={form.categoria} onChange={(e) => setForm({ ...form, categoria: e.target.value })} className="cli-input" />
              </div>
              <div className="cli-form-group">
                <label className="cli-label">Ruta local (/media/...):</label>
                <input type="text" value={form.url} onChange={(e) => setForm({ ...form, url: e.target.value })} className="cli-input" placeholder="/media/..." />
              </div>
              <div className="modal-footer">
                <button type="button" onClick={() => setShowAddModal(false)} className="btn-cli btn-cli-secondary">Cancelar</button>
                <button type="submit" className="btn-cli btn-cli-primary">Guardar Recurso</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
