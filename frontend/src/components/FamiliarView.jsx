import React, { useState, useEffect } from 'react';
import { getPerfilEstudiante, getNotasEstudiante } from '../services/api';

export default function FamiliarView({ currentRole }) {
  const [estudiante, setEstudiante] = useState(null);
  const [notas, setNotas] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    const p = await getPerfilEstudiante(1);
    const n = await getNotasEstudiante(1);
    setEstudiante(p);
    setNotas(n);
    setLoading(false);
  };

  if (loading) {
    return (
      <div className="flat-panel">
        <p className="font-mono text-mute">Cargando portal de seguimiento familiar...</p>
      </div>
    );
  }

  const notasCriticas = notas.filter(n => n.valor < 3.0);
  const promedio = notas.length > 0 ? (notas.reduce((acc, curr) => acc + curr.valor, 0) / notas.length).toFixed(1) : '0.0';

  return (
    <div className="page-container">
      {/* Visual Alerts for Guardians (HUFAM2) */}
      {notasCriticas.length > 0 ? (
        <div className="cli-diagnostic danger">
          <div className="cli-diagnostic-title">
            Alerta de Rendimiento Académico Crítico (HUFAM2)
          </div>
          <div style={{ marginTop: 4, fontSize: 12.5, color: 'var(--body)' }}>
            Tu acudido registra asignaturas con calificaciones por debajo del umbral mínimo de aprobación (3.0):
          </div>
          <div style={{ marginTop: 8, display: 'flex', flexDirection: 'column', gap: 6 }}>
            {notasCriticas.map(nc => (
              <div key={nc.id} style={{ fontSize: 12 }}>
                • <span style={{ fontWeight: 600 }}>{nc.nombreMateria} ({nc.tipoEvaluacion})</span>: Nota <span style={{ fontWeight: 700, color: 'var(--danger)' }}>{nc.valor.toFixed(1)}</span>. Orientación pedagógica: {nc.recomendacion}
              </div>
            ))}
          </div>
        </div>
      ) : (
        <div className="cli-diagnostic success">
          <div className="cli-diagnostic-title">Desempeño Académico Satisfactorio</div>
          <div>El estudiante no registra alertas de reprobación en el periodo académico vigente.</div>
        </div>
      )}

      {/* Linked Student Card (HUFAM1) */}
      <div className="flat-panel">
        <div className="flat-panel-header">
          <div>
            <div style={{ fontSize: 13.5, fontWeight: 700 }}>Estudiante Vinculado: {estudiante.nombreEstudiante} (HUFAM1)</div>
            <div style={{ fontSize: 11.5, color: 'var(--mute)' }}>Acudiente: Marta Morales • Parentesco: Madre • Acceso Autorizado</div>
          </div>
          <span className="badge-status badge-success">● Vínculo Activo</span>
        </div>

        <div className="metrics-grid" style={{ marginBottom: 20 }}>
          <div className="metric-tile">
            <div className="metric-tile-header">
              <span>Programa</span>
            </div>
            <div className="metric-tile-value" style={{ fontSize: 16 }}>{estudiante.programa}</div>
            <div className="metric-tile-sub">Semestre {estudiante.semestre}</div>
          </div>

          <div className="metric-tile">
            <div className="metric-tile-header">
              <span>Promedio General</span>
            </div>
            <div className="metric-tile-value" style={{ color: promedio >= 3.0 ? 'var(--ink)' : 'var(--danger)' }}>
              {promedio} / 5.0
            </div>
            <div className="metric-tile-sub">{notas.length} evaluaciones</div>
          </div>

          <div className="metric-tile">
            <div className="metric-tile-header">
              <span>Asistencia Global</span>
            </div>
            <div className="metric-tile-value">100%</div>
            <div className="metric-tile-sub">Sin ausencias registradas</div>
          </div>
        </div>

        <div style={{ fontSize: 13, fontWeight: 700, marginBottom: 10 }}>
          Boletín de Calificaciones del Periodo:
        </div>

        <div className="terminal-table-wrap">
          <table className="terminal-table">
            <thead>
              <tr>
                <th>Asignatura</th>
                <th>Evaluación</th>
                <th>Calificación</th>
                <th>Fecha</th>
                <th>Estado</th>
              </tr>
            </thead>
            <tbody>
              {notas.map(n => {
                const isPassing = n.valor >= 3.0;
                return (
                  <tr key={n.id}>
                    <td style={{ fontWeight: 600, color: 'var(--ink)' }}>{n.nombreMateria}</td>
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
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}