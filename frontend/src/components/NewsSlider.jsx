import React, { useState, useEffect, useRef, useCallback } from 'react';

const SLIDES_BY_ROLE = {
  ESTUDIANTE: [
    {
      tag: 'NOTICIA IA',
      title: 'Google lanza Gemini 2.5 para educadores con descuento institucional',
      body: 'Los estudiantes EDU.CORE acceden al 80% del costo con correo institucional. Incluye tutoría IA, generación de resúmenes y preparación para exámenes.',
      cta: 'Ver cómo aplicar',
      accent: '#3b82f6',
      image: 'https://images.unsplash.com/photo-1677442136019-21780ecad995?w=600&q=80',
    },
    {
      tag: 'CERTIFICADO GRATIS',
      title: 'NVIDIA Deep Learning Institute: Cursos gratis de IA generativa',
      body: 'Cursos autoformativos en Python, CUDA y redes neuronales. Certificado literal al completar cada ruta. Compatible con horario de estudiantes.',
      cta: 'Tomar curso',
      accent: '#22c55e',
      image: 'https://images.unsplash.com/photo-1620712943543-bcc4688e7485?w=600&q=80',
    },
    {
      tag: 'TECNOLOGÍA',
      title: 'Microsoft anuncia Copilot for Education en campus',
      body: 'Integración directa con Microsoft 365 Education. Los estudiantes EDU.CORE pueden activar la vista previa antes del despliegue general.',
      cta: 'Activar acceso',
      accent: '#f59e0b',
      image: 'https://images.unsplash.com/photo-1611162617474-5b21e879e113?w=600&q=80',
    },
    {
      tag: 'CONVOCATORIA',
      title: 'Hackathon Interuniversitario EDU.CORE — 48h de código e IA',
      body: 'Inscripción abierta para equipos de 3-5 estudiantes. Premios en hardware, cloud credits y mentoría NVIDIA. Registro antes del 15 de octubre.',
      cta: 'Inscribir equipo',
      accent: '#a855f7',
      image: 'https://images.unsplash.com/photo-1531482615713-2afd69097998?w=600&q=80',
    },
  ],
  DOCENTE: [
    {
      tag: 'PEDAGOGÍA IA',
      title: 'Google for Education: certificado gratis en IA para docentes',
      body: 'Ruta auto-gestionada sobre integración de Gemini en aula, evaluación formativa y generación de rúbricas. Certificado institucional al finalizar.',
      cta: 'Certificarme ahora',
      accent: '#22c55e',
      image: 'https://images.unsplash.com/photo-1587620962725-abab7fe55159?w=600&q=80',
    },
    {
      tag: 'HERRAMIENTAS',
      title: 'Microsoft Teams con IA: transcripciones en vivo y resúmenes automáticos',
      body: 'Disponible para docentes EDU.CORE. Activación automática en reuniones de tutoría y seguimiento estudiantil.',
      cta: 'Configurar',
      accent: '#3b82f6',
      image: 'https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=600&q=80',
    },
    {
      tag: 'INVESTIGACIÓN',
      title: 'NVIDIA Academic Grant: financiación para proyectos de IA en aula',
      body: 'Hasta $50K USD en créditos cloud para investigaciones pedagógicas con GPU aceleradores. Aplica con carta institucional.',
      cta: 'Aplicar al grant',
      accent: '#f59e0b',
      image: 'https://images.unsplash.com/photo-1532094349884-543b42bd0c07?w=600&q=80',
    },
    {
      tag: 'CAPACITACIÓN',
      title: 'Coursera + Google: ruta gratuita de ML para educación superior',
      body: '12 lecciones sobre machine learning aplicado a analítica estudiantil. Reconocimiento oficial de Coursera e instituciones aliadas.',
      cta: 'Iniciar ruta',
      accent: '#ef4444',
      image: 'https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=600&q=80',
    },
  ],
  FAMILIAR: [
    {
      tag: 'SEGUIMIENTO',
      title: 'Acceso al panel familiar: alertas tempranas y reportes en tiempo real',
      body: 'Configura notificaciones por correo para ausencias, notas críticas y solicitudes de bienestar de tu acudiente.',
      cta: 'Activar alertas',
      accent: '#3b82f6',
      image: 'https://images.unsplash.com/photo-1552664730-d307ca884978?w=600&q=80',
    },
    {
      tag: 'RECURSOS',
      title: 'Guía gratuita: cómo apoyar a tu hijo en la transición al mundo digital',
      body: 'Material preparado por Bienestar Institucional con tips de ciberseguridad, balance digital y acompañamiento académico.',
      cta: 'Descargar guía',
      accent: '#22c55e',
      image: 'https://images.unsplash.com/photo-1484480974693-6ca0a78fb36b?w=600&q=80',
    },
    {
      tag: 'BENEFICIOS',
      title: 'Plataforma Microsoft Family: licencias gratis para el hogar',
      body: 'Hasta 6 miembros pueden acceder a Office 365, OneDrive y herramientas de productividad con el correo institucional del estudiante.',
      cta: 'Activar beneficios',
      accent: '#f59e0b',
      image: 'https://images.unsplash.com/photo-1531297484001-80022131f5a1?w=600&q=80',
    },
  ],
  BIENESTAR: [
    {
      tag: 'PRIORIDAD',
      title: 'Nuevo protocolo de alerta temprana: detección con IA de riesgo académico',
      body: 'Dashboard integrado con métricas de ausencias, notas y participación. Dispara intervenciones antes del punto crítico.',
      cta: 'Ver protocolo',
      accent: '#ef4444',
      image: 'https://images.unsplash.com/photo-1576091160550-2173dba999ef?w=600&q=80',
    },
    {
      tag: 'CAPACITACIÓN',
      title: 'Curso NVIDIA: IA aplicada a detección de señales de malestar',
      body: 'Ruta técnica y ética para equipos de bienestar. Enfocada en herramientas de soporte no invasivo y privacidad estudiantil.',
      cta: 'Inscribir equipo',
      accent: '#a855f7',
      image: 'https://images.unsplash.com/photo-1576091160399-112ba8d25d1d?w=600&q=80',
    },
    {
      tag: 'HERRAMIENTAS',
      title: 'Portal de cita virtual: piloto con IA para triage inicial',
      body: 'Los estudiantes inician con chatbot de clasificación y derivan a profesional humano según urgencia y temática.',
      cta: 'Activar piloto',
      accent: '#22c55e',
      image: 'https://images.unsplash.com/photo-1551650975-87deedd944c3?w=600&q=80',
    },
  ],
  ADMIN: [
    {
      tag: 'SLA',
      title: 'Reporte ejecutivo mensual: retención, SLA y engagement',
      body: 'Acceso a la vista ejecutiva del dashboard con métricas agregadas para presentar a rectoría y consejo académico.',
      cta: 'Ver reporte',
      accent: '#3b82f6',
      image: 'https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=600&q=80',
    },
    {
      tag: 'INTEGRACIÓN',
      title: 'SSO con Google Workspace y Microsoft Entra ID ya disponible',
      body: 'Edu.core ahora soporta autenticación federada. Configura los dominios institucionales desde el panel de administración.',
      cta: 'Configurar SSO',
      accent: '#f59e0b',
      image: 'https://images.unsplash.com/photo-1563986768609-322da13575f3?w=600&q=80',
    },
    {
      tag: 'CONVENIO',
      title: 'Partnership NVIDIA-EDU.CORE: laboratorios de IA para la institución',
      body: 'Acceso a GPUs en la nube a tarifas preferenciales para proyectos académicos y de investigación estudiantil.',
      cta: 'Solicitar convenio',
      accent: '#22c55e',
      image: 'https://images.unsplash.com/photo-1550751827-4bd374c3f58b?w=600&q=80',
    },
  ],
};

const AUTO_ADVANCE_MS = 6000;

export default function NewsSlider({ currentRole }) {
  const [currentIndex, setCurrentIndex] = useState(0);
  const [isPlaying, setIsPlaying] = useState(true);
  const slides = SLIDES_BY_ROLE[currentRole] || SLIDES_BY_ROLE.ESTUDIANTE;
  const intervalRef = useRef(null);

  useEffect(() => {
    setCurrentIndex(0);
  }, [currentRole]);

  const advance = useCallback(() => {
    setCurrentIndex(prev => (prev + 1) % slides.length);
  }, [slides.length]);

  useEffect(() => {
    if (isPlaying && slides.length > 1) {
      intervalRef.current = setInterval(advance, AUTO_ADVANCE_MS);
    }
    return () => clearInterval(intervalRef.current);
  }, [isPlaying, advance, slides.length]);

  const goTo = (idx) => {
    setCurrentIndex(idx);
    setIsPlaying(false);
  };

  const current = slides[currentIndex];

  return (
    <section className="news-slider" aria-label="Noticias y anuncios">
      <div className="news-slide" style={{ '--slide-accent': current.accent }}>
        <div className="news-slide-bg" />
        <div className="news-slide-content">
          <div>
            <div className="news-slide-tag">{current.tag}</div>
            <h2 className="news-slide-title">{current.title}</h2>
            <p className="news-slide-body">{current.body}</p>
            <button type="button" className="news-slide-cta" onClick={() => {}}>
              {current.cta}
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M5 12h14M12 5l7 7-7 7"/>
              </svg>
            </button>
          </div>
          {current.image && (
            <div className="news-slide-image-wrapper">
              <img src={current.image} alt={current.title} className="news-slide-image" />
            </div>
          )}
        </div>
      </div>

      {/* Controls */}
      <div className="news-controls">
        <button
          type="button"
          className="news-playpause-btn"
          onClick={() => setIsPlaying(p => !p)}
          aria-label={isPlaying ? 'Pausar slider' : 'Reproducir slider'}
        >
          {isPlaying ? (
            <svg width="14" height="14" viewBox="0 0 24 24" fill="currentColor"><rect x="6" y="4" width="4" height="16"/><rect x="14" y="4" width="4" height="16"/></svg>
          ) : (
            <svg width="14" height="14" viewBox="0 0 24 24" fill="currentColor"><polygon points="5,3 19,12 5,21"/></svg>
          )}
        </button>

        <div className="news-dots">
          {slides.map((_, idx) => (
            <button
              key={idx}
              type="button"
              className={`news-dot ${idx === currentIndex ? 'active' : ''}`}
              onClick={() => goTo(idx)}
              aria-label={`Ir al slide ${idx + 1}`}
            />
          ))}
        </div>
      </div>

      {/* Glow */}
      <div className="news-glow" style={{ background: current.accent }} />
    </section>
  );
}
