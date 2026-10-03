# EDU.CORE · Plataforma Integral de Gestión y Acompañamiento Estudiantil

![Stack](https://img.shields.io/badge/stack-SpringBoot%20·%20React%20·%20Python-blue)
![Status](https://img.shields.io/badge/status-en%20desarrollo-yellow)
![License](https://img.shields.io/badge/licencia-uso%20institucional-lightgrey)

Plataforma web integral para la gestión académica, social y analítica de estudiantes universitarios. Backend desacoplado en **Spring Boot**, frontend moderno en **React + Vite** y módulo de analítica en **Python/Pandas**, con interfaces diferenciadas por rol y mascotas guía pixel-art con IA proactiva.

## ✨ Características principales

- **Slider de noticias por rol** — carrusel con noticias de IA, tecnología y educación, más certificados gratuitos (NVIDIA, Google, Microsoft). Contenido distinto para Estudiante, Docente, Familiar, Bienestar y Admin.
- **Interfaces por rol** — cada rol ve sus propios tabs y su propio dashboard: el estudiante ve su rendimiento, el docente sus cursos, el familiar el seguimiento, bienestar el triage y admin la consola ejecutiva SaaS.
- **Mascotas guía pixel-art** — búho con farol (roles generales) y capibara profesor (docentes), con sprite sheets animados cuadro por cuadro, patrullaje, nube de diálogo estilo RPG, voz sintetizada y decisiones proactivas con sistema de XP.
- **Lab Digital** — sandbox con retos de código y ejecución en vivo.
- **Módulos completos** — Hoja de vida, Académico (notas), Asistencia, Bienestar (tickets), Familiar y Registro.
- **Vista ejecutiva SaaS** — retención, SLA, curva de actividad semanal y narrativa de impacto institucional.

## 🏗️ Arquitectura

```
plataforma-estudiantil/
├── backend/                 # Spring Boot 3.3.4 · Java 17 · JPA · H2
│   └── src/main/java/com/edu/plataforma/
│       ├── config/          # CORS, DataLoader con datos de demostración
│       ├── controller/      # REST: usuarios, estudiantes, notas, asistencias, dashboard
│       ├── dto/             # DTOs con Bean Validation
│       ├── exception/       # Manejador global de excepciones
│       ├── model/           # Entidades JPA
│       ├── repository/      # Repositorios con queries avanzadas
│       └── service/         # Lógica de negocio
├── frontend/                # React 18 + Vite · SPA con diseño dark/light
│   └── src/
│       ├── components/      # Vistas por módulo + NewsSlider + CopilotWidget
│       ├── services/        # Cliente API con fallback local
│       ├── App.jsx          # Selector de rol y enrutador
│       └── index.css        # Design system propio
├── data-analysis/           # Python 3.12 · Pandas · NumPy · SciPy
│   ├── scripts/             # Análisis por módulo (registro, notas, asistencia…)
│   ├── reports/             # Gráficas PNG y reportes CSV generados
│   └── run_all_analysis.py  # Orquestador de todos los análisis
└── iniciar_plataforma.bat   # Arranque automatizado en Windows
```

## 🚀 Puesta en marcha

### Opción 1 · Script automatizado (Windows)

Doble clic en `iniciar_plataforma.bat` en la raíz del proyecto.

### Opción 2 · Por partes

**Backend** — API en `http://localhost:8080`, consola H2 en `http://localhost:8080/h2-console` (`jdbc:h2:mem:edudb`, usuario `sa`, sin contraseña):

```bash
cd backend
mvn spring-boot:run
```

**Frontend** — app en `http://localhost:5173`:

```bash
cd frontend
npm install
npm run dev
```

**Analítica** — gráficas y CSV en `data-analysis/reports/`:

```bash
cd data-analysis
python run_all_analysis.py
```

## 👥 Roles de demostración

Desde el avatar (arriba a la derecha) cambia de rol sin login:

| Rol | Ve |
|---|---|
| Estudiante | Dashboard personal, Lab Digital, Hoja de vida, Académico, Asistencia, Bienestar + búho guía |
| Docente | Dashboard de cursos, Académico, Asistencia, Bienestar, Registro + capibara profesor |
| Familiar | Portal de seguimiento + Bienestar |
| Bienestar | Gestión de casos, triage con SLA, Asistencia |
| Admin | Todo + vista ejecutiva SaaS |

## 🤖 Mascotas guía

- Sprite sheets pixel-art de 6 cuadros (caminar + volar) en `frontend/public/`.
- Nube de diálogo vintage con máquina de escribir, decisiones por tipo con color e icono propios, sistema de XP y voz (`♪ Hablar`).
- Se pueden minimizar, desactivar por rol y reactivar cuando quieras.

## 📊 Analítica

Correlación asistencia/nota, histogramas de acceso familiar, distribución por rol y reportes exportables — ver `data-analysis/reports/` y `ROBOT_AI_PAPER*.md` para el diseño del asistente.

## 🛣️ Roadmap

- [ ] Conectar el asistente a un endpoint real de IA (Gemini/OpenAI vía Spring Boot)
- [ ] Quiz adaptativo en Lab Digital y detección de similitud en entregas
- [ ] Digest semanal por correo para acudientes
- [ ] SSO (Google Workspace / Microsoft Entra ID) y migración a PostgreSQL
