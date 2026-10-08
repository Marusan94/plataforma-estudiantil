import React, { useState, useEffect } from 'react';

const DECISIONS = {
  ESTUDIANTE: [
    { role: 'ALERTA', text: 'Tu asistencia en G-ALGO-01 cayó al 72%. ¿Agenda tutoría?', action: 'Agendar' },
    { role: 'RECOMENDACIÓN', text: 'Promedio en Matemáticas: 3.4/5.0. Prioriza ejercicios de álgebra.', action: 'Ver guías' },
    { role: 'RECORDATORIO', text: '20 minutos hoy en Lab Digital suman 10h este mes.', action: 'Abrir Lab' },
    { role: 'OPORTUNIDAD', text: 'NVIDIA DLI tiene curso gratis de Python. Certificado al finalizar.', action: 'Inscribirme' },
  ],
  DOCENTE: [
    { role: 'ALERTA', text: 'G-ALGO-01 tiene 4 estudiantes en riesgo. ¿Notificar a familiares?', action: 'Notificar' },
    { role: 'RECORDATORIO', text: 'Subir notas del corte antes del viernes 17:00.', action: 'Subir notas' },
    { role: 'REVISIÓN', text: '84% de asistencia semanal. ¿Publicar aviso en la plataforma?', action: 'Publicar' },
    { role: 'RECURSO', text: 'Google for Education: certificado gratuito en IA para docentes.', action: 'Ver certificado' },
  ],
  FAMILIAR: [
    { role: 'SEGUIMIENTO', text: 'Intervención de bienestar abierta desde hace 2 días, sin novedad.', action: 'Consultar' },
    { role: 'RECORDATORIO', text: 'Revisar el boletín al cierre del corte. Próximo: 15 de octubre.', action: 'Ver calendario' },
    { role: 'ALERTA', text: '1 caso de asistencia baja detectado en G-BD-01. ¿Llamar directamente?', action: 'Llamar' },
  ],
  BIENESTAR: [
    { role: 'TRIAGE', text: '3 casos pendientes superan las 24h de SLA. Primer caso lleva 36h.', action: 'Priorizar' },
    { role: 'ALERTA', text: 'Áreas con mayor demanda: rendimiento académico y psicológico.', action: 'Derivar' },
    { role: 'RECURSO', text: 'Curso NVIDIA: IA aplicada a detección de señales de malestar.', action: 'Inscribir equipo' },
  ],
  ADMIN: [
    { role: 'MÉTRICA', text: 'Retención 94.8% (+2.4% vs semestre anterior). ¿Mantener o aumentar meta?', action: 'Ajustar' },
    { role: 'ALERTA', text: 'G-ALGO-01 reprobación 28%. ¿Asignar refuerzo extra o reeditar grupo?', action: 'Decidir' },
    { role: 'REPORTE', text: 'Reporte ejecutivo mensual listo. ¿Compartir con Rectoría?', action: 'Compartir' },
  ],
};

// Chispazos de personalidad: aparecen de vez en cuando entre las misiones
const PERSONALITY = {
  ESTUDIANTE: [
    'Hoo-hoo... los búhos giramos la cabeza 270°. Yo giro tus datos 360°.',
    'Mi farol nunca se apaga. Tu racha de estudio, eso sí, parpadea.',
    'Ojo de búho: ese 3.4 puede ser 4.2 antes del corte. Créeme, veo de noche.',
  ],
  DOCENTE: [
    'Como buen capibara, todo con calma... menos las notas del viernes. Esas no esperan.',
    'Me bañé en el río esta mañana y se me ocurrió: ¿y si G-ALGO-01 necesita un repaso extra?',
    'Los capibaras somos amigos de todos. Incluso de los reportes ejecutivos.',
  ],
  FAMILIAR: [
    'Hoo... estar pendiente ya es la mitad del acompañamiento. Vas bien.',
    'Mi farol ilumina el boletín para que nada te tome por sorpresa.',
  ],
  BIENESTAR: [
    'Hoo-hoo. Tres casos no son una ola: son tres faroles esperando encenderse.',
    'Respira. Prioriza uno a la vez, como aleteo por aleteo.',
  ],
  ADMIN: [
    'Como buen capibara, floto sobre los números... 94.8% se ve bien desde aquí arriba.',
    'Dato curioso: los capibaras no se estresan. Los rectores, a veces sí. Para eso estoy.',
  ],
};

const KIND_LABEL = {
  'alerta': '⚠ Alerta',
  'recomendación': '✦ Te recomiendo',
  'recordatorio': '◷ Recuerda',
  'oportunidad': '★ Oportunidad',
  'revisión': '◉ Revisa esto',
  'seguimiento': '◎ Seguimiento',
  'triage': '✚ Triage',
  'métrica': '▤ Dato clave',
  'reporte': '▤ Reporte listo',
  'recurso': '⬢ Recurso gratis',
  'personalidad': '❖ Te cuento',
};

function reasonReply(message, current, role) {
  const lower = message.toLowerCase();
  if (lower.includes('por qu') || lower.includes('porque') || lower.includes('razón') || lower.includes('razon')) {
    return `Buena pregunta. Lo propongo porque detecté "${current.text}" y tu rol ${role} puede actuar sobre eso hoy mismo.`;
  }
  if (lower.includes('cómo') || lower.includes('como') || lower.includes('hago')) {
    return `Paso a paso: 1) abre el módulo relacionado, 2) revisa el dato que te mostré, 3) ejecuta la acción "${current.action}".`;
  }
  return `Razonando tu mensaje sobre "${current.text}": mi sugerencia es priorizar esto antes que el resto, porque es lo más urgente para tu rol ${role}.`;
}

export default function CopilotWidget({ currentRole, activeTab }) {
  const [hidden, setHidden] = useState(false);
  const [index, setIndex] = useState(0);
  const [chars, setChars] = useState(0);
  const [conversing, setConversing] = useState(false);
  const [log, setLog] = useState([]);
  const [input, setInput] = useState('');
  const [disabledRoles, setDisabledRoles] = useState(() => {
    try { return JSON.parse(localStorage.getItem('owl_disabled_roles') || '{}'); }
    catch (e) { return {}; }
  });
  const decisions = DECISIONS[currentRole] || DECISIONS.ESTUDIANTE;

  useEffect(() => { setIndex(0); setHidden(false); setLog([]); setConversing(false); }, [currentRole, activeTab]);

  useEffect(() => {
    const interval = setInterval(() => setIndex(i => (i + 1) % (decisions.length + 1)), 9000);
    return () => clearInterval(interval);
  }, [decisions]);

  const relevanceScore = (d) => {
    const map = { dashboard: 2, 'lab-digital': 1, academico: 1, asistencia: 1, bienestar: 0, familiar: 0, registro: 0, 'hoja-vida': 0 };
    const keywords = {
      alerta: ['alerta', 'riesg', 'baja', 'falta', 'reprob', 'pendiente'],
      recomendación: ['curso', 'oporat', 'certif', 'guía', 'lab', 'práctica'],
      recordatorio: ['recordatorio', 'boletín', 'notas', 'asistencia'],
      oportunidad: ['curso', 'nvidia', 'google', 'certificado', 'gratis'],
    };
    let score = 0;
    for (const [k, ws] of Object.entries(keywords)) {
      if (ws.some(w => d.text.toLowerCase().includes(w))) score += map[activeTab] ?? 0;
    }
    return score + decisions.indexOf(d) * 0.1;
  };

  const sorted = [...decisions].sort((a, b) => relevanceScore(b) - relevanceScore(a));
  const persLines = PERSONALITY[currentRole] || PERSONALITY.ESTUDIANTE;
  const feed = [...sorted, { role: 'PERSONALIDAD', text: persLines[index % persLines.length], action: 'Jeje' }];
  const current = feed[index] || feed[0];
  const urgent = ['alerta', 'triage'].includes(current.role.toLowerCase());
  const isPersonality = current.role === 'PERSONALIDAD';
  const isTeacher = currentRole === 'DOCENTE';
  const roleDisabled = !!disabledRoles[currentRole];
  const kindLabel = KIND_LABEL[current.role.toLowerCase()] || current.role;

  // Color neutro monocromo por tipo de mensaje
  const KIND_STYLE = {
    'alerta': { color: '#6b7280', border: '#6b7280', icon: '⚠' },
    'recomendación': { color: '#4b5563', border: '#4b5563', icon: '✦' },
    'recordatorio': { color: '#6b7280', border: '#6b7280', icon: '◷' },
    'oportunidad': { color: '#374151', border: '#374151', icon: '★' },
    'revisión': { color: '#4b5563', border: '#4b5563', icon: '◉' },
    'seguimiento': { color: '#6b7280', border: '#6b7280', icon: '◎' },
    'triage': { color: '#374151', border: '#374151', icon: '✚' },
    'métrica': { color: '#4b5563', border: '#4b5563', icon: '▤' },
    'reporte': { color: '#6b7280', border: '#6b7280', icon: '▤' },
    'recurso': { color: '#374151', border: '#374151', icon: '⬢' },
    'personalidad': { color: '#4b5563', border: '#4b5563', icon: '❖' },
  };
  const kindStyle = KIND_STYLE[current.role.toLowerCase()] || { color: '#202020', border: '#202020', icon: '●' };

  const toggleRole = () => {
    setDisabledRoles(prev => {
      const next = { ...prev };
      if (next[currentRole]) delete next[currentRole];
      else next[currentRole] = true;
      try { localStorage.setItem('owl_disabled_roles', JSON.stringify(next)); } catch (e) {}
      return next;
    });
  };

  // Efecto máquina de escribir estilo Pokémon
  useEffect(() => {
    setChars(0);
    const t = setInterval(() => {
      setChars(c => {
        if (c >= current.text.length) { clearInterval(t); return c; }
        return c + 2;
      });
    }, 24);
    return () => clearInterval(t);
  }, [index, currentRole, activeTab]);

  const shown = current.text.slice(0, chars);
  const textDone = chars >= current.text.length;
  const nextMission = () => setIndex(i => (i + 1) % feed.length);

  const speak = () => {
    try {
      const synth = window.speechSynthesis;
      try {
        const Ctx = window.AudioContext || window.webkitAudioContext;
        if (Ctx) {
          const ctx = new Ctx();
          const t0 = ctx.currentTime;
          if (isTeacher) {
            const o = ctx.createOscillator();
            const g = ctx.createGain();
            o.type = 'sawtooth';
            o.frequency.setValueAtTime(110, t0);
            o.frequency.exponentialRampToValueAtTime(65, t0 + 0.3);
            g.gain.setValueAtTime(0.12, t0);
            g.gain.exponentialRampToValueAtTime(0.001, t0 + 0.35);
            o.connect(g); g.connect(ctx.destination);
            o.start(t0); o.stop(t0 + 0.35);
          } else {
            [0, 0.28].forEach((off, i) => {
              const o = ctx.createOscillator();
              const g = ctx.createGain();
              o.type = 'sine';
              o.frequency.setValueAtTime(i === 0 ? 392 : 330, t0 + off);
              g.gain.setValueAtTime(0.0001, t0 + off);
              g.gain.exponentialRampToValueAtTime(0.15, t0 + off + 0.05);
              g.gain.exponentialRampToValueAtTime(0.001, t0 + off + 0.22);
              o.connect(g); g.connect(ctx.destination);
              o.start(t0 + off); o.stop(t0 + off + 0.25);
            });
          }
          setTimeout(() => ctx.close(), 1200);
        }
      } catch (e) {}
      if (!synth) return;
      synth.cancel();
      const utter = new SpeechSynthesisUtterance(current.text);
      utter.lang = 'es-ES';
      const voices = synth.getVoices ? synth.getVoices().filter(v => v.lang && v.lang.toLowerCase().startsWith('es')) : [];
      if (voices.length) {
        const deepHints = ['jorge', 'pablo', 'diego', 'male', 'hombre', 'miguel', 'carlos'];
        const highHints = ['lucia', 'laura', 'paloma', 'helena', 'female', 'mujer', 'sofia', 'valeria'];
        const hints = isTeacher ? deepHints : highHints;
        utter.voice = voices.find(v => hints.some(h => v.name.toLowerCase().includes(h))) || voices[0];
      }
      utter.rate = isTeacher ? 0.85 : 1.15;
      utter.pitch = isTeacher ? 0.6 : 1.4;
      setTimeout(() => synth.speak(utter), 550);
    } catch (e) { /* voz no disponible */ }
  };

  const openConversation = () => {
    setConversing(true);
    setLog([{ from: 'owl', text: `Razonemos juntos: "${current.text}" Pregúntame por qué, o cómo hacerlo.` }]);
  };

  const sendMessage = () => {
    const text = input.trim();
    if (!text) return;
    setLog(prev => [...prev, { from: 'user', text }, { from: 'owl', text: reasonReply(text, current, currentRole) }]);
    setInput('');
  };

  if (roleDisabled) {
    return (
      <div className="robot-core">
        <button type="button" className="owl-restore" onClick={toggleRole} aria-label="Activar mascota">
          {isTeacher ? 'PROFE' : 'GUÍA'} OFF
        </button>
      </div>
    );
  }

  const spriteLabel = isTeacher ? 'Capibara profesor pixel art' : 'Búho guía pixel art con farol';

  return (
    <div className="robot-core">
      {!hidden && (
        <div className="owl-bubble" style={{ borderColor: kindStyle.border }}>
          <button type="button" className="bubble-close" onClick={() => setHidden(true)} aria-label="Minimizar">×</button>
          {!conversing ? (
            <>
              <div className="mission-head">
                <span className="mission-star" style={{ color: kindStyle.color }}>
                  <span className="kind-icon" style={{ color: kindStyle.color, borderColor: kindStyle.border }}>{kindStyle.icon}</span>
                  {kindLabel} · {index + 1}/{feed.length}
                </span>
                <span className={`owl-bubble-tag tag-${current.role.toLowerCase()}`} style={{ color: kindStyle.color, borderColor: kindStyle.border }}>{current.role}</span>
              </div>
              <p className="owl-bubble-text">{shown}<span className="type-caret">▌</span></p>
              {!isPersonality && (
                <div className="mission-reward">RECOMPENSA: +{(index + 1) * 50} XP · INSIGNIA {current.role}</div>
              )}
              <div className="owl-bubble-actions">
                <button type="button" className="owl-bubble-btn" onClick={speak}>♪ Hablar</button>
                <button type="button" className="owl-bubble-btn primary" onClick={openConversation}>Conversar y razonar</button>
              </div>
              {textDone && (
                <button type="button" className="bubble-next" onClick={nextMission} aria-label="Siguiente">▼</button>
              )}
            </>
          ) : (
            <>
              <div className="mission-head">
                <span className="mission-star">Conversando con {isTeacher ? 'el profe' : 'el búho'}</span>
                <button type="button" className="owl-bubble-btn small" onClick={() => setConversing(false)}>Volver</button>
              </div>
              <div className="chat-log bubble-log">
                {log.map((m, i) => (
                  <div key={i} className={`chat-msg ${m.from === 'user' ? 'user' : 'owl'}`}>{m.text}</div>
                ))}
              </div>
              <div className="owl-bubble-actions">
                <input
                  type="text"
                  placeholder="Pregunta por qué o cómo..."
                  className="bubble-input"
                  value={input}
                  onChange={e => setInput(e.target.value)}
                  onKeyDown={e => e.key === 'Enter' && sendMessage()}
                />
                <button type="button" className="owl-bubble-btn primary" onClick={sendMessage}>Razonar</button>
              </div>
            </>
          )}
        </div>
      )}

      <button type="button" className={`robot-avatar owl-avatar${urgent ? ' urgency-high' : ''}`} onClick={() => setHidden(!hidden)} aria-label={spriteLabel}>
        <div
          className={`buho ${isTeacher ? 'capibara' : ''} ${urgent ? 'vuela' : hidden ? 'quieto' : 'camina'}`}
          role="img"
          aria-label={spriteLabel}
        />
        <span className="owl-name">{isTeacher ? 'PROFE' : 'GUÍA'}</span>
      </button>

      <button type="button" className="owl-mini-disable" onClick={toggleRole} aria-label="Desactivar mascota">
        OFF {currentRole}
      </button>
    </div>
  );
}
