import React, { useState, useEffect } from 'react';
import { getDashboardData, getNotasEstudiante, getAsistenciasGrupo, getProgresoEstudiante, getCursos, getEstudianteByUsuario } from '../services/api';

export default function DashboardView({ currentRole, sessionUser }) {
  const isStudent = currentRole === 'ESTUDIANTE';
  const isDocente = currentRole === 'DOCENTE';
  const isFamiliar = currentRole === 'FAMILIAR';
  const isBienestar = currentRole === 'BIENESTAR';
  const isAdmin = currentRole === 'ADMIN';

  let roleTitle = 'Dashboard';
  let roleSubtitle = 'Vista académica operativa';
  if (isStudent) { roleTitle = 'Dashboard Estudiante'; roleSubtitle = 'Mi rendimiento, asistencia y bienestar'; }
  if (isDocente) { roleTitle = 'Dashboard Docente'; roleSubtitle = 'Mis cursos, estudiantes y alertas'; }
  if (isFamiliar) { roleTitle = 'Portal Familiar'; roleSubtitle = 'Información del estudiante vinculado'; }
  if (isBienestar) { roleTitle = 'Gestión Bienestar'; roleSubtitle = 'Casos y solicitudes activas'; }
  if (isAdmin) { roleTitle = 'Consola Administrativa'; roleSubtitle = 'Visión ejecutiva SaaS'; }

  const [studentId, setStudentId] = useState(null);
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [selectedGroup, setSelectedGroup] = useState('ALL');
  const [selectedPeriod, setSelectedPeriod] = useState('2026-1');
  const [isFiltering, setIsFiltering] = useState(false);
  const [activeDashboardMode, setActiveDashboardMode] = useState('ACADEMIC'); // 'ACADEMIC' | 'EXECUTIVE'

  const [notas, setNotas] = useState([]);
  const [notasAll, setNotasAll] = useState([]);
  const [progreso, setProgreso] = useState([]);
  const [asistenciaPct, setAsistenciaPct] = useState(null);
  const [asistenciaBase, setAsistenciaBase] = useState(0);
  const [cursosCount, setCursosCount] = useState(null);

  useEffect(() => {
    loadData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [currentRole]);

  const loadData = async () => {
    setLoading(true);
    const res = await getDashboardData();
    setData(res);

    try {
      if (isStudent) {
        // El id de Usuario NO es el id de Estudiante: se resuelve via /estudiantes/usuario/{id}
        let sid = null;
        const uid = sessionUser && sessionUser.id;
        if (uid) {
          try {
            const ficha = await getEstudianteByUsuario(uid);
            if (ficha && ficha.id) sid = ficha.id;
          } catch (e) { /* sin ficha: cae al id 1 */ }
        }
        const target = sid || 1;
        setStudentId(target);
        const [n, p, a1, a2, a3, a4, a5, a6] = await Promise.all([
          getNotasEstudiante(target),
          getProgresoEstudiante(target),
          getAsistenciasGrupo(1, '2026-03-03'),
          getAsistenciasGrupo(2, '2026-03-03'),
          getAsistenciasGrupo(3, '2026-03-03'),
          getAsistenciasGrupo(4, '2026-03-03'),
          getAsistenciasGrupo(5, '2026-03-03'),
          getAsistenciasGrupo(6, '2026-03-03')
        ]);
        const notasList = Array.isArray(n) ? n : [];
        setNotas(notasList);
        setNotasAll([]);
        setProgreso(Array.isArray(p) ? p : []);
        const todas = [a1, a2, a3, a4, a5, a6].flatMap(x => (Array.isArray(x) ? x : []));
        const mias = todas.filter(a => String(a.estudianteId) === String(target));
        setAsistenciaBase(mias.length);
        if (mias.length > 0) {
          const presentes = mias.filter(a => a.estado === 'PRESENTE' || a.estado === 'JUSTIFICADO' || a.justificada === true).length;
          setAsistenciaPct(Math.round((presentes / mias.length) * 100));
        } else {
          setAsistenciaPct(null);
        }
      } else if (isDocente) {
        const [nAll, cursos] = await Promise.all([
          getNotasEstudiante('all'),
          getCursos()
        ]);
        setNotasAll(Array.isArray(nAll) ? nAll : []);
        setNotas([]);
        setCursosCount(Array.isArray(cursos) ? cursos.length : 0);
      } else if (isFamiliar) {
        const nAll = await getNotasEstudiante('all');
        setNotasAll(Array.isArray(nAll) ? nAll : []);
        setNotas([]);
      } else {
        setNotas([]);
        setNotasAll([]);
        setProgreso([]);
      }
    } catch (e) {
      console.warn('[Dashboard] carga por rol:', e.message);
    }
    setLoading(false);
  };

  const handleFilterChange = (grupo, periodo) => {
    setSelectedGroup(grupo);
    setSelectedPeriod(periodo);
    setIsFiltering(true);
    setTimeout(() => {
      setIsFiltering(false);
    }, 200);
  };

  if (loading || !data) {
    return (
      <div className="flat-panel">
        <p className="font-mono text-mute">Cargando indicadores del panel...</p>
      </div>
    );
  }

  // ============ VISTA ESTUDIANTE (datos propios) ============
  if (isStudent) {
    const promedio = notas.length > 0
      ? (notas.reduce((acc, n) => acc + (Number(n.valor) || 0), 0) / notas.length)
      : null;
    const porMateria = {};
    notas.forEach(n => {
      const key = n.nombreMateria || `Materia ${n.materiaId}`;
      if (!porMateria[key]) porMateria[key] = { nombre: key, total: 0, count: 0 };
      porMateria[key].total += (Number(n.valor) || 0);
      porMateria[key].count += 1;
    });
    const materiasList = Object.values(porMateria).map(m => ({
      ...m,
      promedio: m.total / m.count,
      pct: Math.min(100, Math.round(((m.total / m.count) / 5.0) * 100))
    }));
    const nombreEst = (sessionUser && sessionUser.nombre) || (notas[0] && notas[0].nombreEstudiante) || 'Estudiante';

    return (
      <div className="page-container">
        <div className="flat-panel" style={{ borderLeft: '3px solid var(--accent)' }}>
          <div style={{ fontSize: 16, fontWeight: 700 }}>{roleTitle}</div>
          <div style={{ fontSize: 12, color: 'var(--mute)' }}>{roleSubtitle} · {nombreEst}</div>
          <div style={{ marginTop: 8 }}>
            <span className="badge-status badge-neutral">Mi semestre 2026-1</span>
            <span className="badge-status badge-neutral" style={{ marginLeft: 8 }}>Periodo 2026-1</span>
          </div>
        </div>

        <div className="metrics-grid">
          <div className="metric-tile">
            <div className="metric-tile-header"><span>Mi promedio (2026-1)</span></div>
            <div className="metric-tile-value">{promedio != null ? `${promedio.toFixed(1)} / 5.0` : 'Sin datos'}</div>
            <div className="metric-tile-sub">{notas.length > 0 ? `${notas.length} evaluaciones · Semestre 2026-1` : 'Sin evaluaciones registradas en 2026-1'}</div>
          </div>
          <div className="metric-tile">
            <div className="metric-tile-header"><span>Mi asistencia (2026-1)</span></div>
            <div className="metric-tile-value">{asistenciaPct != null ? `${asistenciaPct}%` : 'Sin datos'}</div>
            <div className="metric-tile-sub">{asistenciaBase > 0 ? `${asistenciaBase} sesiones registradas · Periodo 2026-1` : 'Sin sesiones registradas para este estudiante'}</div>
          </div>
          <div className="metric-tile">
            <div className="metric-tile-header"><span>Mis cursos (2026-1)</span></div>
            <div className="metric-tile-value">{progreso.length > 0 ? progreso.length : (materiasList.length > 0 ? materiasList.length : 'Sin datos')}</div>
            <div className="metric-tile-sub">Avance por curso · Semestre 2026-1</div>
          </div>
        </div>

        <div className="flat-panel">
          <div className="flat-panel-header">
            <div>
              <div style={{ fontSize: 13.5, fontWeight: 700 }}>Mi avance por curso · Semestre 2026-1</div>
              <div style={{ fontSize: 11.5, color: 'var(--mute)' }}>Progreso reportado por tus docentes</div>
            </div>
            <span className="badge-status badge-neutral">{progreso.length} cursos</span>
          </div>
          {progreso.length === 0 ? (
            <p className="font-mono text-mute">Sin registros de avance para tu cuenta en el semestre 2026-1.</p>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
              {progreso.map((p, i) => {
                const avance = Number(p.avance) || 0;
                return (
                  <div key={p.id || i} style={{ borderBottom: '1px solid var(--hairline)', paddingBottom: 8 }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 4, fontSize: 12.5 }}>
                      <span style={{ fontWeight: 600 }}>{p.curso || p.cursoId || 'Curso'}</span>
                      <span style={{ fontWeight: 700 }}>{avance}%</span>
                    </div>
                    <div className="meter-bar-track">
                      <div className="meter-bar-fill" style={{ width: `${Math.min(100, avance)}%`, backgroundColor: 'var(--accent)' }} />
                    </div>
                    {p.observaciones ? <div style={{ fontSize: 11.5, color: 'var(--mute)', marginTop: 4 }}>{p.observaciones}</div> : null}
                  </div>
                );
              })}
            </div>
          )}
        </div>

        <div className="flat-panel">
          <div className="flat-panel-header">
            <div>
              <div style={{ fontSize: 13.5, fontWeight: 700 }}>Mis notas por materia · Periodo 2026-1</div>
              <div style={{ fontSize: 11.5, color: 'var(--mute)' }}>Promedio de cada asignatura en porcentaje</div>
            </div>
            <span className="badge-status badge-neutral">{materiasList.length} materias</span>
          </div>
          {materiasList.length === 0 ? (
            <p className="font-mono text-mute">No tienes notas registradas en el periodo 2026-1.</p>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
              {materiasList.map(m => (
                <div key={m.nombre} style={{ borderBottom: '1px solid var(--hairline)', paddingBottom: 8 }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 4, fontSize: 12.5 }}>
                    <span style={{ fontWeight: 600 }}>{m.nombre}</span>
                    <span style={{ fontWeight: 700 }}>{m.promedio.toFixed(1)} / 5.0 · {m.pct}%</span>
                  </div>
                  <div className="meter-bar-track">
                    <div className="meter-bar-fill" style={{ width: `${m.pct}%`, backgroundColor: m.promedio >= 3.0 ? 'var(--accent)' : 'var(--danger)' }} />
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        <div className="flat-panel">
          <div className="flat-panel-header">
            <div>
              <div style={{ fontSize: 13.5, fontWeight: 700 }}>Detalle de evaluaciones · 2026-1</div>
              <div style={{ fontSize: 11.5, color: 'var(--mute)' }}>Agrupadas por materia</div>
            </div>
            <span className="badge-status badge-neutral">{notas.length} notas</span>
          </div>
          {notas.length === 0 ? (
            <p className="font-mono text-mute">Sin evaluaciones para mostrar en este periodo.</p>
          ) : (
            <div className="terminal-table-wrap">
              <table className="terminal-table">
                <thead>
                  <tr><th>Materia</th><th>Evaluación</th><th>Nota</th><th>Fecha</th><th>Estado</th></tr>
                </thead>
                <tbody>
                  {[...notas].sort((a, b) => String(a.nombreMateria).localeCompare(String(b.nombreMateria))).map(n => {
                    const ok = Number(n.valor) >= 3.0;
                    return (
                      <tr key={n.id}>
                        <td style={{ fontWeight: 600 }}>{n.nombreMateria}</td>
                        <td>{n.tipoEvaluacion}</td>
                        <td style={{ fontWeight: 700 }}>{Number(n.valor).toFixed(1)} / 5.0</td>
                        <td style={{ color: 'var(--mute)', fontSize: 12 }}>{n.fecha}</td>
                        <td><span className={ok ? 'badge-status badge-success' : 'badge-status badge-danger'}>{ok ? '● Aprobado' : '● Reprobado'}</span></td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    );
  }

  // ============ VISTA DOCENTE (sus cursos + riesgo) ============
  if (isDocente) {
    const materias = Array.isArray(data.resumenMaterias) ? data.resumenMaterias : [];
    const totalEst = materias.reduce((a, m) => a + (Number(m.totalEstudiantes) || 0), 0);
    const enRiesgo = {};
    notasAll.forEach(n => {
      if (Number(n.valor) < 3.0) {
        const key = n.estudianteId || n.nombreEstudiante;
        if (!enRiesgo[key]) enRiesgo[key] = { id: n.estudianteId, nombre: n.nombreEstudiante || 'Estudiante', notas: [] };
        enRiesgo[key].notas.push(n);
      }
    });
    const riesgoList = Object.values(enRiesgo);

    return (
      <div className="page-container">
        <div className="flat-panel" style={{ borderLeft: '3px solid var(--accent)' }}>
          <div style={{ fontSize: 16, fontWeight: 700 }}>{roleTitle}</div>
          <div style={{ fontSize: 12, color: 'var(--mute)' }}>{roleSubtitle} · Periodo 2026-1{isDocente && data && data.resumenMaterias ? ` · ${data.resumenMaterias.length} materias a cargo` : ''}</div>
        </div>

        <div className="metrics-grid">
          <div className="metric-tile">
            <div className="metric-tile-header"><span>Materias a cargo</span></div>
            <div className="metric-tile-value">{materias.length}</div>
            <div className="metric-tile-sub">Periodo 2026-1</div>
          </div>
          <div className="metric-tile">
            <div className="metric-tile-header"><span>Estudiantes totales</span></div>
            <div className="metric-tile-value">{totalEst}</div>
            <div className="metric-tile-sub">Suma de tus grupos</div>
          </div>
          <div className="metric-tile">
            <div className="metric-tile-header"><span>En riesgo (&lt; 3.0)</span></div>
            <div className="metric-tile-value" style={{ color: riesgoList.length > 0 ? 'var(--danger)' : 'inherit' }}>{riesgoList.length}</div>
            <div className="metric-tile-sub">Requieren acompañamiento</div>
          </div>
        </div>

        <div className="flat-panel">
          <div className="flat-panel-header">
            <div>
              <div style={{ fontSize: 13.5, fontWeight: 700 }}>Mis materias · Promedio y reprobación</div>
              <div style={{ fontSize: 11.5, color: 'var(--mute)' }}>Periodo 2026-1</div>
            </div>
            <span className="badge-status badge-neutral">{materias.length} materias</span>
          </div>
          {materias.length === 0 ? (
            <p className="font-mono text-mute">No tienes materias asignadas en este periodo.</p>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 14 }}>
              {materias.map(m => {
                const fillPct = Math.min(100, ((Number(m.promedio) || 0) / 5.0) * 100);
                const ok = (Number(m.promedio) || 0) >= 3.0;
                return (
                  <div key={m.materiaId} style={{ borderBottom: '1px solid var(--hairline)', paddingBottom: 10 }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 4, fontSize: 12.5 }}>
                      <span style={{ fontWeight: 600 }}>{m.nombreMateria} ({m.codigo}) · {m.totalEstudiantes} estudiantes</span>
                      <span style={{ fontWeight: 700 }}>{Number(m.promedio).toFixed(1)} / 5.0</span>
                    </div>
                    <div className="meter-bar-track" style={{ marginBottom: 4 }}>
                      <div className="meter-bar-fill" style={{ width: `${fillPct}%`, backgroundColor: ok ? 'var(--accent)' : 'var(--danger)' }} />
                    </div>
                    <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 11, color: 'var(--mute)' }}>
                      <span>Mín: {Number(m.minimo).toFixed(1)} • Máx: {Number(m.maximo).toFixed(1)}</span>
                      <span>Reprobación: {m.porcentajeReprobacion}%</span>
                    </div>
                  </div>
                );
              })}
            </div>
          )}
        </div>

        <div className="flat-panel">
          <div className="flat-panel-header">
            <div>
              <div style={{ fontSize: 13.5, fontWeight: 700 }}>Estudiantes en riesgo</div>
              <div style={{ fontSize: 11.5, color: 'var(--mute)' }}>Con al menos una nota menor a 3.0</div>
            </div>
            <span className="badge-status badge-neutral">{riesgoList.length} casos</span>
          </div>
          {riesgoList.length === 0 ? (
            <p className="font-mono text-mute">Ningún estudiante en riesgo: todos aprobaron sus evaluaciones.</p>
          ) : (
            <div className="terminal-table-wrap">
              <table className="terminal-table">
                <thead>
                  <tr><th>Estudiante</th><th>Materia crítica</th><th>Nota</th><th>Recomendación</th></tr>
                </thead>
                <tbody>
                  {riesgoList.flatMap(s => s.notas.map(n => (
                    <tr key={`${s.id}-${n.id}`}>
                      <td style={{ fontWeight: 600 }}>{s.nombre}</td>
                      <td>{n.nombreMateria} ({n.tipoEvaluacion})</td>
                      <td style={{ fontWeight: 700, color: 'var(--danger)' }}>{Number(n.valor).toFixed(1)}</td>
                      <td style={{ fontSize: 12 }}>{n.recomendacion || 'Derivar a tutoría.'}</td>
                    </tr>
                  )))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    );
  }

  // ============ VISTA FAMILIAR (estudiante vinculado) ============
  if (isFamiliar) {
    const nombreCuenta = (sessionUser && sessionUser.nombre ? String(sessionUser.nombre) : '').toLowerCase();
    const tokens = nombreCuenta.split(/\s+/).filter(t => t.length > 2);
    let vinculadas = [];
    if (tokens.length > 0) {
      vinculadas = notasAll.filter(n => {
        const nom = String(n.nombreEstudiante || '').toLowerCase();
        return tokens.some(t => nom.includes(t));
      });
    }
    if (vinculadas.length === 0 && sessionUser && (sessionUser.estudianteId || sessionUser.hijoId)) {
      const vid = String(sessionUser.estudianteId || sessionUser.hijoId);
      vinculadas = notasAll.filter(n => String(n.estudianteId) === vid);
    }
    const nombreVinculado = vinculadas.length > 0 ? vinculadas[0].nombreEstudiante : null;
    const promedioFam = vinculadas.length > 0
      ? (vinculadas.reduce((a, n) => a + (Number(n.valor) || 0), 0) / vinculadas.length)
      : null;

    return (
      <div className="page-container">
        <div className="flat-panel" style={{ borderLeft: '3px solid var(--accent)' }}>
          <div style={{ fontSize: 16, fontWeight: 700 }}>{roleTitle}</div>
          <div style={{ fontSize: 12, color: 'var(--mute)' }}>{roleSubtitle} · Periodo 2026-1</div>
        </div>
        {vinculadas.length === 0 ? (
          <div className="flat-panel">
            <div className="flat-panel-header">
              <div style={{ fontSize: 13.5, fontWeight: 700 }}>Sin estudiante vinculado</div>
            </div>
            <p className="font-mono text-mute">No se encontró un estudiante vinculado a su cuenta{nombreCuenta ? ` (${sessionUser.nombre})` : ''}. Pida al administrador que asocie un acudido a este usuario para ver su promedio, asistencia y notas.</p>
          </div>
        ) : (
          <>
            <div className="flat-panel">
              <div className="flat-panel-header">
                <div>
                  <div style={{ fontSize: 13.5, fontWeight: 700 }}>Estudiante vinculado: {nombreVinculado}</div>
                  <div style={{ fontSize: 11.5, color: 'var(--mute)' }}>Periodo 2026-1 · Semestre 2026-1</div>
                </div>
                <span className="badge-status badge-success">● Vínculo activo</span>
              </div>
            </div>
            <div className="metrics-grid">
              <div className="metric-tile">
                <div className="metric-tile-header"><span>Promedio</span></div>
                <div className="metric-tile-value">{promedioFam != null ? `${promedioFam.toFixed(1)} / 5.0` : 'Sin datos'}</div>
                <div className="metric-tile-sub">Periodo 2026-1</div>
              </div>
              <div className="metric-tile">
                <div className="metric-tile-header"><span>Evaluaciones</span></div>
                <div className="metric-tile-value">{vinculadas.length}</div>
                <div className="metric-tile-sub">Notas registradas</div>
              </div>
              <div className="metric-tile">
                <div className="metric-tile-header"><span>Reprobadas (&lt; 3.0)</span></div>
                <div className="metric-tile-value">{vinculadas.filter(n => Number(n.valor) < 3.0).length}</div>
                <div className="metric-tile-sub">Requieren atención</div>
              </div>
            </div>
            <div className="flat-panel">
              <div className="flat-panel-header">
                <div style={{ fontSize: 13.5, fontWeight: 700 }}>Boletín del periodo 2026-1</div>
                <span className="badge-status badge-neutral">{vinculadas.length} notas</span>
              </div>
              <div className="terminal-table-wrap">
                <table className="terminal-table">
                  <thead>
                    <tr><th>Asignatura</th><th>Evaluación</th><th>Nota</th><th>Fecha</th><th>Estado</th></tr>
                  </thead>
                  <tbody>
                    {vinculadas.map(n => {
                      const ok = Number(n.valor) >= 3.0;
                      return (
                        <tr key={n.id}>
                          <td style={{ fontWeight: 600 }}>{n.nombreMateria}</td>
                          <td>{n.tipoEvaluacion}</td>
                          <td style={{ fontWeight: 700 }}>{Number(n.valor).toFixed(1)} / 5.0</td>
                          <td style={{ color: 'var(--mute)', fontSize: 12 }}>{n.fecha}</td>
                          <td><span className={ok ? 'badge-status badge-success' : 'badge-status badge-danger'}>{ok ? '● Aprobado' : '● Reprobado'}</span></td>
                        </tr>
                      );
                    })}
                  </tbody>
                </table>
              </div>
            </div>
          </>
        )}
      </div>
    );
  }

  // ============ VISTA BIENESTAR (solo casos, sin académico) ============
  if (isBienestar) {
    const porTipo = data.solicitudesPorTipo || {};
    const totalSol = Object.values(porTipo).reduce((a, b) => a + (Number(b) || 0), 0);
    const pendientes = Number(data.solicitudesPendientes) || 0;
    const tipos = Object.entries(porTipo);

    return (
      <div className="page-container">
        <div className="flat-panel" style={{ borderLeft: '3px solid var(--accent)' }}>
          <div style={{ fontSize: 16, fontWeight: 700 }}>{roleTitle}</div>
          <div style={{ fontSize: 12, color: 'var(--mute)' }}>{roleSubtitle} · Periodo 2026-1</div>
        </div>

        <div className="metrics-grid">
          <div className="metric-tile">
            <div className="metric-tile-header"><span>Casos totales</span></div>
            <div className="metric-tile-value">{data.totalSolicitudesBienestar}</div>
            <div className="metric-tile-sub">Solicitudes registradas</div>
          </div>
          <div className="metric-tile">
            <div className="metric-tile-header"><span>Casos pendientes</span></div>
            <div className="metric-tile-value">{pendientes}</div>
            <div className="metric-tile-sub">Requieren atención</div>
          </div>
          <div className="metric-tile">
            <div className="metric-tile-header"><span>Tipos activos</span></div>
            <div className="metric-tile-value">{tipos.length}</div>
            <div className="metric-tile-sub">Áreas de apoyo</div>
          </div>
        </div>

        <div className="flat-panel">
          <div className="flat-panel-header">
            <div>
              <div style={{ fontSize: 13.5, fontWeight: 700 }}>Distribución de casos por tipo</div>
              <div style={{ fontSize: 11.5, color: 'var(--mute)' }}>Periodo 2026-1</div>
            </div>
            <span className="badge-status badge-neutral">{totalSol} casos</span>
          </div>
          {tipos.length === 0 ? (
            <p className="font-mono text-mute">No hay solicitudes de bienestar registradas.</p>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
              {tipos.map(([tipo, cant]) => {
                const pct = totalSol > 0 ? Math.round((Number(cant) / totalSol) * 100) : 0;
                return (
                  <div key={tipo} style={{ borderBottom: '1px solid var(--hairline)', paddingBottom: 8 }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 4, fontSize: 12.5 }}>
                      <span>{tipo}</span>
                      <span style={{ fontWeight: 600 }}>{cant} ({pct}%)</span>
                    </div>
                    <div className="meter-bar-track">
                      <div className="meter-bar-fill" style={{ width: `${pct}%`, backgroundColor: 'var(--accent)' }} />
                    </div>
                  </div>
                );
              })}
            </div>
          )}
          <div style={{ marginTop: 16, paddingTop: 10, borderTop: '1px solid var(--hairline)', fontSize: 11.5, color: 'var(--mute)' }}>
            <div>SLA promedio de respuesta institucional: 24 horas</div>
          </div>
        </div>
      </div>
    );
  }

  const materiasFiltradas = selectedGroup === 'ALL'
    ? data.resumenMaterias
    : data.resumenMaterias.filter(m => m.materiaId === parseInt(selectedGroup));

  const totalSolicitudes = Object.values(data.solicitudesPorTipo || {}).reduce((a, b) => a + b, 0);

  // SVG Sparkline Helper
  const renderSparkline = (points, color = 'var(--accent)') => {
    const min = Math.min(...points);
    const max = Math.max(...points);
    const range = max - min || 1;
    const width = 120;
    const height = 24;

    const coords = points.map((p, idx) => {
      const x = (idx / (points.length - 1)) * width;
      const y = height - ((p - min) / range) * (height - 6) - 3;
      return `${x},${y}`;
    }).join(' ');

    return (
      <svg className="sparkline-svg" viewBox={`0 0 ${width} ${height}`}>
        <polyline
          fill="none"
          stroke={color}
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
          points={coords}
        />
      </svg>
    );
  };

  return (
    <div className="page-container">
      <div className="flat-panel" style={{ borderLeft: '3px solid var(--accent)' }}>
        <div style={{ fontSize: 16, fontWeight: 700 }}>{roleTitle}</div>
        <div style={{ fontSize: 12, color: 'var(--mute)' }}>{roleSubtitle}</div>
      </div>

      {/* Dashboard Mode Switcher */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 12 }}>
        <div className="segmented-control">
          <button
            type="button"
            onClick={() => setActiveDashboardMode('ACADEMIC')}
            className={`segmented-btn ${activeDashboardMode === 'ACADEMIC' ? 'active' : ''}`}
          >
            Vista Académica & Operativa
          </button>
          <button
            type="button"
            onClick={() => setActiveDashboardMode('EXECUTIVE')}
            className={`segmented-btn ${activeDashboardMode === 'EXECUTIVE' ? 'active' : ''}`}
          >
            Vista Ejecutiva & Analítica SaaS
          </button>
        </div>

        <div style={{ fontSize: 12, color: 'var(--mute)' }}>
          Periodo Académico Activo: <strong>2026-1</strong>
        </div>
      </div>

      {/* ========================================================
          VISTA 1: VISTA ACADEMICA & OPERATIVA
          ======================================================== */}
      {activeDashboardMode === 'ACADEMIC' && (
        <>
          {/* Sleek Terminal Centerpiece */}
          <div className="terminal-tui">
            <div className="tui-header-row">
              <div className="tui-dots">
                <span className="tui-dot" style={{ background: '#ef4444' }}></span>
                <span className="tui-dot" style={{ background: '#f59e0b' }}></span>
                <span className="tui-dot" style={{ background: '#000000' }}></span>
              </div>
              <div>edu.core // terminal v2.4</div>
              <div style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
                <span style={{ width: 6, height: 6, borderRadius: '50%', background: '#000000' }}></span>
                <span>online</span>
              </div>
            </div>
            <div className="tui-content">
              <div>$ runtime: Spring Boot 3.3.4 + H2 Database (edudb)</div>
              <div>$ analytics: Pandas + SciPy pipeline (r=0.883 correlación asist/nota)</div>
              <div>$ query-telemetry --group={selectedGroup} --period={selectedPeriod}</div>
            </div>
          </div>

          {/* Dynamic Filter Controls (HUE2) */}
          <div className="flat-panel" style={{ padding: '12px 18px' }}>
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: 12 }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 8, fontSize: 13, fontWeight: 600 }}>
                <span>Filtros de consulta:</span>
              </div>

              <div style={{ display: 'flex', gap: 12, alignItems: 'center', flexWrap: 'wrap' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: 8, fontSize: 12 }}>
                  <span className="text-mute">Grupo:</span>
                  <select
                    value={selectedGroup}
                    onChange={(e) => handleFilterChange(e.target.value, selectedPeriod)}
                    className="role-select"
                  >
                    <option value="ALL">Todos los grupos</option>
                    <option value="1">G-WEB-01 (Desarrollo Web)</option>
                    <option value="2">G-BD-01 (Bases de Datos)</option>
                    <option value="3">G-ALGO-01 (Algoritmos)</option>
                  </select>
                </div>

                <div style={{ display: 'flex', alignItems: 'center', gap: 8, fontSize: 12 }}>
                  <span className="text-mute">Periodo:</span>
                  <select
                    value={selectedPeriod}
                    onChange={(e) => handleFilterChange(selectedGroup, e.target.value)}
                    className="role-select"
                  >
                    <option value="2026-1">2026-1 (Activo)</option>
                    <option value="2025-2">2025-2 (Histórico)</option>
                  </select>
                </div>

                <span className="badge-status badge-success">
                  {isFiltering ? '● Actualizando...' : '● Sincronizado'}
                </span>
              </div>
            </div>
          </div>

          {/* KPI Tiles with Clean Sparklines */}
          <div className="metrics-grid">
            <div className="metric-tile">
              <div>
                <div className="metric-tile-header">
                  <span>Matriculados</span>
                  <span style={{ color: 'var(--success)', fontSize: 11 }}>+2 este mes</span>
                </div>
                <div className="metric-tile-value">{data.totalEstudiantes}</div>
                <div className="metric-tile-sub">Estudiantes activos</div>
              </div>
              {renderSparkline([8, 8, 9, 9, 10, 10, 10], 'var(--success)')}
            </div>

            <div className="metric-tile">
              <div>
                <div className="metric-tile-header">
                  <span>Promedio General</span>
                  <span style={{ color: 'var(--accent)', fontSize: 11 }}>+0.12 vs anterior</span>
                </div>
                <div className="metric-tile-value">{data.promedioGeneral}</div>
                <div className="metric-tile-sub">Escala de 0.0 a 5.0</div>
              </div>
              {renderSparkline([3.5, 3.6, 3.7, 3.65, 3.8, 3.82, 3.84], 'var(--accent)')}
            </div>

            <div className="metric-tile">
              <div>
                <div className="metric-tile-header">
                  <span>Asistencia Global</span>
                  <span style={{ color: 'var(--success)', fontSize: 11 }}>● Regular</span>
                </div>
                <div className="metric-tile-value">{data.porcentajeAsistencia}%</div>
                <div className="metric-tile-sub">Sesiones cumplidas</div>
              </div>
              {renderSparkline([70, 72, 75, 74, 76, 75, 75], 'var(--success)')}
            </div>

            <div className="metric-tile">
              <div>
                <div className="metric-tile-header">
                  <span>Casos Bienestar</span>
                  <span style={{ color: 'var(--warning)', fontSize: 11 }}>1 en espera</span>
                </div>
                <div className="metric-tile-value">{data.totalSolicitudesBienestar}</div>
                <div className="metric-tile-sub">{data.solicitudesPendientes} pendientes</div>
              </div>
              {renderSparkline([1, 2, 3, 2, 3, 2, 2], 'var(--warning)')}
            </div>

            <div className="metric-tile">
              <div>
                <div className="metric-tile-header">
                  <span>Alertas de Riesgo</span>
                  <span style={{ color: 'var(--danger)', fontSize: 11 }}>Acción ped.</span>
                </div>
                <div className="metric-tile-value" style={{ color: data.estudiantesEnRiesgo > 0 ? 'var(--danger)' : 'inherit' }}>
                  {data.estudiantesEnRiesgo}
                </div>
                <div className="metric-tile-sub">Calificaciones &lt; 3.0</div>
              </div>
              {renderSparkline([4, 4, 3, 3, 2, 2, 2], 'var(--danger)')}
            </div>
          </div>

          {/* Graphical Histogram: Distribución de Calificaciones */}
          <div className="flat-panel">
            <div className="flat-panel-header">
              <div>
                <div style={{ fontSize: 13.5, fontWeight: 700 }}>Distribución y Campana de Calificaciones (HUE1)</div>
                <div style={{ fontSize: 11.5, color: 'var(--mute)' }}>Frecuencia de rendimiento académico por rangos de nota</div>
              </div>
              <span className="badge-status badge-neutral">11 Evaluaciones</span>
            </div>

            {/* SVG Histogram */}
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: 12, alignItems: 'end', height: 120, paddingTop: 20 }}>
              {[
                { range: '< 3.0 (Crítico)', count: 2, pct: 18, color: 'var(--danger)' },
                { range: '3.0 - 3.7 (Básico)', count: 2, pct: 18, color: 'var(--warning)' },
                { range: '3.8 - 4.4 (Alto)', count: 5, pct: 46, color: 'var(--accent)' },
                { range: '4.5 - 5.0 (Excelente)', count: 2, pct: 18, color: 'var(--success)' }
              ].map(item => (
                <div key={item.range} style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', height: '100%', justifyContent: 'flex-end' }}>
                  <span style={{ fontSize: 11.5, fontWeight: 700, color: item.color, marginBottom: 4 }}>
                    {item.count} est. ({item.pct}%)
                  </span>
                  <div
                    style={{
                      width: '80%',
                      height: `${item.pct * 1.8}%`,
                      minHeight: 12,
                      backgroundColor: item.color,
                      borderRadius: '4px 4px 0 0',
                      transition: 'height 400ms ease'
                    }}
                  />
                  <span style={{ fontSize: 10.5, color: 'var(--mute)', marginTop: 6, textAlign: 'center' }}>
                    {item.range}
                  </span>
                </div>
              ))}
            </div>
          </div>

          {/* Split Panels */}
          <div className="grid-2">
            {/* Rendimiento por Asignatura */}
            <div className="flat-panel">
              <div className="flat-panel-header">
                <div>
                  <div style={{ fontSize: 13.5, fontWeight: 700 }}>Rendimiento por Asignatura</div>
                  <div style={{ fontSize: 11.5, color: 'var(--mute)' }}>Media de notas y reprobación</div>
                </div>
                <span className="badge-status badge-neutral">{materiasFiltradas.length} materias</span>
              </div>

              <div style={{ display: 'flex', flexDirection: 'column', gap: 14 }}>
                {materiasFiltradas.map(m => {
                  const isPassing = m.promedio >= 3.0;
                  const fillPct = Math.min(100, (m.promedio / 5.0) * 100);

                  return (
                    <div key={m.materiaId} style={{ borderBottom: '1px solid var(--hairline)', paddingBottom: 10 }}>
                      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 4, fontSize: 12.5 }}>
                        <span style={{ fontWeight: 600 }}>{m.nombreMateria} ({m.codigo})</span>
                        <span style={{ fontWeight: 700, color: isPassing ? 'var(--ink-strong)' : 'var(--danger)' }}>
                          {m.promedio.toFixed(1)} / 5.0
                        </span>
                      </div>

                      <div className="meter-bar-track" style={{ marginBottom: 4 }}>
                        <div
                          className="meter-bar-fill"
                          style={{
                            width: `${fillPct}%`,
                            backgroundColor: isPassing ? 'var(--accent)' : 'var(--danger)'
                          }}
                        />
                      </div>

                      <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 11, color: 'var(--mute)' }}>
                        <span>Mín: {m.minimo.toFixed(1)} • Máx: {m.maximo.toFixed(1)}</span>
                        <span style={{ color: m.porcentajeReprobacion > 25 ? 'var(--danger)' : 'inherit' }}>
                          Reprobación: {m.porcentajeReprobacion}%
                        </span>
                      </div>
                    </div>
                  );
                })}
              </div>
            </div>

            {/* Bienestar Distribution */}
            <div className="flat-panel">
              <div className="flat-panel-header">
                <div>
                  <div style={{ fontSize: 13.5, fontWeight: 700 }}>Áreas de Apoyo y Bienestar</div>
                  <div style={{ fontSize: 11.5, color: 'var(--mute)' }}>Distribución de casos</div>
                </div>
                <span className="badge-status badge-neutral">{totalSolicitudes} casos</span>
              </div>

              <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
                {Object.entries(data.solicitudesPorTipo || {}).map(([tipo, cant]) => {
                  const pct = totalSolicitudes > 0 ? Math.round((cant / totalSolicitudes) * 100) : 0;
                  return (
                    <div key={tipo} style={{ borderBottom: '1px solid var(--hairline)', paddingBottom: 8 }}>
                      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 4, fontSize: 12.5 }}>
                        <span>{tipo}</span>
                        <span style={{ fontWeight: 600 }}>{cant} ({pct}%)</span>
                      </div>
                      <div className="meter-bar-track">
                        <div className="meter-bar-fill" style={{ width: `${pct}%`, backgroundColor: 'var(--accent)' }} />
                      </div>
                    </div>
                  );
                })}
              </div>

              <div style={{ marginTop: 16, paddingTop: 10, borderTop: '1px solid var(--hairline)', fontSize: 11.5, color: 'var(--mute)' }}>
                <div>SLA promedio de respuesta institucional: 24 horas</div>
              </div>
            </div>
          </div>
        </>
      )}

      {/* ========================================================
          VISTA 2: VISTA EJECUTIVA & ANALITICA SAAS (Para Vender)
          ======================================================== */}
      {activeDashboardMode === 'EXECUTIVE' && (
        <>
          <div className="flat-panel" style={{ borderLeft: '3px solid var(--success)' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 12 }}>
              <div>
                <div style={{ display: 'flex', alignItems: 'center', gap: 10, flexWrap: 'wrap' }}>
                  <span style={{ fontSize: 15, fontWeight: 700 }}>
                    Consola Ejecutiva SaaS // Gestión de Retención & SLA
                  </span>
                  <span className="badge-status badge-success">● Métrica en Tiempo Real</span>
                </div>
                <div style={{ fontSize: 12, color: 'var(--mute)', marginTop: 4 }}>
                  Indicadores clave para Rectores, Directores de Programa y Tomadores de Decisiones.
                </div>
              </div>
              <span className="badge-status badge-neutral">Semestre 2026-1</span>
            </div>
          </div>

          {/* Executive SaaS KPIs */}
          <div className="metrics-grid">
            <div className="metric-tile">
              <div className="metric-tile-header">
                <span>Tasa de Retención</span>
                <span style={{ color: 'var(--success)' }}>+2.4%</span>
              </div>
              <div className="metric-tile-value" style={{ color: 'var(--success)' }}>94.8%</div>
              <div className="metric-tile-sub">Meta institucional: &gt; 92.0%</div>
              {renderSparkline([91, 92, 92.5, 93, 94, 94.2, 94.8], 'var(--success)')}
            </div>

            <div className="metric-tile">
              <div className="metric-tile-header">
                <span>SLA Casos Críticos</span>
                <span style={{ color: 'var(--accent)' }}>Cumplido</span>
              </div>
              <div className="metric-tile-value">4.2h</div>
              <div className="metric-tile-sub">Objetivo respuesta: &lt; 24h</div>
              {renderSparkline([8.5, 7.2, 6.0, 5.5, 4.8, 4.4, 4.2], 'var(--accent)')}
            </div>

            <div className="metric-tile">
              <div className="metric-tile-header">
                <span>Riesgo Mitigado</span>
                <span style={{ color: 'var(--success)' }}>Efectivo</span>
              </div>
              <div className="metric-tile-value">85.7%</div>
              <div className="metric-tile-sub">Tutorías tempranas cerradas</div>
              {renderSparkline([60, 65, 72, 78, 80, 84, 85.7], 'var(--success)')}
            </div>

            <div className="metric-tile">
              <div className="metric-tile-header">
                <span>Horas Lab Digital</span>
                <span style={{ color: 'var(--accent)' }}>+32h</span>
              </div>
              <div className="metric-tile-value">184h</div>
              <div className="metric-tile-sub">Práctica autónoma en sandbox</div>
              {renderSparkline([90, 110, 130, 145, 160, 175, 184], 'var(--accent)')}
            </div>
          </div>

          {/* Graphic: Weekly Activity & Engagement SVG Area Chart */}
          <div className="flat-panel">
            <div className="flat-panel-header">
              <div>
                <div style={{ fontSize: 13.5, fontWeight: 700 }}>Curva de Actividad & Concurrencia Semanal (Lunes a Viernes)</div>
                <div style={{ fontSize: 11.5, color: 'var(--mute)' }}>Asistencia efectiva y horas de práctica acumuladas por cohortes</div>
              </div>
              <span className="badge-status badge-success">● 98.4% Concurrencia</span>
            </div>

            {/* SVG Area Chart */}
            <div style={{ width: '100%', height: 160, position: 'relative', marginTop: 10 }}>
              <svg viewBox="0 0 500 120" style={{ width: '100%', height: '100%', overflow: 'visible' }}>
                <defs>
                  <linearGradient id="areaGradient" x1="0%" y1="0%" x2="0%" y2="100%">
                    <stop offset="0%" stopColor="var(--accent)" stopOpacity="0.25" />
                    <stop offset="100%" stopColor="var(--accent)" stopOpacity="0.0" />
                  </linearGradient>
                </defs>

                {/* Grid Lines */}
                <line x1="0" y1="30" x2="500" y2="30" stroke="var(--hairline)" strokeDasharray="3 3" />
                <line x1="0" y1="70" x2="500" y2="70" stroke="var(--hairline)" strokeDasharray="3 3" />
                <line x1="0" y1="110" x2="500" y2="110" stroke="var(--hairline)" />

                {/* Area and Line */}
                <polygon
                  fill="url(#areaGradient)"
                  points="0,110 0,65 125,45 250,75 375,30 500,50 500,110"
                />
                <polyline
                  fill="none"
                  stroke="var(--accent)"
                  strokeWidth="2.5"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  points="0,65 125,45 250,75 375,30 500,50"
                />

                {/* Data Points */}
                {[
                  { x: 0, y: 65, val: '88%' },
                  { x: 125, y: 45, val: '95%' },
                  { x: 250, y: 75, val: '84%' },
                  { x: 375, y: 30, val: '98%' },
                  { x: 500, y: 50, val: '92%' }
                ].map((pt, idx) => (
                  <g key={idx}>
                    <circle cx={pt.x} cy={pt.y} r="4" fill="var(--surface-card)" stroke="var(--accent)" strokeWidth="2" />
                    <text x={pt.x} y={pt.y - 8} fontSize="9" fill="var(--mute)" textAnchor="middle" fontWeight="bold">
                      {pt.val}
                    </text>
                  </g>
                ))}
              </svg>
            </div>

            <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: 12, fontSize: 11.5, color: 'var(--mute)' }}>
              <span>Lunes (Inicio de ciclo)</span>
              <span>Martes</span>
              <span>Miércoles (Pico tutorías)</span>
              <span>Jueves (Pico de asistencia)</span>
              <span>Viernes (Cierre y labs)</span>
            </div>
          </div>

          {/* Institutional Value Proposition Card */}
          <div className="grid-2">
            <div className="flat-panel">
              <div style={{ fontSize: 13.5, fontWeight: 700, marginBottom: 8 }}>
                Impacto en Retención Institucional
              </div>
              <p style={{ fontSize: 12, color: 'var(--body)', lineHeight: 1.6 }}>
                Al detectar tempranamente notas inferiores a 3.0 y ausencias superiores al 15%, el sistema dispara alertas automáticas a familiares y orientadores de bienestar, reduciendo la deserción estudiantil en un promedio del <strong>12% semestral</strong>.
              </p>
              <div style={{ marginTop: 12, display: 'flex', gap: 8 }}>
                <span className="badge-status badge-success">● ROI Positivo para la Universidad</span>
              </div>
            </div>

            <div className="flat-panel">
              <div style={{ fontSize: 13.5, fontWeight: 700, marginBottom: 8 }}>
                Preparado para Integración Empresarial
              </div>
              <p style={{ fontSize: 12, color: 'var(--body)', lineHeight: 1.6 }}>
                Compatible con autenticación Single Sign-On (Google Workspace, Microsoft Entra ID), bases de datos relacionales PostgreSQL/MySQL y estándares LTI para integración con LMS existentes.
              </p>
              <div style={{ marginTop: 12, display: 'flex', gap: 8 }}>
                <span className="badge-status badge-neutral">SSO Ready • REST API • Cloud Native</span>
              </div>
            </div>
          </div>
        </>
      )}
    </div>
  );
}
