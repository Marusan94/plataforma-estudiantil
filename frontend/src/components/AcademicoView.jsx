import React, { useState, useEffect } from 'react';
import { getNotasEstudiante, registrarNota } from '../services/api';

export default function AcademicoView({ currentRole }) {
  const [notas, setNotas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filterMateria, setFilterMateria] = useState('ALL');
  const [sortField, setSortField] = useState('fecha');
  const [sortAsc, setSortAsc] = useState(false);
  const [showAddModal, setShowAddModal] = useState(false);
  const [newNota, setNewNota] = useState({
    estudianteId: 1,
    materiaId: 1,
    valor: 4.0,
    tipoEvaluacion: 'Parcial 2',
    comentario: ''
  });

  useEffect(() => {
    loadNotas();
  }, [currentRole]);

  const loadNotas = async () => {
    setLoading(true);
    const data = await getNotasEstudiante(currentRole === 'ESTUDIANTE' ? 1 : 'all');
    setNotas(data);
    setLoading(false);
  };

  const handleSort = (field) => {
    if (sortField === field) {
      setSortAsc(!sortAsc);
    } else {
      setSortField(field);
      setSortAsc(true);
    }
  };

  const filteredNotas = notas.filter(n => filterMateria === 'ALL' || n.nombreMateria === filterMateria);
  const sortedNotas = [...filteredNotas].sort((a, b) => {
    let valA = a[sortField];
    let valB = b[sortField];
    if (typeof valA === 'string') {
      return sortAsc ? valA.localeCompare(valB) : valB.localeCompare(valA);
    }
    return sortAsc ? valA - valB : valB - valA;
  });

  const exportCSV = () => {
    let csv = "Materia,Estudiante,Tipo_Evaluacion,Nota,Fecha,Estado,Recomendacion\n";
    sortedNotas.forEach(n => {
      csv += `"${n.nombreMateria}","${n.nombreEstudiante}","${n.tipoEvaluacion}",${n.valor},"${n.fecha}","${n.valor >= 3.0 ? 'APROBADO' : 'REPROBADO'}","${n.recomendacion || ''}"\n`;
    });
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `boletin_calificaciones_${new Date().toISOString().split('T')[0]}.csv`;
    a.click();
  };

  const handleCreateNota = async (e) => {
    e.preventDefault();
    await registrarNota(newNota);
    setShowAddModal(false);
    loadNotas();
  };

  const notasBajas = sortedNotas.filter(n => n.valor < 3.0);

  if (loading) {
    return (
      <div className="flat-panel">
        <p className="font-mono text-mute">Cargando registro de calificaciones académicas...</p>
      </div>
    );
  }

  return (
    <div className="page-container">
      {/* Pedagogical Warnings (HUA2) */}
      {notasBajas.length > 0 && (
        <div className="cli-diagnostic warning">
          <div className="cli-diagnostic-title">
            Alerta Pedagógica: {notasBajas.length} calificación(es) bajo el umbral mínimo (3.0)
          </div>
          <div style={{ marginTop: 4, fontSize: 12.5, color: 'var(--body)' }}>
            Se sugiere activar planes de acompañamiento y tutorías pedagógicas para los siguientes registros:
          </div>
          <div style={{ marginTop: 8, display: 'flex', flexDirection: 'column', gap: 4 }}>
            {notasBajas.map(nb => (
              <div key={nb.id} style={{ fontSize: 12 }}>
                • <span style={{ fontWeight: 600 }}>{nb.nombreEstudiante || 'Estudiante'}</span> en {nb.nombreMateria} (calificación: <span style={{ color: 'var(--danger)', fontWeight: 700 }}>{nb.valor.toFixed(1)}</span>): {nb.recomendacion}
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Control and Filter Bar */}
      <div className="flat-panel" style={{ padding: '12px 18px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 12 }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 10, flexWrap: 'wrap' }}>
            <span style={{ fontSize: 12, fontWeight: 600, color: 'var(--mute)' }}>Filtrar Asignatura:</span>
            <select 
              value={filterMateria} 
              onChange={(e) => setFilterMateria(e.target.value)}
              className="role-select"
            >
              <option value="ALL">Todas las asignaturas</option>
              <option value="Desarrollo Web Full Stack">Desarrollo Web Full Stack</option>
              <option value="Bases de Datos Relacionales">Bases de Datos Relacionales</option>
              <option value="Algoritmos y Programación">Algoritmos y Programación</option>
            </select>
          </div>

          <div style={{ display: 'flex', gap: 8, alignItems: 'center' }}>
            <button onClick={exportCSV} className="btn-cli btn-cli-secondary btn-cli-sm">
              Exportar CSV
            </button>

            {(currentRole === 'DOCENTE' || currentRole === 'ADMIN') && (
              <button onClick={() => setShowAddModal(true)} className="btn-cli btn-cli-primary btn-cli-sm">
                Registrar Nota
              </button>
            )}
          </div>
        </div>
      </div>

      {/* Grades Table (HUA1) */}
      <div className="terminal-table-wrap">
        <table className="terminal-table">
          <thead>
            <tr>
              <th onClick={() => handleSort('nombreMateria')} style={{ cursor: 'pointer' }}>
                Materia {sortField === 'nombreMateria' ? (sortAsc ? '↑' : '↓') : ''}
              </th>
              <th>Estudiante</th>
              <th>Evaluación</th>
              <th onClick={() => handleSort('valor')} style={{ cursor: 'pointer' }}>
                Nota {sortField === 'valor' ? (sortAsc ? '↑' : '↓') : ''}
              </th>
              <th onClick={() => handleSort('fecha')} style={{ cursor: 'pointer' }}>
                Fecha {sortField === 'fecha' ? (sortAsc ? '↑' : '↓') : ''}
              </th>
              <th>Estado</th>
              <th>Diagnóstico / Recomendación</th>
            </tr>
          </thead>
          <tbody>
            {sortedNotas.map(n => {
              const isPassing = n.valor >= 3.0;
              return (
                <tr key={n.id}>
                  <td style={{ fontWeight: 600, color: 'var(--ink)' }}>{n.nombreMateria}</td>
                  <td>{n.nombreEstudiante || 'Santiago Pérez'}</td>
                  <td>{n.tipoEvaluacion}</td>
                  <td style={{ fontWeight: 700, color: isPassing ? 'var(--ink)' : 'var(--danger)' }}>
                    {n.valor.toFixed(1)} / 5.0
                  </td>
                  <td style={{ color: 'var(--mute)', fontSize: 12 }}>{n.fecha}</td>
                  <td>
                    <span className={isPassing ? 'badge-status badge-success' : 'badge-status badge-danger'}>
                      {isPassing ? '● Aprobado' : '● Reprobado'}
                    </span>
                  </td>
                  <td style={{ fontSize: 12, maxWidth: 300, color: 'var(--body)' }}>
                    {n.recomendacion || 'Rendimiento conforme a los objetivos del curso.'}
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>

      {/* Modal Add Grade */}
      {showAddModal && (
        <div className="modal-overlay">
          <div className="modal-dialog">
            <div className="modal-header">
              <span className="modal-title">Registrar Nueva Calificación</span>
              <button 
                onClick={() => setShowAddModal(false)}
                style={{ background: 'none', border: 'none', cursor: 'pointer', fontSize: 16, color: 'var(--mute)' }}
              >
                ✕
              </button>
            </div>

            <form onSubmit={handleCreateNota}>
              <div className="cli-form-group">
                <label className="cli-label">Estudiante:</label>
                <select 
                  value={newNota.estudianteId} 
                  onChange={(e) => setNewNota({ ...newNota, estudianteId: parseInt(e.target.value) })}
                  className="cli-select"
                >
                  <option value={1}>Santiago Pérez (EST-2026-001)</option>
                  <option value={2}>Laura Gómez (EST-2026-002)</option>
                </select>
              </div>

              <div className="cli-form-group">
                <label className="cli-label">Asignatura:</label>
                <select 
                  value={newNota.materiaId} 
                  onChange={(e) => setNewNota({ ...newNota, materiaId: parseInt(e.target.value) })}
                  className="cli-select"
                >
                  <option value={1}>Desarrollo Web Full Stack (G-WEB-01)</option>
                  <option value={2}>Bases de Datos Relacionales (G-BD-01)</option>
                  <option value={3}>Algoritmos y Programación (G-ALGO-01)</option>
                </select>
              </div>

              <div className="grid-2">
                <div className="cli-form-group">
                  <label className="cli-label">Tipo de Evaluación:</label>
                  <input 
                    type="text" 
                    value={newNota.tipoEvaluacion} 
                    onChange={(e) => setNewNota({ ...newNota, tipoEvaluacion: e.target.value })}
                    className="cli-input"
                    required
                  />
                </div>

                <div className="cli-form-group">
                  <label className="cli-label">Nota Numérica (0.0 - 5.0):</label>
                  <input 
                    type="number" 
                    step="0.1" 
                    min="0" 
                    max="5.0"
                    value={newNota.valor} 
                    onChange={(e) => setNewNota({ ...newNota, valor: parseFloat(e.target.value) })}
                    className="cli-input"
                    required
                  />
                </div>
              </div>

              <div className="cli-form-group">
                <label className="cli-label">Observaciones y Retroalimentación:</label>
                <textarea 
                  rows={3}
                  value={newNota.comentario}
                  onChange={(e) => setNewNota({ ...newNota, comentario: e.target.value })}
                  placeholder="Retroalimentación cualitativa para el estudiante..."
                  className="cli-textarea"
                />
              </div>

              <div className="modal-footer">
                <button type="button" onClick={() => setShowAddModal(false)} className="btn-cli btn-cli-secondary">
                  Cancelar
                </button>
                <button type="submit" className="btn-cli btn-cli-primary">
                  Guardar Calificación
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}