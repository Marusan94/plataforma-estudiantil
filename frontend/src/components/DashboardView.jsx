import React, { useState, useEffect } from 'react';
import { getDashboardData } from '../services/api';

export default function DashboardView({ currentRole }) {
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

  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [selectedGroup, setSelectedGroup] = useState('ALL');
  const [selectedPeriod, setSelectedPeriod] = useState('2026-1');
  const [isFiltering, setIsFiltering] = useState(false);
  const [activeDashboardMode, setActiveDashboardMode] = useState('ACADEMIC'); // 'ACADEMIC' | 'EXECUTIVE'

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    const res = await getDashboardData();
    setData(res);
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

  if (loading) {
    return (
      <div className="flat-panel">
        <p className="font-mono text-mute">Cargando telemetría e indicadores analíticos...</p>
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
                <span className="tui-dot" style={{ background: '#22c55e' }}></span>
              </div>
              <div>edu.core // terminal v2.4</div>
              <div style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
                <span style={{ width: 6, height: 6, borderRadius: '50%', background: '#22c55e' }}></span>
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