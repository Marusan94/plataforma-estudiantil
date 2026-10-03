import React, { useState, useEffect } from 'react';
import { getAsistenciasGrupo, registrarAsistenciaLote } from '../services/api';

export default function AsistenciaView({ currentRole }) {
  const [selectedFecha, setSelectedFecha] = useState('2026-03-03');
  const [asistencias, setAsistencias] = useState([]);
  const [loading, setLoading] = useState(true);
  const [saveSuccess, setSaveSuccess] = useState(false);

  useEffect(() => {
    loadAsistencia();
  }, [selectedFecha]);

  const loadAsistencia = async () => {
    setLoading(true);
    const data = await getAsistenciasGrupo(1, selectedFecha);
    setAsistencias(data);
    setLoading(false);
  };

  const handleStateToggle = (estudianteId, nuevoEstado) => {
    setAsistencias(prev => prev.map(item => {
      if (item.estudianteId === estudianteId) {
        return {
          ...item,
          estado: nuevoEstado,
          justificada: nuevoEstado === 'JUSTIFICADO'
        };
      }
      return item;
    }));
  };

  const handleObservacionChange = (estudianteId, obs) => {
    setAsistencias(prev => prev.map(item => {
      if (item.estudianteId === estudianteId) {
        return { ...item, observaciones: obs };
      }
      return item;
    }));
  };

  const handleGuardarLote = async () => {
    const payload = {
      grupoId: 1,
      fecha: selectedFecha,
      asistencias: asistencias.map(a => ({
        estudianteId: a.estudianteId,
        estado: a.estado,
        observaciones: a.observaciones || '',
        justificada: a.estado === 'JUSTIFICADO'
      }))
    };
    await registrarAsistenciaLote(payload);
    setSaveSuccess(true);
    setTimeout(() => setSaveSuccess(false), 3000);
  };

  if (loading) {
    return (
      <div className="flat-panel">
        <p className="font-mono text-mute">Cargando registro y control de asistencia...</p>
      </div>
    );
  }

  return (
    <div className="page-container">
      {saveSuccess && (
        <div className="cli-diagnostic success">
          <div className="cli-diagnostic-title">Planilla guardada exitosamente</div>
          <div>El registro de asistencia para la fecha {selectedFecha} ha sido sincronizado en el servidor.</div>
        </div>
      )}

      {/* Student Global Attendance (HUAS2) */}
      <div className="flat-panel">
        <div className="flat-panel-header">
          <div>
            <div style={{ fontSize: 13.5, fontWeight: 700 }}>Asistencia Global del Estudiante (HUAS2)</div>
            <div style={{ fontSize: 11.5, color: 'var(--mute)' }}>Cálculo acumulativo: (Sesiones asistidas / Total sesiones programadas) * 100</div>
          </div>
          <span className="badge-status badge-success">● 100% Regular</span>
        </div>

        <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
          <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 12, marginBottom: 4 }}>
              <span>Porcentaje de cumplimiento global</span>
              <span style={{ fontWeight: 700, color: 'var(--success)' }}>100% (4 de 4 sesiones)</span>
            </div>
            <div className="meter-bar-track">
              <div className="meter-bar-fill" style={{ width: '100%', backgroundColor: 'var(--success)' }} />
            </div>
          </div>

          <div className="terminal-table-wrap" style={{ marginTop: 8 }}>
            <table className="terminal-table">
              <thead>
                <tr>
                  <th>Asignatura</th>
                  <th>Grupo</th>
                  <th>Sesiones Programadas</th>
                  <th>Asistencias Efectivas</th>
                  <th>Estado</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td style={{ fontWeight: 600 }}>Desarrollo Web Full Stack</td>
                  <td>G-WEB-01</td>
                  <td>4</td>
                  <td>4</td>
                  <td><span className="badge-status badge-success">● Óptimo</span></td>
                </tr>
                <tr>
                  <td style={{ fontWeight: 600 }}>Bases de Datos Relacionales</td>
                  <td>G-BD-01</td>
                  <td>4</td>
                  <td>4</td>
                  <td><span className="badge-status badge-success">● Óptimo</span></td>
                </tr>
                <tr>
                  <td style={{ fontWeight: 600 }}>Algoritmos y Programación</td>
                  <td>G-ALGO-01</td>
                  <td>4</td>
                  <td>4</td>
                  <td><span className="badge-status badge-success">● Óptimo</span></td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {/* Daily Attendance Roster (HUAS1) */}
      <div className="flat-panel">
        <div className="flat-panel-header">
          <div>
            <div style={{ fontSize: 13.5, fontWeight: 700 }}>Planilla Diaria de Asistencia del Grupo (HUAS1)</div>
            <div style={{ fontSize: 11.5, color: 'var(--mute)' }}>Grupo: G-WEB-01 • Asignatura: Desarrollo Web Full Stack</div>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
            <span style={{ fontSize: 12, color: 'var(--mute)' }}>Fecha de sesión:</span>
            <input 
              type="date"
              value={selectedFecha}
              onChange={(e) => setSelectedFecha(e.target.value)}
              className="role-select"
            />
          </div>
        </div>

        <div className="terminal-table-wrap">
          <table className="terminal-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Estudiante</th>
                <th>Estado de Asistencia</th>
                <th>Observaciones</th>
              </tr>
            </thead>
            <tbody>
              {asistencias.map(a => (
                <tr key={a.estudianteId}>
                  <td style={{ color: 'var(--mute)' }}>EST-{a.estudianteId}</td>
                  <td style={{ fontWeight: 600, color: 'var(--ink)' }}>{a.nombreEstudiante}</td>
                  <td>
                    <div style={{ display: 'flex', gap: 6 }}>
                      <button
                        type="button"
                        onClick={() => handleStateToggle(a.estudianteId, 'PRESENTE')}
                        className={`btn-cli btn-cli-sm ${a.estado === 'PRESENTE' ? 'btn-cli-primary' : 'btn-cli-secondary'}`}
                      >
                        Presente
                      </button>

                      <button
                        type="button"
                        onClick={() => handleStateToggle(a.estudianteId, 'AUSENTE')}
                        style={{
                          borderColor: a.estado === 'AUSENTE' ? 'var(--danger)' : 'var(--hairline)',
                          color: a.estado === 'AUSENTE' ? '#fff' : 'var(--danger)',
                          background: a.estado === 'AUSENTE' ? 'var(--danger)' : 'transparent'
                        }}
                        className="btn-cli btn-cli-sm"
                      >
                        Ausente
                      </button>

                      <button
                        type="button"
                        onClick={() => handleStateToggle(a.estudianteId, 'JUSTIFICADO')}
                        style={{
                          borderColor: a.estado === 'JUSTIFICADO' ? 'var(--warning)' : 'var(--hairline)',
                          color: a.estado === 'JUSTIFICADO' ? '#fff' : 'var(--warning)',
                          background: a.estado === 'JUSTIFICADO' ? 'var(--warning)' : 'transparent'
                        }}
                        className="btn-cli btn-cli-sm"
                      >
                        Justificado
                      </button>
                    </div>
                  </td>
                  <td>
                    <input 
                      type="text"
                      placeholder="Observaciones..."
                      value={a.observaciones || ''}
                      onChange={(e) => handleObservacionChange(a.estudianteId, e.target.value)}
                      className="cli-input"
                      style={{ padding: '5px 8px', fontSize: 12 }}
                    />
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        <div style={{ display: 'flex', justifyContent: 'flex-end', marginTop: 16 }}>
          <button onClick={handleGuardarLote} className="btn-cli btn-cli-primary">
            Guardar Planilla de Asistencia
          </button>
        </div>
      </div>
    </div>
  );
}