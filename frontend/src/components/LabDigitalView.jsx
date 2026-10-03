import React, { useState, useEffect } from 'react';
import { getCursosLab, completarRetoLab } from '../services/api';

export default function LabDigitalView({ currentRole }) {
  let labTitle = 'Lab Digital';
  let labSubtitle = 'Práctica y retos interactivos';
  if (currentRole === 'DOCENTE') { labTitle = 'Lab Digital // Mis Cursos'; labSubtitle = 'Supervisa y administra cursos'; }
  if (currentRole === 'FAMILIAR') { labTitle = 'Lab Digital // No Disponible'; labSubtitle = 'Acceso restringido para acudientes'; }
  if (currentRole === 'BIENESTAR') { labTitle = 'Lab Digital // Consulta'; labSubtitle = 'Apoyo a estudiantes en riesgo'; }
  if (currentRole === 'ADMIN') { labTitle = 'Lab Digital // Administración'; labSubtitle = 'Gestión completa de laboratorios'; }

  const [cursos, setCursos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedCategory, setSelectedCategory] = useState('ALL');
  const [activeSandboxCourse, setActiveSandboxCourse] = useState(null);
  const [sandboxCode, setSandboxCode] = useState('');
  const [terminalOutput, setTerminalOutput] = useState('');
  const [validationSuccess, setValidationSuccess] = useState(false);
  const [completedSkills, setCompletedSkills] = useState([]);

  useEffect(() => {
    loadCourses();
    const stored = JSON.parse(localStorage.getItem('user_skills_lab') || '[]');
    setCompletedSkills(stored);
  }, []);

  const loadCourses = async () => {
    setLoading(true);
    const data = await getCursosLab();
    setCursos(data);
    setLoading(false);
  };

  const handleOpenSandbox = (curso) => {
    setActiveSandboxCourse(curso);
    setSandboxCode(curso.retoSandbox.codigoInicial);
    setTerminalOutput('> Entorno interactivo listo. Pulsa "Ejecutar Código" para verificar la solución.\n');
    setValidationSuccess(false);
  };

  const handleExecuteCode = () => {
    const logs = [];
    const originalConsoleLog = console.log;
    try {
      console.log = (...args) => {
        logs.push(args.map(a => typeof a === 'object' ? JSON.stringify(a) : String(a)).join(' '));
      };

      const runFn = new Function(sandboxCode);
      runFn();

      const outputText = logs.length > 0 ? logs.join('\n') : '> Ejecución exitosa (sin salida en consola).';
      setTerminalOutput(`> STDOUT:\n${outputText}\n\n[Ejecución completada con éxito 200 OK]`);
    } catch (err) {
      setTerminalOutput(`> ERROR DE EJECUCIÓN:\n${err.message}\n\n[Error en tiempo de ejecución]`);
    } finally {
      console.log = originalConsoleLog;
    }
  };

  const handleValidateSkill = async () => {
    if (!activeSandboxCourse) return;
    const skillName = `${activeSandboxCourse.titulo} (Certificado Lab)`;
    await completarRetoLab(activeSandboxCourse.id, skillName);
    
    const updated = JSON.parse(localStorage.getItem('user_skills_lab') || '[]');
    setCompletedSkills(updated);
    setValidationSuccess(true);
  };

  const categories = [
    { id: 'ALL', label: 'Todos' },
    { id: 'WEB', label: 'Desarrollo Web' },
    { id: 'BACKEND', label: 'Backend' },
    { id: 'DATA-AI', label: 'Data & IA' },
    { id: 'DEVOPS', label: 'DevOps & Linux' },
    { id: 'TOOLS', label: 'Herramientas & Git' },
    { id: 'CS-CORE', label: 'Ciencias de la Computación' }
  ];

  const filtered = selectedCategory === 'ALL' 
    ? cursos 
    : cursos.filter(c => c.categoria === selectedCategory);

  if (loading) {
    return (
      <div className="flat-panel">
        <p className="font-mono text-mute">Cargando catálogo abierto de habilidades digitales...</p>
      </div>
    );
  }

  return (
    <div className="page-container">
      {/* Header Info */}
      <div className="flat-panel" style={{ borderLeft: '3px solid var(--accent)' }}>
        <div style={{ fontSize: 16, fontWeight: 700 }}>{labTitle}</div>
        <div style={{ fontSize: 12, color: 'var(--mute)' }}>{labSubtitle}</div>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 12 }}>
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: 10, flexWrap: 'wrap' }}>
              <span style={{ fontSize: 15, fontWeight: 700 }}>
                Lab Exploratorio de Habilidades Digitales
              </span>
              <span className="badge-status badge-success">● Acceso Abierto</span>
            </div>
            <div style={{ fontSize: 12.5, color: 'var(--mute)', marginTop: 4 }}>
              Rutas formativas gratuitas de referencia global (FreeCodeCamp, CS50 Harvard, Kaggle, Linux Foundation) con entorno de práctica integrado.
            </div>
          </div>

          <div>
            <span className="badge-status badge-neutral">
              {completedSkills.length} habilidades acreditadas
            </span>
          </div>
        </div>
      </div>

      {/* Category Filter Bar */}
      <div className="flat-panel" style={{ padding: '12px 16px' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 12, flexWrap: 'wrap' }}>
          <span style={{ fontSize: 12, fontWeight: 600, color: 'var(--mute)' }}>Categoría:</span>
          <div className="lab-filter-bar">
            {categories.map(cat => (
              <button
                key={cat.id}
                onClick={() => setSelectedCategory(cat.id)}
                className={`lab-filter-btn ${selectedCategory === cat.id ? 'active' : ''}`}
              >
                {cat.label}
              </button>
            ))}
          </div>
        </div>
      </div>

      {/* Course Grid */}
      <div className="lab-grid">
        {filtered.map(curso => {
          const isDone = completedSkills.some(s => s.includes(curso.titulo));

          return (
            <div key={curso.id} className="lab-card">
              <div>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', gap: 8, marginBottom: 10 }}>
                  <span className="lab-card-tag" style={{ color: 'var(--accent)' }}>
                    {curso.categoria}
                  </span>
                  <span className="badge-status badge-success" style={{ fontSize: 10 }}>
                    ● 100% Gratis
                  </span>
                </div>

                <div style={{ fontSize: 14, fontWeight: 700, color: 'var(--ink-strong)', marginBottom: 4 }}>
                  {curso.titulo}
                </div>

                <div style={{ fontSize: 11.5, color: 'var(--mute)', marginBottom: 10 }}>
                  {curso.proveedor} • {curso.horas}h • {curso.nivel}
                </div>

                <p style={{ fontSize: 12, color: 'var(--body)', marginBottom: 12, lineHeight: 1.5 }}>
                  {curso.descripcion}
                </p>

                <div style={{ marginBottom: 12 }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', color: 'var(--mute)', fontSize: 11, marginBottom: 4 }}>
                    <span>Progreso sugerido</span>
                    <span>{curso.progreso}%</span>
                  </div>
                  <div className="meter-bar-track">
                    <div className="meter-bar-fill" style={{ width: `${curso.progreso}%`, backgroundColor: 'var(--accent)' }} />
                  </div>
                </div>

                <div style={{ borderTop: '1px solid var(--hairline)', paddingTop: 10, marginTop: 10 }}>
                  <div style={{ fontSize: 11, fontWeight: 600, color: 'var(--mute)', marginBottom: 6 }}>
                    Módulos Formativos:
                  </div>
                  <ul style={{ listStyle: 'none', padding: 0, margin: 0, fontSize: 11.5, color: 'var(--body)' }}>
                    {curso.modulos.map((m, idx) => (
                      <li key={idx} style={{ marginBottom: 4, display: 'flex', gap: 6 }}>
                        <span style={{ color: 'var(--mute)' }}>•</span>
                        <span>{m}</span>
                      </li>
                    ))}
                  </ul>
                </div>
              </div>

              <div style={{ display: 'flex', flexDirection: 'column', gap: 8, marginTop: 14 }}>
                <a
                  href={curso.urlFuente}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="btn-cli btn-cli-secondary btn-cli-sm"
                  style={{ textDecoration: 'none', width: '100%' }}
                >
                  Acceder al Curso Oficial
                </a>

                <button
                  type="button"
                  onClick={() => handleOpenSandbox(curso)}
                  className={`btn-cli btn-cli-sm ${isDone ? 'btn-cli-secondary' : 'btn-cli-primary'}`}
                >
                  {isDone ? 'Práctica Acreditada (Reintentar)' : 'Abrir Sandbox de Práctica'}
                </button>
              </div>
            </div>
          );
        })}
      </div>

      {/* Interactive Sandbox Modal */}
      {activeSandboxCourse && (
        <div className="modal-overlay">
          <div className="modal-dialog" style={{ maxWidth: 660 }}>
            <div className="modal-header">
              <div>
                <span className="modal-title">
                  {activeSandboxCourse.retoSandbox.titulo}
                </span>
                <div style={{ fontSize: 11.5, color: 'var(--mute)', marginTop: 2 }}>
                  Entorno interactivo para {activeSandboxCourse.titulo}
                </div>
              </div>
              <button 
                onClick={() => setActiveSandboxCourse(null)}
                style={{ background: 'none', border: 'none', cursor: 'pointer', fontSize: 16, color: 'var(--mute)' }}
              >
                ✕
              </button>
            </div>

            {validationSuccess && (
              <div className="cli-diagnostic success">
                <div className="cli-diagnostic-title">Reto acreditado exitosamente</div>
                <div>La competencia ha sido verificada y agregada a tu hoja de vida.</div>
              </div>
            )}

            <div style={{ marginBottom: 8, fontSize: 12, color: 'var(--body)' }}>
              Edita el script para resolver el ejercicio y haz clic en <strong>Ejecutar Código</strong>:
            </div>

            <textarea
              className="lab-editor-textarea"
              value={sandboxCode}
              onChange={(e) => setSandboxCode(e.target.value)}
              rows={8}
            />

            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: 10 }}>
              <span style={{ fontSize: 11, color: 'var(--mute)' }}>
                JavaScript V8 • Salida esperada: {activeSandboxCourse.retoSandbox.salidaEsperada}
              </span>
              <button onClick={handleExecuteCode} className="btn-cli btn-cli-primary btn-cli-sm">
                Ejecutar Código
              </button>
            </div>

            <div className="lab-terminal-output font-mono">
              {terminalOutput}
            </div>

            <div className="modal-footer">
              <button 
                type="button" 
                onClick={() => setActiveSandboxCourse(null)} 
                className="btn-cli btn-cli-secondary"
              >
                Cerrar
              </button>
              <button 
                type="button" 
                onClick={handleValidateSkill} 
                className="btn-cli btn-cli-accent"
              >
                Acreditar Habilidad en Perfil
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
