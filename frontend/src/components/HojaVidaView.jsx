import React, { useState, useEffect, useRef } from 'react';
import { getPerfilEstudiante, updatePerfilEstudiante } from '../services/api';
import { jsPDF } from 'jspdf';
import html2pdf from 'html2pdf.js';
import { Document, Packer, Paragraph, TextRun, HeadingLevel, AlignmentType, Packer as DocxPacker } from 'docx';
import { saveAs } from 'file-saver';

export default function HojaVidaView({ currentRole }) {
  const [perfil, setPerfil] = useState(null);
  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState(false);
  const [formData, setFormData] = useState({});
  const [previewPhoto, setPreviewPhoto] = useState('');
  const [saveAlert, setSaveAlert] = useState(false);
  const [atsScore, setAtsScore] = useState(null);
  const [atsSuggestions, setAtsSuggestions] = useState([]);
  const [showAssistant, setShowAssistant] = useState(false);
  const [assistantMessages, setAssistantMessages] = useState([
    { role: 'assistant', content: '🧠 Asistente IA de Hoja de Vida activado. Pide sugerencias, análisis ATS o mejoras.' }
  ]);
  const [assistantInput, setAssistantInput] = useState('');
  const cvRef = useRef(null);

  useEffect(() => {
    loadPerfil();
  }, []);

  const loadPerfil = async () => {
    setLoading(true);
    const data = await getPerfilEstudiante(1);
    setPerfil(data);
    setFormData({
      resumen: data.resumen,
      intereses: data.intereses,
      experiencia: data.experiencia,
      fotoUrl: data.fotoUrl
    });
    setPreviewPhoto(data.fotoUrl);
    setLoading(false);
  };

  const handlePhotoUpload = (e) => {
    const file = e.target.files[0];
    if (file) {
      const url = URL.createObjectURL(file);
      setPreviewPhoto(url);
      setFormData(prev => ({ ...prev, fotoUrl: url }));
    }
  };

  const handleSave = async (e) => {
    e.preventDefault();
    await updatePerfilEstudiante(perfil.id, formData);
    setPerfil(prev => ({ ...prev, ...formData }));
    setEditing(false);
    setSaveAlert(true);
    setTimeout(() => setSaveAlert(false), 3000);
  };

  // Generate ATS score and suggestions
  const generateATSAnalysis = () => {
    if (!perfil) return;
    let score = 70;
    const suggestions = [];

    if (perfil.resumen && perfil.resumen.length > 100) score += 10;
    else suggestions.push('Añade un resumen profesional más detallado (100+ caracteres)');

    if (perfil.habilidades && perfil.habilidades.length >= 5) score += 10;
    else suggestions.push('Añade al menos 5 habilidades técnicas y blandas');

    if (perfil.experiencia && perfil.experiencia.length > 50) score += 5;
    else suggestions.push('Detalla tu experiencia y proyectos destacados');

    if (perfil.proyectos && perfil.proyectos.length >= 2) score += 5;
    else suggestions.push('Incluye al menos 2 proyectos con tecnologías y enlaces');

    if (perfil.certificados && perfil.certificados.length >= 1) score += 5;
    else suggestions.push('Añade certificaciones relevantes');

    setAtsScore(Math.min(100, score));
    setAtsSuggestions(suggestions);
  };

  // Download as PDF
  const downloadPDF = async () => {
    if (!cvRef.current) return;
    const element = cvRef.current;
    const opt = {
      margin: 0.5,
      filename: `CV_${perfil.nombreEstudiante.replace(/\s+/g, '_')}.pdf`,
      image: { type: 'jpeg', quality: 0.98 },
      html2canvas: { scale: 2, useCORS: true, logging: false },
      jsPDF: { unit: 'in', format: 'letter', orientation: 'portrait' },
      pagebreak: { mode: ['avoid-all', 'css', 'legacy'] }
    };
    await html2pdf().set(opt).from(element).save();
  };

  // Download as Word
  const downloadWord = async () => {
    if (!perfil) return;
    const doc = new Document({
      sections: [{
        properties: {},
        children: [
          new Paragraph({
            children: [new TextRun({ text: perfil.nombreEstudiante, bold: true, size: 32, color: '1a1a2e' })],
            alignment: AlignmentType.CENTER,
            spacing: { after: 200 }
          }),
          new Paragraph({
            children: [
              new TextRun({ text: `${perfil.programa} • Semestre ${perfil.semestre}`, size: 20, color: '4a4a6a' }),
              new TextRun({ text: '\n', break: 1 }),
              new TextRun({ text: perfil.correoEstudiante, size: 20, color: '4a4a6a' }),
            ],
            alignment: AlignmentType.CENTER,
            spacing: { after: 300 }
          }),
          new Paragraph({
            children: [new TextRun({ text: 'RESUMEN PROFESIONAL', bold: true, size: 24, color: '1a1a2e', underline: true })],
            spacing: { after: 100 }
          }),
          new Paragraph({
            children: [new TextRun({ text: perfil.resumen || 'Sin resumen', size: 22 })],
            spacing: { after: 200 }
          }),
          new Paragraph({
            children: [new TextRun({ text: 'HABILIDADES Y COMPETENCIAS', bold: true, size: 24, color: '1a1a2e', underline: true })],
            spacing: { after: 100 }
          }),
          ...(perfil.habilidades?.map(h => new Paragraph({
            children: [
              new TextRun({ text: `• ${h.nombre} (${h.nivel})`, size: 22, bold: h.tipo === 'Tecnica' }),
              new TextRun({ text: ` [${h.tipo}]`, size: 20, color: '6a6a8a' })
            ],
            spacing: { after: 60 }
          })) || []),
          new Paragraph({
            children: [new TextRun({ text: 'EXPERIENCIA Y PROYECTOS', bold: true, size: 24, color: '1a1a2e', underline: true })],
            spacing: { after: 100 }
          }),
          new Paragraph({
            children: [new TextRun({ text: perfil.experiencia || 'Sin experiencia previa registrada.', size: 22 })],
            spacing: { after: 200 }
          }),
          new Paragraph({
            children: [new TextRun({ text: 'PROYECTOS DESTACADOS', bold: true, size: 24, color: '1a1a2e', underline: true })],
            spacing: { after: 100 }
          }),
          ...(perfil.proyectos?.map(p => new Paragraph({
            children: [
              new TextRun({ text: p.titulo, bold: true, size: 22 }),
              new TextRun({ text: '\n', break: 1 }),
              new TextRun({ text: p.descripcion, size: 20 }),
              new TextRun({ text: `\nTecnologías: ${p.tecnologias}`, size: 20, color: '4a4a6a', italics: true }),
              new TextRun({ text: `\nURL: ${p.url}`, size: 20, color: '0066cc', underline: true }),
            ],
            spacing: { after: 150 }
          })) || []),
          new Paragraph({
            children: [new TextRun({ text: 'CERTIFICACIONES', bold: true, size: 24, color: '1a1a2e', underline: true })],
            spacing: { after: 100 }
          }),
          ...(perfil.certificados?.map(c => new Paragraph({
            children: [
              new TextRun({ text: c.nombre, bold: true, size: 22 }),
              new TextRun({ text: ` • ${c.institucion}`, size: 20, color: '4a4a6a' }),
              new TextRun({ text: ` • ${c.fecha}`, size: 20, color: '6a6a8a' }),
            ],
            spacing: { after: 80 }
          })) || []),
          new Paragraph({
            children: [new TextRun({ text: 'ÁREAS DE INTERÉS', bold: true, size: 24, color: '1a1a2e', underline: true })],
            spacing: { after: 100 }
          }),
          new Paragraph({
            children: [new TextRun({ text: perfil.intereses || 'Sin intereses registrados', size: 22 })],
          })
        ]
      }]
    });

    const blob = await DocxPacker.toBlob(doc);
    saveAs(blob, `CV_${perfil.nombreEstudiante.replace(/\s+/g, '_')}.docx`);
  };

  // AI Assistant handlers
  const handleAssistantSend = async (e) => {
    e.preventDefault();
    if (!assistantInput.trim()) return;
    const userMsg = assistantInput;
    setAssistantMessages(prev => [...prev, { role: 'user', content: userMsg }]);
    setAssistantInput('');

    // Simulate AI response
    setTimeout(() => {
      let response = '';
      const lower = userMsg.toLowerCase();
      if (lower.includes('ats') || lower.includes('puntaje') || lower.includes('score')) {
        generateATSAnalysis();
        response = `📊 Tu puntuación ATS actual: ${atsScore || 'calculando...'}/100. ${atsSuggestions.length > 0 ? 'Sugerencias: ' + atsSuggestions.join('; ') : '¡Excelente perfil!'}`;
      } else if (lower.includes('mejora') || lower.includes('suger') || lower.includes('consejo')) {
        generateATSAnalysis();
        response = `💡 Sugerencias para mejorar tu CV:\n${atsSuggestions.map(s => `• ${s}`).join('\n')}`;
      } else if (lower.includes('habilidad') || lower.includes('skill')) {
        response = '🔧 Añade habilidades técnicas específicas (ej: "Spring Boot 3", "React 18", "PostgreSQL 15") y nivel. Las blandas: "Liderazgo", "Comunicación técnica".';
      } else {
        response = '🤖 Puedo ayudarte con: análisis ATS, sugerencias de mejora, palabras clave para tu sector, formato profesional. ¿Qué necesitas?';
      }
      setAssistantMessages(prev => [...prev, { role: 'assistant', content: response }]);
    }, 500);
  };

  if (loading) {
    return (
      <div className="flat-panel">
        <p className="font-mono text-mute">Cargando perfil curricular y competencias...</p>
      </div>
    );
  }

  const currentProfile = formData.resumen ? formData : perfil;

  return (
    <div className="page-container">
      {saveAlert && (
        <div className="cli-diagnostic success">
          <div className="cli-diagnostic-title">Hoja de vida actualizada</div>
          <div>Los cambios en tu perfil profesional han sido persistidos en el sistema institucional.</div>
        </div>
      )}

      {/* Toolbar */}
      <div className="cv-toolbar">
        <div className="cv-toolbar-left">
          <span className="cv-title">📄 Hoja de Vida Profesional</span>
          <span className="cv-subtitle">{currentProfile.nombreEstudiante} • {currentProfile.programa} • Semestre {currentProfile.semestre}</span>
        </div>
        <div className="cv-toolbar-right">
          <button onClick={generateATSAnalysis} className="btn-cli btn-cli-secondary btn-cli-sm" title="Análisis ATS">
            📊 ATS Score
          </button>
          <button onClick={downloadPDF} className="btn-cli btn-cli-primary btn-cli-sm" title="Descargar PDF">
            📥 PDF
          </button>
          <button onClick={downloadWord} className="btn-cli btn-cli-secondary btn-cli-sm" title="Descargar Word">
            📄 Word
          </button>
          <button onClick={() => setShowAssistant(!showAssistant)} className={`btn-cli btn-cli-sm ${showAssistant ? 'btn-cli-primary' : 'btn-cli-secondary'}`} title="Asistente IA">
            🤖 IA
          </button>
          <button onClick={() => setEditing(true)} className="btn-cli btn-cli-accent btn-cli-sm">
            ✏️ Editar
          </button>
        </div>
      </div>

      {/* ATS Score Display */}
      {atsScore !== null && (
        <div className="ats-score-banner">
          <div className="ats-score-circle" style={{ background: `conic-gradient(var(--success) ${atsScore * 3.6}deg, var(--hairline) 0deg)` }}>
            <span>{atsScore}</span>
          </div>
          <div>
            <strong>Puntuación ATS: {atsScore}/100</strong>
            {atsSuggestions.length > 0 && <span> • {atsSuggestions.join(' | ')}</span>}
          </div>
        </div>
      )}

      {/* AI Assistant Panel */}
      {showAssistant && (
        <div className="assistant-panel">
          <div className="assistant-header">
            <span>🤖 Asistente de Hoja de Vida</span>
            <button onClick={() => setShowAssistant(false)} className="btn-cli btn-cli-ghost btn-cli-xs">✕</button>
          </div>
          <div className="assistant-messages">
            {assistantMessages.map((msg, i) => (
              <div key={i} className={`assistant-msg ${msg.role}`}>
                <span>{msg.content}</span>
              </div>
            ))}
          </div>
          <form onSubmit={handleAssistantSend} className="assistant-input-form">
            <input
              type="text"
              value={assistantInput}
              onChange={(e) => setAssistantInput(e.target.value)}
              placeholder="Pide análisis ATS, sugerencias, palabras clave..."
              className="cli-input"
            />
            <button type="submit" className="btn-cli btn-cli-primary btn-cli-sm">Enviar</button>
          </form>
        </div>
      )}

      {/* CV Content - Professional Layout */}
      <div ref={cvRef} className="cv-document" id="cv-document">
        {/* Header */}
        <header className="cv-header">
          <div className="cv-header-main">
            <div className="cv-photo-wrapper">
              <img 
                src={previewPhoto || currentProfile.fotoUrl} 
                alt={currentProfile.nombreEstudiante} 
                className="cv-photo"
              />
            </div>
            <div className="cv-header-info">
              <h1 className="cv-name">{currentProfile.nombreEstudiante}</h1>
              <p className="cv-title-role">{currentProfile.programa} • Semestre {currentProfile.semestre}</p>
              <div className="cv-contact">
                <span>📧 {currentProfile.correoEstudiante}</span>
                <span>🆔 EST-2026-001</span>
                <span>📍 Estudiante Activo</span>
              </div>
            </div>
          </div>
          <div className="cv-header-accent"></div>
        </header>

        {/* Professional Summary */}
        <section className="cv-section">
          <h2 className="cv-section-title">📋 Resumen Profesional</h2>
          <p className="cv-summary">{currentProfile.resumen}</p>
        </section>

        {/* Skills */}
        <section className="cv-section">
          <h2 className="cv-section-title">🛠️ Habilidades y Competencias</h2>
          <div className="cv-skills-grid">
            <div className="cv-skills-category">
              <h3>Técnicas</h3>
              <div className="cv-skills-list">
                {currentProfile.habilidades?.filter(h => h.tipo === 'Tecnica').map(h => (
                  <span key={h.id} className="cv-skill-badge tech">
                    {h.nombre} <span className="skill-level">({h.nivel})</span>
                  </span>
                ))}
              </div>
            </div>
            <div className="cv-skills-category">
              <h3>Blandas / Transversales</h3>
              <div className="cv-skills-list">
                {currentProfile.habilidades?.filter(h => h.tipo !== 'Tecnica').map(h => (
                  <span key={h.id} className="cv-skill-badge soft">
                    {h.nombre} <span className="skill-level">({h.nivel})</span>
                  </span>
                ))}
              </div>
            </div>
          </div>
        </section>

        {/* Experience */}
        <section className="cv-section">
          <h2 className="cv-section-title">💼 Experiencia y Proyectos Destacados</h2>
          <div className="cv-experience">
            <p className="cv-experience-text">{currentProfile.experiencia}</p>
          </div>
        </section>

        {/* Projects */}
        {currentProfile.proyectos && currentProfile.proyectos.length > 0 && (
          <section className="cv-section">
            <h2 className="cv-section-title">🚀 Proyectos Académicos y Personales</h2>
            <div className="cv-projects">
              {currentProfile.proyectos.map(p => (
                <article key={p.id} className="cv-project-card">
                  <div className="cv-project-header">
                    <h3 className="cv-project-title">{p.titulo}</h3>
                    <a href={p.url} target="_blank" rel="noopener noreferrer" className="cv-project-link">Ver código →</a>
                  </div>
                  <p className="cv-project-desc">{p.descripcion}</p>
                  <div className="cv-project-tech">{p.tecnologias}</div>
                </article>
              ))}
            </div>
          </section>
        )}

        {/* Certifications */}
        {currentProfile.certificados && currentProfile.certificados.length > 0 && (
          <section className="cv-section">
            <h2 className="cv-section-title">🏆 Certificaciones</h2>
            <ul className="cv-certifications">
              {currentProfile.certificados.map(c => (
                <li key={c.id} className="cv-cert-item">
                  <div>
                    <strong>{c.nombre}</strong>
                    <br />
                    <small>{c.institucion} • {c.fecha}</small>
                  </div>
                  <a href={c.urlArchivo} target="_blank" rel="noopener noreferrer" className="cv-cert-link">Ver certificado</a>
                </li>
              ))}
            </ul>
          </section>
        )}

        {/* Interests */}
        <section className="cv-section">
          <h2 className="cv-section-title">🎯 Áreas de Interés y Metas</h2>
          <p className="cv-interests">{currentProfile.intereses}</p>
        </section>

        {/* Footer */}
        <footer className="cv-footer">
          <p>Hoja de vida generada por EDU.CORE Platform • {new Date().toLocaleDateString('es-CO')}</p>
          <p className="cv-footer-note">Expediente académico validado por Registro y Control Académico</p>
        </footer>
      </div>

      {/* Edit Modal */}
      {editing && (
        <div className="modal-overlay" onClick={() => setEditing(false)}>
          <div className="modal-dialog" onClick={e => e.stopPropagation()}>
            <div className="modal-header">
              <span className="modal-title">Actualizar Hoja de Vida Estudiantil</span>
              <button onClick={() => setEditing(false)} style={{ background: 'none', border: 'none', cursor: 'pointer', fontSize: 16, color: 'var(--mute)' }}>✕</button>
            </div>
            <form onSubmit={handleSave}>
              <div className="cli-form-group">
                <label className="cli-label">Fotografía de Perfil:</label>
                <input type="file" accept="image/*" onChange={handlePhotoUpload} className="cli-input" />
              </div>
              <div className="cli-form-group">
                <label className="cli-label">Resumen Profesional / Perfil:</label>
                <textarea rows={4} value={formData.resumen} onChange={(e) => setFormData({ ...formData, resumen: e.target.value })} className="cli-textarea" />
              </div>
              <div className="cli-form-group">
                <label className="cli-label">Intereses y Metas:</label>
                <input type="text" value={formData.intereses} onChange={(e) => setFormData({ ...formData, intereses: e.target.value })} className="cli-input" />
              </div>
              <div className="cli-form-group">
                <label className="cli-label">Experiencia y Proyectos Destacados:</label>
                <textarea rows={4} value={formData.experiencia} onChange={(e) => setFormData({ ...formData, experiencia: e.target.value })} className="cli-textarea" />
              </div>
              <div className="modal-footer">
                <button type="button" onClick={() => setEditing(false)} className="btn-cli btn-cli-secondary">Cancelar</button>
                <button type="submit" className="btn-cli btn-cli-primary">Guardar Cambios</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}