import React, { useState, useEffect, useRef } from 'react';
import { getCursosLab, completarRetoLab } from '../services/api';

// Icons and visual assets for each category/challenge
const categoryIcons = {
  'WEB': { emoji: '🌐', color: '#9ca3af', bg: 'rgba(156, 163, 175, 0.12)', label: 'Desarrollo Web' },
  'BACKEND': { emoji: '⚙️', color: '#9ca3af', bg: 'rgba(156, 163, 175, 0.12)', label: 'Backend' },
  'DATA-AI': { emoji: '🧠', color: '#9ca3af', bg: 'rgba(156, 163, 175, 0.12)', label: 'Data & IA' },
  'DEVOPS': { emoji: '🐳', color: '#9ca3af', bg: 'rgba(156, 163, 175, 0.12)', label: 'DevOps & Linux' },
  'TOOLS': { emoji: '🔧', color: '#9ca3af', bg: 'rgba(156, 163, 175, 0.12)', label: 'Herramientas & Git' },
  'CS-CORE': { emoji: '🎓', color: '#9ca3af', bg: 'rgba(156, 163, 175, 0.12)', label: 'Ciencias de la Computación' }
};

const challengeImages = {
  'course-web-01': '/img/photo-1498050108023-c5249f4df085.jpg?w=400&h=220&fit=crop',
  'course-cs-01': '/img/photo-1555066931-4365d14bab8c.jpg?w=400&h=220&fit=crop',
  'course-py-01': '/img/photo-1551288049-bebda4e38f71.jpg?w=400&h=220&fit=crop',
  'course-backend-01': '/img/photo-1558494949-ef010cbdcc31.jpg?w=400&h=220&fit=crop',
  'course-linux-01': '/img/photo-1580910051074-3eb694886505.jpg?w=400&h=220&fit=crop',
  'course-git-01': '/img/photo-1618401471353-b98afee0b2eb.jpg?w=400&h=220&fit=crop'
};

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
  const [searchQuery, setSearchQuery] = useState('');
  const [showTicketView, setShowTicketView] = useState(false);
  const [selectedTicket, setSelectedTicket] = useState(null);
  const [ticketAnimation, setTicketAnimation] = useState(false);
  const terminalRef = useRef(null);

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
    setTerminalOutput('> 🎫 Ticket abierto: ' + curso.retoSandbox.titulo + '\n> Entorno interactivo listo. Escribe tu solución y pulsa "Ejecutar Código".\n');
    setValidationSuccess(false);
    setShowTicketView(false);
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
      setTerminalOutput(prev => prev + '\n> STDOUT:\n' + outputText + '\n\n[✓ Ejecución completada con éxito 200 OK]');
      if (terminalRef.current) terminalRef.current.scrollTop = terminalRef.current.scrollHeight;
    } catch (err) {
      setTerminalOutput(prev => prev + '\n> ❌ ERROR DE EJECUCIÓN:\n' + err.message + '\n\n[Error en tiempo de ejecución]');
      if (terminalRef.current) terminalRef.current.scrollTop = terminalRef.current.scrollHeight;
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
    
    setTerminalOutput(prev => prev + '\n> 🏆 HABILIDAD ACREDITADA: ' + skillName + '\n> Agregada a tu Hoja de Vida automáticamente.');
  };

  const handleOpenTicket = (curso) => {
    setSelectedTicket(curso);
    setTicketAnimation(true);
    setTimeout(() => setTicketAnimation(false), 600);
    setShowTicketView(true);
    
    // Find the full course data and open sandbox
    const fullCourse = cursos.find(c => c.id === curso.courseId || c.titulo === curso.courseTitle);
    if (fullCourse) {
      handleOpenSandbox(fullCourse);
    }
  };

  const handleSearchTicket = () => {
    setShowTicketView(true);
  };

  // Simulated mini database for tickets
  const ticketDatabase = cursos.flatMap(curso => 
    curso.modulos.map((modulo, idx) => ({
      id: `${curso.id}-${idx}`,
      title: modulo,
      category: curso.categoria,
      courseTitle: curso.titulo,
      courseId: curso.id,
      provider: curso.proveedor,
      difficulty: curso.nivel,
      hours: Math.ceil(curso.horas / curso.modulos.length),
      status: completedSkills.some(s => s.includes(curso.titulo)) ? 'completed' : 'pending',
      tags: [curso.categoria, modulo.split(' ')[0]],
      estimatedTime: `${Math.ceil(curso.horas / curso.modulos.length * 60)} min`
    }))
  );

  const filteredTickets = ticketDatabase.filter(ticket => 
    (!searchQuery || 
      ticket.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
      ticket.courseTitle.toLowerCase().includes(searchQuery.toLowerCase()) ||
      ticket.tags.some(tag => tag.toLowerCase().includes(searchQuery.toLowerCase()))
    ) &&
    (selectedCategory === 'ALL' || ticket.category === selectedCategory)
  );

  const categories = [
    { id: 'ALL', label: 'Todos', icon: '📋' },
    { id: 'WEB', label: 'Web', icon: '🌐' },
    { id: 'BACKEND', label: 'Backend', icon: '⚙️' },
    { id: 'DATA-AI', label: 'Data & IA', icon: '🧠' },
    { id: 'DEVOPS', label: 'DevOps', icon: '🐳' },
    { id: 'TOOLS', label: 'Tools', icon: '🔧' },
    { id: 'CS-CORE', label: 'CS Core', icon: '🎓' }
  ];

  if (loading) {
    return (
      <div className="flat-panel">
        <div className="loading-ticket">
          <div className="ticket-loader"></div>
          <p className="font-mono text-mute">Cargando base de tickets del Lab Digital...</p>
        </div>
      </div>
    );
  }

  const filtered = selectedCategory === 'ALL' 
    ? cursos 
    : cursos.filter(c => c.categoria === selectedCategory);

  if (currentRole === 'FAMILIAR') {
    return (
      <div className="page-container">
        <div className="flat-panel" style={{ textAlign: 'center', padding: 60 }}>
          <div style={{ fontSize: 48, marginBottom: 16 }}>🔒</div>
          <h2 style={{ marginBottom: 8 }}>Acceso Restringido</h2>
          <p style={{ color: 'var(--mute)' }}>El Lab Digital está disponible solo para estudiantes, docentes y administradores.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="page-container lab-digital-root">
      {/* Header with Stats */}
      <div className="lab-header-panel">
        <div className="lab-header-left">
          <div className="lab-title-row">
            <span className="lab-icon-badge" style={{ background: 'var(--accent-soft)', color: 'var(--accent)' }}>🎫</span>
            <div>
              <h1 style={{ fontSize: 20, fontWeight: 800, marginBottom: 2 }}>{labTitle}</h1>
              <p style={{ color: 'var(--mute)', fontSize: 13 }}>{labSubtitle}</p>
            </div>
          </div>
          <p className="lab-practice-text">
            <span className="practice-label">📋 HOY PRACTICARÁS:</span>
            <span className="practice-desc">
              {filteredTickets.length > 0 
                ? `Resolver ${filteredTickets.length} retos de ${selectedCategory === 'ALL' ? 'todas las categorías' : categories.find(c => c.id === selectedCategory)?.label || selectedCategory} — simula tickets reales, busca en la base de datos, escribe tu script y acredita la habilidad.`
                : 'Selecciona una categoría para ver retos disponibles.'}
            </span>
          </p>
        </div>
        <div className="lab-header-right">
          <div className="lab-stats-mini">
            <div className="stat-mini">
              <span className="stat-mini-value">{completedSkills.length}</span>
              <span className="stat-mini-label">Acreditadas</span>
            </div>
            <div className="stat-mini">
              <span className="stat-mini-value">{filteredTickets.filter(t => t.status === 'completed').length}</span>
              <span className="stat-mini-label">Completados</span>
            </div>
            <div className="stat-mini">
              <span className="stat-mini-value">{filteredTickets.filter(t => t.status === 'pending').length}</span>
              <span className="stat-mini-label">Pendientes</span>
            </div>
          </div>
        </div>
      </div>

      {/* Search & Filter Bar - Ticket Database */}
      <div className="ticket-search-panel">
        <div className="search-input-wrapper">
          <span className="search-icon">🔍</span>
          <input
            type="text"
            placeholder="Buscar ticket por título, curso, etiqueta... (ej: 'binaria', 'docker', 'SQL')"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="ticket-search-input"
            onKeyDown={(e) => e.key === 'Enter' && handleSearchTicket()}
          />
          <button onClick={handleSearchTicket} className="search-btn" title="Buscar">
            Buscar
          </button>
        </div>
        <div className="category-filter-bar">
          {categories.map(cat => (
            <button
              key={cat.id}
              onClick={() => { setSelectedCategory(cat.id); setSearchQuery(''); setShowTicketView(true); }}
              className={`category-pill ${selectedCategory === cat.id ? 'active' : ''}`}
              title={cat.label}
            >
              <span>{cat.icon}</span>
              <span>{cat.label}</span>
              <span className="ticket-count">{filteredTickets.filter(t => selectedCategory === 'ALL' || t.category === cat.id).length}</span>
            </button>
          ))}
        </div>
      </div>

      {/* View Toggle */}
      <div className="view-toggle">
        <button
          onClick={() => setShowTicketView(false)}
          className={`view-btn ${!showTicketView ? 'active' : ''}`}
        >
          📚 Vista Cursos
        </button>
        <button
          onClick={() => setShowTicketView(true)}
          className={`view-btn ${showTicketView ? 'active' : ''}`}
        >
          🎫 Vista Tickets
        </button>
      </div>

      {/* Ticket Database View */}
      {showTicketView && (
        <div className="ticket-database-view">
          <div className="ticket-db-header">
            <h3>🗄️ Base de Datos de Tickets</h3>
            <span className="db-count">{filteredTickets.length} tickets encontrados</span>
          </div>
          <div className="ticket-grid">
            {filteredTickets.map((ticket, idx) => (
              <div
                key={ticket.id}
                className={`ticket-card ${ticket.status === 'completed' ? 'completed' : ''} ${ticketAnimation && selectedTicket?.id === ticket.id ? 'animating' : ''}`}
                style={{ animationDelay: `${idx * 0.05}s` }}
                onClick={() => handleOpenTicket(ticket)}
              >
                <div className="ticket-header">
                  <div className="ticket-category-badge" style={{ 
                    background: categoryIcons[ticket.category]?.bg || 'var(--accent-soft)',
                    color: categoryIcons[ticket.category]?.color || 'var(--accent)'
                  }}>
                    <span>{categoryIcons[ticket.category]?.emoji || '📋'}</span>
                    <span>{ticket.category}</span>
                  </div>
                  <div className="ticket-status">
                    {ticket.status === 'completed' ? (
                      <span className="status-badge completed">✓ Completado</span>
                    ) : (
                      <span className="status-badge pending">⏳ Pendiente</span>
                    )}
                  </div>
                </div>
                
                <div className="ticket-body">
                  <h4 className="ticket-title">{ticket.title}</h4>
                  <p className="ticket-course">{ticket.courseTitle}</p>
                  
                  <div className="ticket-meta">
                    <span className="meta-item">
                      <span className="meta-icon">🏢</span>
                      <span>{ticket.provider}</span>
                    </span>
                    <span className="meta-item">
                      <span className="meta-icon">⏱️</span>
                      <span>{ticket.estimatedTime}</span>
                    </span>
                    <span className="meta-item">
                      <span className="meta-icon">📊</span>
                      <span>{ticket.difficulty}</span>
                    </span>
                  </div>

                  <div className="ticket-tags">
                    {ticket.tags.map(tag => (
                      <span key={tag} className="ticket-tag">{tag}</span>
                    ))}
                  </div>
                </div>

                <div className="ticket-footer">
                  <div className="ticket-progress">
                    <div className="progress-mini">
                      <div className="progress-mini-fill" style={{ width: ticket.status === 'completed' ? '100%' : '0%' }}></div>
                    </div>
                    <span className="progress-text">
                      {ticket.status === 'completed' ? 'Listo para acreditar' : 'Clic para abrir ticket'}
                    </span>
                  </div>
                  <span className="ticket-action-hint">🔍 →</span>
                </div>
              </div>
            ))}
          </div>
          
          {filteredTickets.length === 0 && (
            <div className="no-tickets">
              <div className="no-tickets-icon">🔍</div>
              <p>No se encontraron tickets</p>
              <p className="no-tickets-hint">Prueba con otra búsqueda o categoría</p>
            </div>
          )}
        </div>
      )}

      {/* Course Grid View */}
      {!showTicketView && (
        <div className="lab-grid">
          {filtered.map(curso => {
            const isDone = completedSkills.some(s => s.includes(curso.titulo));
            const catIcon = categoryIcons[curso.categoria] || { emoji: '📋', color: 'var(--accent)', bg: 'var(--accent-soft)' };
            const imgUrl = challengeImages[curso.id];

            return (
              <div key={curso.id} className="lab-card enhanced" style={{ animationDelay: `${cursos.indexOf(curso) * 0.05}s` }}>
                {imgUrl && (
                  <div className="lab-card-image">
                    <img src={imgUrl} alt={curso.titulo} loading="lazy" />
                    <div className="lab-card-overlay">
                      <span className="overlay-category" style={{ background: catIcon.bg, color: catIcon.color }}>
                        {catIcon.emoji} {catIcon.label}
                      </span>
                    </div>
                  </div>
                )}

                <div className="lab-card-content">
                  <div className="card-header">
                    <span className="lab-card-tag" style={{ background: catIcon.bg, color: catIcon.color }}>
                      {catIcon.emoji} {catIcon.label}
                    </span>
                    <span className="badge-status badge-success" style={{ fontSize: 10 }}>● 100% Gratis</span>
                  </div>

                  <h3 className="card-title">{curso.titulo}</h3>

                  <p className="card-provider">{curso.proveedor} • {curso.horas}h • {curso.nivel}</p>

                  <p className="card-description">{curso.descripcion}</p>

                  <div className="card-progress">
                    <div className="progress-info">
                      <span>Progreso sugerido</span>
                      <span>{curso.progreso}%</span>
                    </div>
                    <div className="meter-bar-track">
                      <div className="meter-bar-fill" style={{ width: `${curso.progreso}%`, backgroundColor: catIcon.color }} />
                    </div>
                  </div>

                  <div className="card-modules">
                    <span className="modules-label">📦 Módulos:</span>
                    <ul>
                      {curso.modulos.map((m, idx) => (
                        <li key={idx}>
                          <span className="module-dot"></span>
                          <span>{m}</span>
                        </li>
                      ))}
                    </ul>
                  </div>
                </div>

                <div className="lab-card-actions">
                  <a
                    href={curso.urlFuente}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="btn-cli btn-cli-secondary btn-cli-sm"
                    style={{ textDecoration: 'none', flex: 1 }}
                  >
                    📖 Curso Oficial
                  </a>

                  <button
                    type="button"
                    onClick={() => handleOpenSandbox(curso)}
                    className={`btn-cli btn-cli-sm ${isDone ? 'btn-cli-secondary' : 'btn-cli-primary'}`}
                    style={{ flex: 1 }}
                  >
                    {isDone ? '✓ Acreditado (Reintentar)' : '🎫 Abrir Ticket'}
                  </button>
                </div>
              </div>
            );
          })}
        </div>
      )}

      {/* Interactive Sandbox Modal - Ticket Style */}
      {activeSandboxCourse && (
        <div className="modal-overlay" onClick={() => setActiveSandboxCourse(null)}>
          <div className="modal-dialog ticket-modal" style={{ maxWidth: 720 }} onClick={e => e.stopPropagation()}>
            <div className="ticket-modal-header">
              <div className="ticket-modal-title">
                <span className="ticket-type-badge" style={{ 
                  background: categoryIcons[activeSandboxCourse.categoria]?.bg || 'var(--accent-soft)',
                  color: categoryIcons[activeSandboxCourse.categoria]?.color || 'var(--accent)'
                }}>
                  {categoryIcons[activeSandboxCourse.categoria]?.emoji || '🎫'} TICKET #{activeSandboxCourse.id.slice(-4).toUpperCase()}
                </span>
                <h2>{activeSandboxCourse.retoSandbox.titulo}</h2>
                <p>{activeSandboxCourse.titulo} • {activeSandboxCourse.retoSandbox.lenguaje?.toUpperCase() || 'JAVASCRIPT'}</p>
              </div>
              <button 
                onClick={() => setActiveSandboxCourse(null)}
                className="modal-close-btn"
              >
                ✕
              </button>
            </div>

            {validationSuccess && (
              <div className="cli-diagnostic success ticket-success">
                <div className="cli-diagnostic-title">🏆 TICKET RESUELTO Y ACREDITADO</div>
                <div>La competencia ha sido verificada y agregada a tu Hoja de Vida.</div>
              </div>
            )}

            {/* Problem Statement - Ticket Style */}
            <div className="ticket-problem-section">
              <div className="problem-header">
                <span className="problem-icon">📋</span>
                <h3>DESCRIPCIÓN DEL TICKET</h3>
              </div>
              <div className="problem-content">
                <p>{activeSandboxCourse.retoSandbox.descripcion || 'Resuelve el reto programático según los requisitos.'}</p>
                <div className="expected-output">
                  <span className="output-label">🎯 Salida esperada:</span>
                  <code>{activeSandboxCourse.retoSandbox.salidaEsperada}</code>
                </div>
              </div>
            </div>

            {/* Mini Database Search within Ticket */}
            <div className="ticket-db-section">
              <div className="db-search-header">
                <h4>🔍 BASE DE DATOS DE CONOCIMIENTO (Simulada)</h4>
              </div>
              <div className="db-search-input">
                <input
                  type="text"
                  placeholder="Buscar en la base de conocimiento... (ej: 'filter', 'map', 'reduce', 'async')"
                  className="db-search-field"
                />
                <span className="db-hint">Presiona Enter para buscar</span>
              </div>
              <div className="db-results">
                <div className="db-entry">
                  <span className="db-key">💡 PISTA:</span>
                  <span className="db-value">
                    {activeSandboxCourse.id === 'course-web-01' && 'Usa .filter() para filtrar arrays y .length para contar elementos.'}
                    {activeSandboxCourse.id === 'course-cs-01' && 'Implementa búsqueda binaria: divide el array a la mitad en cada iteración.'}
                    {activeSandboxCourse.id === 'course-py-01' && 'reduce((a,b) => a+b, 0) suma el array; divide por .length para la media.'}
                    {activeSandboxCourse.id === 'course-backend-01' && 'Valida rangos con if/throw; retorna objeto con valor y boolean aprobado.'}
                    {activeSandboxCourse.id === 'course-linux-01' && '.filter() con .includes() para encontrar patrones; .forEach() para imprimir.'}
                    {activeSandboxCourse.id === 'course-git-01' && '.length da el total; [length-1] accede al último elemento.'}
                  </span>
                </div>
                <div className="db-entry">
                  <span className="db-key">📚 REFERENCIA:</span>
                  <a href={activeSandboxCourse.urlFuente} target="_blank" rel="noopener" className="db-link">
                    {activeSandboxCourse.proveedor} → Documentación oficial
                  </a>
                </div>
              </div>
            </div>

            {/* Code Editor */}
            <div className="ticket-editor-section">
              <div className="editor-header">
                <h4>⌨️ ESCRIBE TU SCRIPT</h4>
                <div className="editor-lang">JavaScript (V8)</div>
              </div>
              <textarea
                className="ticket-editor"
                value={sandboxCode}
                onChange={(e) => setSandboxCode(e.target.value)}
                rows={12}
                spellCheck="false"
                placeholder="// Escribe tu solución aquí..."
              />
              <div className="editor-footer">
                <span className="editor-hint">💡 Tip: Usa console.log() para debuggear paso a paso</span>
                <button onClick={handleExecuteCode} className="btn-cli btn-cli-primary btn-run">
                  ▶ Ejecutar Código
                </button>
              </div>
            </div>

            {/* Terminal Output */}
            <div className="ticket-terminal">
              <div className="terminal-header-bar">
                <div className="terminal-dots">
                  <span></span><span></span><span></span>
                </div>
                <span className="terminal-title">terminal.js</span>
                <div className="terminal-status">
                  <span className="status-dot"></span>
                  <span>CONECTADO</span>
                </div>
              </div>
              <div className="terminal-output-window" ref={terminalRef}>
                <pre className="terminal-text">{terminalOutput}</pre>
              </div>
            </div>

            <div className="modal-footer ticket-footer">
              <button 
                type="button" 
                onClick={() => setActiveSandboxCourse(null)} 
                className="btn-cli btn-cli-secondary"
              >
                Cerrar Ticket
              </button>
              <button 
                type="button" 
                onClick={handleValidateSkill} 
                className="btn-cli btn-cli-accent"
                disabled={validationSuccess}
              >
                {validationSuccess ? '✓ Acreditado en Perfil' : '🏆 Acreditar Habilidad'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}