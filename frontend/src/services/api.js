// Servicio centralizado de conexion a API REST Spring Boot con fallback integrado
// En producción se inyecta con la variable VITE_API_URL (ver render.yaml)
const API_BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080';

export async function fetchWithFallback(endpoint, options = {}, fallbackData = null) {
  try {
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), 3000);
    const res = await fetch(`${API_BASE}${endpoint}`, {
      ...options,
      headers: {
        'Content-Type': 'application/json',
        ...(options.headers || {})
      },
      signal: controller.signal
    });
    clearTimeout(timeoutId);
    if (!res.ok) {
      const err = await res.json().catch(() => ({ message: res.statusText }));
      throw new Error(err.message || 'Error en la peticion al servidor');
    }
    return await res.json();
  } catch (error) {
    console.warn(`[API Fallback] ${endpoint}:`, error.message);
    if (fallbackData !== null) {
      return fallbackData;
    }
    throw error;
  }
}

// 1. Estadisticas & Dashboard
export async function getDashboardData() {
  const fallback = {
    totalUsuarios: 20,
    totalEstudiantes: 10,
    totalDocentes: 2,
    totalFamiliares: 4,
    promedioGeneral: 3.75,
    porcentajeAsistencia: 87.5,
    totalSolicitudesBienestar: 8,
    solicitudesPendientes: 3,
    estudiantesEnRiesgo: 3,
    distribucionPorGenero: { Masculino: 5, Femenino: 4, Otro: 1 },
    solicitudesPorTipo: { Academico: 2, Psicologico: 2, Economico: 2, Orientacion: 2 },
    resumenMaterias: [
      { materiaId: 1, nombreMateria: 'Desarrollo Web Full Stack', codigo: 'PROG-301', promedio: 3.8, desviacion: 0.92, minimo: 2.4, maximo: 4.9, totalEstudiantes: 8, porcentajeReprobacion: 30.0 },
      { materiaId: 2, nombreMateria: 'Bases de Datos Relacionales', codigo: 'BD-201', promedio: 3.67, desviacion: 0.82, minimo: 2.6, maximo: 4.7, totalEstudiantes: 7, porcentajeReprobacion: 28.57 },
      { materiaId: 3, nombreMateria: 'Algoritmos y Estructuras', codigo: 'ALGO-101', promedio: 2.65, desviacion: 0.21, minimo: 2.5, maximo: 2.8, totalEstudiantes: 2, porcentajeReprobacion: 100.0 }
    ]
  };
  return fetchWithFallback('/dashboard/indicadores', { method: 'GET' }, fallback);
}

// 2. Estudiantes y Perfil
export async function getEstudiantes() {
  const fallback = [
    { id: 1, nombre: 'Santiago Perez', correo: 'santiago.perez@estudiante.edu.co', programa: 'Desarrollo de Software', semestre: 3, codigoEstudiante: 'EST-2026-001', genero: 'Masculino' },
    { id: 2, nombre: 'Valeria Ramirez', correo: 'valeria.ramirez@estudiante.edu.co', programa: 'Desarrollo de Software', semestre: 3, codigoEstudiante: 'EST-2026-002', genero: 'Femenino' },
    { id: 3, nombre: 'Mateo Morales', correo: 'mateo.morales@estudiante.edu.co', programa: 'Desarrollo de Software', semestre: 2, codigoEstudiante: 'EST-2026-003', genero: 'Masculino' },
    { id: 4, nombre: 'Camila Torres', correo: 'camila.torres@estudiante.edu.co', programa: 'Diseno Grafico Digital', semestre: 2, codigoEstudiante: 'EST-2026-004', genero: 'Femenino' },
    { id: 5, nombre: 'Daniela Ortiz', correo: 'daniela.ortiz@estudiante.edu.co', programa: 'Desarrollo de Software', semestre: 1, codigoEstudiante: 'EST-2026-005', genero: 'Femenino' },
    { id: 6, nombre: 'Juan Diego Marin', correo: 'juan.marin@estudiante.edu.co', programa: 'Redes y Telecomunicaciones', semestre: 3, codigoEstudiante: 'EST-2026-006', genero: 'Masculino' }
  ];
  return fetchWithFallback('/estudiantes', { method: 'GET' }, fallback);
}

export async function getPerfilEstudiante(estudianteId = 1) {
  const fallback = {
    id: 1,
    estudianteId: 1,
    nombreEstudiante: 'Santiago Perez',
    correoEstudiante: 'santiago.perez@estudiante.edu.co',
    programa: 'Desarrollo de Software',
    semestre: 3,
    resumen: 'Estudiante apasionado por el desarrollo fullstack con Spring Boot y React. Enfocado en soluciones limpias y escalables.',
    intereses: 'Arquitectura de software, APIs REST, React, Cloud Computing, Inteligencia Artificial',
    experiencia: 'Monitor academico de Algoritmos durante 2025. Desarrollador Junior freelance en proyectos web.',
    fotoUrl: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300',
    habilidades: [
      { id: 1, nombre: 'Java & Spring Boot', nivel: 'Avanzado', tipo: 'Tecnica' },
      { id: 2, nombre: 'React & Tailwind CSS', nivel: 'Avanzado', tipo: 'Tecnica' },
      { id: 3, nombre: 'SQL / PostgreSQL', nivel: 'Intermedio', tipo: 'Tecnica' },
      { id: 4, nombre: 'Trabajo en Equipo', nivel: 'Avanzado', tipo: 'Blanda' },
      { id: 5, nombre: 'Resolucion de Problemas', nivel: 'Avanzado', tipo: 'Blanda' }
    ],
    proyectos: [
      { id: 1, titulo: 'Plataforma de E-commerce Educativo', descripcion: 'Sistema con catalogo, carrito de compras, checkout y microservicios.', url: 'https://github.com/santiagoperez/ecommerce-demo', tecnologias: 'React, Spring Boot, PostgreSQL, Docker' },
      { id: 2, titulo: 'App de Registro y Asistencia QR', descripcion: 'Aplicacion movil y web para escaneo rapido de presencia en aula.', url: 'https://github.com/santiagoperez/asistencia-qr', tecnologias: 'React Native, Node.js, SQLite' }
    ],
    certificados: [
      { id: 1, nombre: 'Certificacion Profesional en Backend con Spring', institucion: 'Oracle University / Java Foundation', fecha: '2025-11-20', urlArchivo: 'https://certificados.org/oracle-spring.pdf' },
      { id: 2, nombre: 'Especializacion Frontend React Moderno', institucion: 'Platzi / Meta Certified', fecha: '2025-08-15', urlArchivo: 'https://certificados.org/meta-react.pdf' }
    ]
  };
  return fetchWithFallback(`/perfil-estudiante/${estudianteId}`, { method: 'GET' }, fallback);
}

export async function updatePerfilEstudiante(perfilId, data) {
  return fetchWithFallback(`/perfiles/${perfilId}`, {
    method: 'PUT',
    body: JSON.stringify(data)
  }, { ...data, id: perfilId });
}

// 3. Calificaciones y Notas
export async function getNotasEstudiante(estudianteId = 1) {
  const fallback = [
    { id: 1, estudianteId: 1, nombreEstudiante: 'Santiago Perez', materiaId: 1, nombreMateria: 'Desarrollo Web Full Stack', grupoId: 1, nombreGrupo: 'G-WEB-01', valor: 4.8, tipoEvaluacion: 'Parcial 1', fecha: '2026-02-15', comentario: 'Excelente desempeno en API REST', recomendacion: 'Rendimiento sobresaliente.' },
    { id: 2, estudianteId: 1, nombreEstudiante: 'Santiago Perez', materiaId: 1, nombreMateria: 'Desarrollo Web Full Stack', grupoId: 1, nombreGrupo: 'G-WEB-01', valor: 4.5, tipoEvaluacion: 'Quiz 1', fecha: '2026-02-28', comentario: 'Buen dominio de componentes', recomendacion: 'Rendimiento sobresaliente.' },
    { id: 3, estudianteId: 1, nombreEstudiante: 'Santiago Perez', materiaId: 2, nombreMateria: 'Bases de Datos Relacionales', grupoId: 2, nombreGrupo: 'G-BD-01', valor: 4.2, tipoEvaluacion: 'Taller SQL', fecha: '2026-03-05', comentario: 'Consultas bien estructuradas', recomendacion: 'Buen desempeno.' },
    { id: 4, estudianteId: 3, nombreEstudiante: 'Mateo Morales', materiaId: 1, nombreMateria: 'Desarrollo Web Full Stack', grupoId: 1, nombreGrupo: 'G-WEB-01', valor: 2.4, tipoEvaluacion: 'Parcial 1', fecha: '2026-02-15', comentario: 'Dificultad en JPA y Spring', recomendacion: 'Alerta Academica: Se sugiere solicitar asesoria en Bienestar y asistir a monitorias.' },
    { id: 5, estudianteId: 3, nombreEstudiante: 'Mateo Morales', materiaId: 2, nombreMateria: 'Bases de Datos Relacionales', grupoId: 2, nombreGrupo: 'G-BD-01', valor: 2.6, tipoEvaluacion: 'Taller SQL', fecha: '2026-03-05', comentario: 'Consultas incompletas', recomendacion: 'Alerta Academica: Reforzar conceptos previos al examen parcial.' }
  ];
  return fetchWithFallback(`/notas/estudiante/${estudianteId}`, { method: 'GET' }, fallback.filter(n => n.estudianteId === estudianteId || estudianteId === 'all'));
}

export async function registrarNota(data) {
  return fetchWithFallback('/notas', {
    method: 'POST',
    body: JSON.stringify(data)
  }, { ...data, id: Date.now() });
}

// 4. Asistencia
export async function getAsistenciasGrupo(grupoId = 1, fecha = '2026-03-03') {
  const fallback = [
    { id: 1, estudianteId: 1, nombreEstudiante: 'Santiago Perez', grupoId: 1, nombreGrupo: 'G-WEB-01', fecha: '2026-03-03', estado: 'PRESENTE', observaciones: 'Puntual', justificada: false },
    { id: 2, estudianteId: 2, nombreEstudiante: 'Valeria Ramirez', grupoId: 1, nombreGrupo: 'G-WEB-01', fecha: '2026-03-03', estado: 'PRESENTE', observaciones: 'Participativa', justificada: false },
    { id: 3, estudianteId: 3, nombreEstudiante: 'Mateo Morales', grupoId: 1, nombreGrupo: 'G-WEB-01', fecha: '2026-03-03', estado: 'AUSENTE', observaciones: 'Falta no justificada', justificada: false },
    { id: 4, estudianteId: 4, nombreEstudiante: 'Camila Torres', grupoId: 1, nombreGrupo: 'G-WEB-01', fecha: '2026-03-03', estado: 'JUSTIFICADO', observaciones: 'Cita medica validada', justificada: true },
    { id: 5, estudianteId: 6, nombreEstudiante: 'Juan Diego Marin', grupoId: 1, nombreGrupo: 'G-WEB-01', fecha: '2026-03-03', estado: 'PRESENTE', observaciones: '', justificada: false }
  ];
  return fetchWithFallback(`/asistencias/grupo/${grupoId}?fecha=${fecha}`, { method: 'GET' }, fallback);
}

export async function registrarAsistenciaLote(loteData) {
  return fetchWithFallback('/asistencias/lote', {
    method: 'POST',
    body: JSON.stringify(loteData)
  }, loteData.asistencias);
}

// 5. Apoyo y Bienestar
export async function getSolicitudesBienestar() {
  const fallback = [
    { id: 1, estudianteId: 3, nombreEstudiante: 'Mateo Morales', programaEstudiante: 'Desarrollo de Software', tipo: 'Academico', descripcion: 'Confusion con persistencia de datos JPA y programacion orientada a objetos.', fecha: '2026-02-20', prioridad: 'ALTA', estado: 'EN_ATENCION', nombreCategoria: 'Academica', nombreProfesional: 'Lic. Andres Castano', seguimientos: [
      { id: 1, fecha: '2026-02-22', descripcion: 'Se agenda tutoria de refuerzo semanal con monitor.', estado: 'EN_ATENCION' },
      { id: 2, fecha: '2026-03-01', descripcion: 'Estudiante avanza satisfactoriamente en ejercicios practicos.', estado: 'EN_ATENCION' }
    ]},
    { id: 2, estudianteId: 5, nombreEstudiante: 'Daniela Ortiz', programaEstudiante: 'Desarrollo de Software', tipo: 'Psicologico', descripcion: 'Altos niveles de estres y ansiedad previa a presentaciones de proyectos tecnicos.', fecha: '2026-03-04', prioridad: 'MEDIA', estado: 'PENDIENTE', nombreCategoria: 'Emocional', nombreProfesional: 'Dra. Mariana Restrepo', seguimientos: [] },
    { id: 3, estudianteId: 8, nombreEstudiante: 'Lucas Henao', programaEstudiante: 'Desarrollo de Software', tipo: 'Economico', descripcion: 'Dificultad de movilidad y transporte para asistir a jornada de la tarde.', fecha: '2026-02-26', prioridad: 'ALTA', estado: 'EN_ATENCION', nombreCategoria: 'Economica', nombreProfesional: 'Lic. Andres Castano', seguimientos: [] },
    { id: 4, estudianteId: 4, nombreEstudiante: 'Camila Torres', programaEstudiante: 'Diseno Grafico Digital', tipo: 'Orientacion Vocacional', descripcion: 'Orientacion para eleccion de practica productiva.', fecha: '2026-02-28', prioridad: 'BAJA', estado: 'CERRADA', nombreCategoria: 'Academica', nombreProfesional: 'Lic. Andres Castano', seguimientos: [] }
  ];
  return fetchWithFallback('/api/necesidades', { method: 'GET' }, fallback);
}

export async function crearSolicitudBienestar(data) {
  return fetchWithFallback('/api/necesidades', {
    method: 'POST',
    body: JSON.stringify(data)
  }, { ...data, id: Date.now(), estado: 'PENDIENTE', fecha: new Date().toISOString().split('T')[0] });
}

export async function cambiarEstadoBienestar(id, estado) {
  return fetchWithFallback(`/api/necesidades/${id}/estado?estado=${estado}`, {
    method: 'PUT'
  }, { mensaje: `Estado actualizado a ${estado}` });
}

// 6. Registro de Usuario (HUF1 - HUF2)
export async function registrarNuevoUsuario(userData) {
  return fetchWithFallback('/usuarios', {
    method: 'POST',
    body: JSON.stringify(userData)
  }, { ...userData, id: Date.now(), estado: 'ACTIVO' });
}

// 7. Lab Exploratorio de Habilidades Digitales & Cursos Gratuitos
export async function getCursosLab() {
  const fallback = [
    {
      id: 'course-web-01',
      titulo: 'Desarrollo Web Full Stack con JavaScript Moderno',
      proveedor: 'FreeCodeCamp (Certificación Abierta)',
      categoria: 'WEB',
      nivel: 'Principiante',
      horas: 40,
      progreso: 65,
      urlFuente: 'https://www.freecodecamp.org/espanol/learn/javascript-algorithms-and-data-structures-v8/',
      descripcion: 'Aprende los fundamentos del desarrollo web moderno: HTML5 semántico, CSS Flexbox/Grid, algoritmos en JavaScript ES6+ y consumo de APIs.',
      modulos: [
        'Estructura web semántica y accesibilidad',
        'Modelos de caja y layouts con Flexbox y CSS Grid',
        'Programación funcional y manipulación del DOM',
        'Asincronía en JS: Promesas, Async/Await y Fetch API'
      ],
      retoSandbox: {
        titulo: 'Reto Lab: Filtrado y Transformación de Calificaciones',
        lenguaje: 'javascript',
        codigoInicial: `// Reto: Filtra los aprobados (>= 3.0) y obtén sus promedios
const notas = [2.4, 4.5, 3.8, 1.9, 5.0, 3.0];
const aprobados = notas.filter(n => n >= 3.0);
console.log("Total aprobados:", aprobados.length);
console.log("Calificaciones:", aprobados);`,
        salidaEsperada: 'Total aprobados: 4'
      }
    },
    {
      id: 'course-cs-01',
      titulo: 'CS50: Introducción a Ciencias de la Computación',
      proveedor: 'Harvard University / edX (OpenCourseWare)',
      categoria: 'CS-CORE',
      nivel: 'Intermedio',
      horas: 60,
      progreso: 30,
      urlFuente: 'https://cs50.harvard.edu/x/',
      descripcion: 'Entrenamiento riguroso en pensamiento computacional y resolución algorítmica de problemas: C, algoritmos de ordenamiento, memoria y punteros.',
      modulos: [
        'Pensamiento computacional y lógica binaria',
        'Lenguaje C: tipos, bucles y gestión de memoria',
        'Estructuras de datos dinámicas: arrays, listas enlazadas, árboles',
        'Notación Big O y eficiencia algorítmica'
      ],
      retoSandbox: {
        titulo: 'Reto Lab: Búsqueda Binaria O(log n)',
        lenguaje: 'javascript',
        codigoInicial: `function busquedaBinaria(arr, x) {
  let inicio = 0, fin = arr.length - 1;
  while (inicio <= fin) {
    let medio = Math.floor((inicio + fin) / 2);
    if (arr[medio] === x) return medio;
    if (arr[medio] < x) inicio = medio + 1;
    else fin = medio - 1;
  }
  return -1;
}
const lista = [10, 20, 30, 40, 50, 60, 70];
console.log("Índice encontrado de 40:", busquedaBinaria(lista, 40));`,
        salidaEsperada: 'Índice encontrado de 40: 3'
      }
    },
    {
      id: 'course-py-01',
      titulo: 'Python para Análisis de Datos & Machine Learning',
      proveedor: 'Kaggle Learn / Python.org',
      categoria: 'DATA-AI',
      nivel: 'Intermedio',
      horas: 35,
      progreso: 80,
      urlFuente: 'https://www.kaggle.com/learn/python',
      descripcion: 'Domina el ecosistema científico de Python: sintaxis avanzada, comprehensions, manipulación de DataFrames con Pandas y análisis exploratorio.',
      modulos: [
        'Estructuras de control y funciones lambda en Python',
        'Limpieza e imputación de valores faltantes',
        'Análisis estadístico descriptivo con Pandas y NumPy',
        'Visualización de correlaciones y distribuciones con Seaborn'
      ],
      retoSandbox: {
        titulo: 'Reto Lab: Cálculo de Métricas y Desviación',
        lenguaje: 'javascript',
        codigoInicial: `// Simulación de cálculo estadístico en JS
const dataset = [3.8, 4.2, 4.0, 2.5, 4.9, 3.6];
const sum = dataset.reduce((a, b) => a + b, 0);
const mean = (sum / dataset.length).toFixed(2);
console.log("Media aritmética del grupo:", mean);`,
        salidaEsperada: 'Media aritmética del grupo: 3.83'
      }
    },
    {
      id: 'course-backend-01',
      titulo: 'Arquitectura Backend con Java 17 & Spring Boot',
      proveedor: 'Baeldung / Java Community Guide',
      categoria: 'BACKEND',
      nivel: 'Avanzado',
      horas: 45,
      progreso: 45,
      urlFuente: 'https://www.baeldung.com/spring-boot',
      descripcion: 'Construcción de servicios web REST empresariales con Spring Boot 3, inyección de dependencias, Spring Data JPA y arquitectura por capas.',
      modulos: [
        'Principios de Inversión de Control y Dependency Injection',
        'Modelado relacional y mapeo de entidades con Hibernate JPA',
        'Controladores REST y negociación de contenido HTTP',
        'Validaciones con Bean Validation y DTOs seguros'
      ],
      retoSandbox: {
        titulo: 'Reto Lab: Lógica de Negocio y Control de Calificaciones',
        lenguaje: 'javascript',
        codigoInicial: `function validarNota(nota) {
  if (nota < 0.0 || nota > 5.0) throw new Error("Rango de calificación inválido");
  return { valor: nota, aprobado: nota >= 3.0 };
}
console.log("Registro 4.2:", validarNota(4.2));
console.log("Registro 2.8:", validarNota(2.8));`,
        salidaEsperada: 'Registro 4.2: { valor: 4.2, aprobado: true }'
      }
    },
    {
      id: 'course-linux-01',
      titulo: 'Fundamentos de Linux, Terminal & Bash Scripting',
      proveedor: 'Linux Foundation / OverTheWire',
      categoria: 'DEVOPS',
      nivel: 'Principiante',
      horas: 25,
      progreso: 50,
      urlFuente: 'https://overthewire.org/wargames/bandit/',
      descripcion: 'Aprende a navegar servidores Unix como un profesional: comandos de terminal, tuberías (pipes), redirecciones, permisos y automatización con scripts.',
      modulos: [
        'Jerarquía del sistema de archivos y navegación CLI',
        'Manipulación de flujos de texto: grep, sed, awk y cut',
        'Permisos chmod, chown y gestión de procesos con ps/kill',
        'Creación de scripts Bash y variables de entorno'
      ],
      retoSandbox: {
        titulo: 'Reto Lab: Pipeline de Filtrado y Regex',
        lenguaje: 'javascript',
        codigoInicial: `const logs = [
  "2026-10-01 INFO 200 /dashboard/indicadores",
  "2026-10-01 WARN 404 /favicon.ico",
  "2026-10-01 ERROR 500 /api/notas/invalid",
  "2026-10-01 INFO 200 /perfil-estudiante/1"
];
const errores = logs.filter(l => l.includes("ERROR") || l.includes("WARN"));
console.log("Incidencias detectadas:", errores.length);
errores.forEach(e => console.log(">", e));`,
        salidaEsperada: 'Incidencias detectadas: 2'
      }
    },
    {
      id: 'course-git-01',
      titulo: 'Control de Versiones Profesional con Git & GitHub CLI',
      proveedor: 'Pro Git Book / GitHub Skills',
      categoria: 'TOOLS',
      nivel: 'Principiante',
      horas: 20,
      progreso: 90,
      urlFuente: 'https://skills.github.com/',
      descripcion: 'Flujos de trabajo colaborativos en Git: commits semánticos, ramas de funcionalidades, resolución de conflictos merge y automatización con GitHub Actions.',
      modulos: [
        'Estructura de commits, staging area y árbol de objetos',
        'Branching strategies: GitFlow vs Trunk-Based Development',
        'Rebase interactivo, squash y cherry-pick',
        'Pull Requests y revisión de código estandarizada'
      ],
      retoSandbox: {
        titulo: 'Reto Lab: Resolución de Historial y Tagging',
        lenguaje: 'javascript',
        codigoInicial: `const commits = [
  { sha: "a1b2", msg: "feat: add dark mode" },
  { sha: "c3d4", msg: "fix: escape jsx tokens" },
  { sha: "e5f6", msg: "docs: add lab exploratorio" }
];
console.log("Total commits listos para release:", commits.length);
console.log("Último commit:", commits[commits.length - 1].msg);`,
        salidaEsperada: 'Total commits listos para release: 3'
      }
    }
  ];

  return fetchWithFallback('/cursos/digitales', { method: 'GET' }, fallback);
}

export async function completarRetoLab(cursoId, habilidad) {
  // Simulación y persistencia local de habilidades completadas
  const profileKey = 'user_skills_lab';
  const current = JSON.parse(localStorage.getItem(profileKey) || '[]');
  if (!current.includes(habilidad)) {
    current.push(habilidad);
    localStorage.setItem(profileKey, JSON.stringify(current));
  }
  return { success: true, habilidad, totalSkills: current.length };
}