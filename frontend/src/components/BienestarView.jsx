import React, { useState, useEffect } from 'react';
import { getSolicitudesBienestar, crearSolicitudBienestar, cambiarEstadoBienestar } from '../services/api';

export default function BienestarView({ currentRole }) {
  const [solicitudes, setSolicitudes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filterEstado, setFilterEstado] = useState('ALL');
  const [successAlert, setSuccessAlert] = useState(false);
  const [newTicket, setNewTicket] = useState({
    estudianteId: 1,
    tipo: 'Psicologico',
    prioridad: 'MEDIA',
    descripcion: ''
  });

  useEffect(() => {
    loadSolicitudes();
  }, []);

  const loadSolicitudes = async () => {
    setLoading(true);
    const data = await getSolicitudesBienestar();
    setSolicitudes(data);
    setLoading(false);
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    if (!newTicket.descripcion.trim()) return;
    await crearSolicitudBienestar(newTicket);
    setSuccessAlert(true);
    setNewTicket({ estudianteId: 1, tipo: 'Psicologico', prioridad: 'MEDIA', descripcion: '' });
    loadSolicitudes();
    setTimeout(() => setSuccessAlert(false), 3500);
  };

  const handleUpdateEstado = async (id, nuevoEstado) => {
    await cambiarEstadoBienestar(id, nuevoEstado);
    setSolicitudes(prev => prev.map(s => s.id === id ? { ...s, estado: nuevoEstado } : s));
  };

  const filtered = solicitudes.filter(s => filterEstado === 'ALL' || s.estado === filterEstado);

  if (loading) {
    return (
      <div className="flat-panel">
        <p className="font-mono text-mute">Cargando bandeja de acompañamiento institucional...</p>
      </div>
    );
  }

  return (
    <div className="page-container">
      {successAlert && (
        <div className="cli-diagnostic success">
          <div className="cli-diagnostic-title">Solicitud de acompañamiento registrada</div>
          <div>Tu requerimiento ha sido recibido. El equipo de Bienestar asignará un profesional en un plazo menor a 24 horas.</div>
        </div>
      )}

      {/* Ticket Creation Form (HUAW1) */}
      <div className="flat-panel">
        <div className="flat-panel-header">
          <div>
            <div style={{ fontSize: 13.5, fontWeight: 700 }}>Solicitud de Acompañamiento y Bienestar (HUAW1)</div>
            <div style={{ fontSize: 11.5, color: 'var(--mute)' }}>Canal institucional confidencial para orientación psicológica, académica o apoyo socioeconómico.</div>
          </div>
          <span className="badge-status badge-neutral">Nuevo Caso</span>
        </div>

        <form onSubmit={handleCreate}>
          <div className="grid-2">
            <div className="cli-form-group">
              <label className="cli-label">Tipo de Acompañamiento:</label>
              <select 
                value={newTicket.tipo} 
                onChange={(e) => setNewTicket({ ...newTicket, tipo: e.target.value })}
                className="cli-select"
              >
                <option value="Psicologico">Apoyo Psicológico y Salud Mental</option>
                <option value="Academico">Acompañamiento y Tutoría Académica</option>
                <option value="Economico">Subsidio o Auxilio Económico</option>
                <option value="Orientacion Vocacional">Orientación Vocacional</option>
              </select>
            </div>

            <div className="cli-form-group">
              <label className="cli-label">Nivel de Prioridad:</label>
              <select 
                value={newTicket.prioridad} 
                onChange={(e) => setNewTicket({ ...newTicket, prioridad: e.target.value })}
                className="cli-select"
              >
                <option value="BAJA">Prioridad Baja</option>
                <option value="MEDIA">Prioridad Media</option>
                <option value="ALTA">Prioridad Alta (Urgente)</option>
              </select>
            </div>
          </div>

          <div className="cli-form-group">
            <label className="cli-label">Descripción de la necesidad o motivo:</label>
            <textarea 
              rows={3} 
              required
              placeholder="Detalla de forma confidencial el motivo de la consulta..."
              value={newTicket.descripcion} 
              onChange={(e) => setNewTicket({ ...newTicket, descripcion: e.target.value })} 
              className="cli-textarea"
            />
          </div>

          <div style={{ display: 'flex', justifyContent: 'flex-end', marginTop: 12 }}>
            <button type="submit" className="btn-cli btn-cli-primary">
              Registrar Solicitud de Apoyo
            </button>
          </div>
        </form>
      </div>

      {/* Tickets List and Case Management (HUAW2) */}
      <div className="flat-panel">
        <div className="flat-panel-header">
          <div>
            <div style={{ fontSize: 13.5, fontWeight: 700 }}>Seguimiento y Gestión de Casos (HUAW2)</div>
            <div style={{ fontSize: 11.5, color: 'var(--mute)' }}>Bandeja de requerimientos activos</div>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
            <span style={{ fontSize: 12, color: 'var(--mute)' }}>Estado:</span>
            <select 
              value={filterEstado} 
              onChange={(e) => setFilterEstado(e.target.value)}
              className="role-select"
            >
              <option value="ALL">Todos los estados</option>
              <option value="PENDIENTE">Pendientes</option>
              <option value="EN_ATENCION">En Atención</option>
              <option value="RESUELTO">Resueltos</option>
            </select>
          </div>
        </div>

        <div className="terminal-table-wrap">
          <table className="terminal-table">
            <thead>
              <tr>
                <th>Ticket</th>
                <th>Estudiante</th>
                <th>Tipo</th>
                <th>Prioridad</th>
                <th>Descripción</th>
                <th>Fecha</th>
                <th>Estado</th>
                {(currentRole === 'BIENESTAR' || currentRole === 'ADMIN') && (
                  <th>Gestión</th>
                )}
              </tr>
            </thead>
            <tbody>
              {filtered.map(s => {
                let badgeClass = 'badge-status badge-neutral';
                let label = s.estado;
                if (s.estado === 'PENDIENTE') {
                  badgeClass = 'badge-status badge-danger';
                  label = '● Pendiente';
                } else if (s.estado === 'EN_ATENCION') {
                  badgeClass = 'badge-status badge-warning';
                  label = '● En Atención';
                } else if (s.estado === 'RESUELTO') {
                  badgeClass = 'badge-status badge-success';
                  label = '● Resuelto';
                }

                return (
                  <tr key={s.id}>
                    <td style={{ fontWeight: 600 }}>#{s.id}</td>
                    <td style={{ fontWeight: 600, color: 'var(--ink)' }}>{s.nombreEstudiante || 'Santiago Pérez'}</td>
                    <td>{s.tipo}</td>
                    <td>
                      <span className={s.prioridad === 'ALTA' ? 'badge-status badge-danger' : 'badge-status badge-neutral'}>
                        {s.prioridad}
                      </span>
                    </td>
                    <td style={{ fontSize: 12, maxWidth: 280 }}>{s.descripcion}</td>
                    <td style={{ color: 'var(--mute)', fontSize: 12 }}>{s.fecha}</td>
                    <td>
                      <span className={badgeClass}>{label}</span>
                    </td>
                    {(currentRole === 'BIENESTAR' || currentRole === 'ADMIN') && (
                      <td>
                        <select 
                          value={s.estado} 
                          onChange={(e) => handleUpdateEstado(s.id, e.target.value)}
                          className="role-select"
                          style={{ fontSize: 11.5 }}
                        >
                          <option value="PENDIENTE">Pendiente</option>
                          <option value="EN_ATENCION">En Atención</option>
                          <option value="RESUELTO">Resuelto</option>
                        </select>
                      </td>
                    )}
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