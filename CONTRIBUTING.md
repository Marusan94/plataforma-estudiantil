# Contributing to EDU.CORE

¡Gracias por tu interés en contribuir! Este documento te guiará para que tu contribución sea fluida y bienvenida.

---

## 🚀 Primeros Pasos

### 1. Fork & Clone
```bash
# Fork en GitHub, luego clona tu fork
git clone https://github.com/TU_USUARIO/plataforma-estudiantil.git
cd plataforma-estudiantil

# Agrega upstream para mantenerte actualizado
git remote add upstream https://github.com/Marusan94/plataforma-estudiantil.git
```

### 2. Configura el entorno
```bash
# Backend
cd backend
mvn spring-boot:run

# Frontend (nueva terminal)
cd frontend
npm install
npm run dev

# Data Analysis (opcional)
cd data-analysis
python run_all_analysis.py
```

### 3. Crea una rama
```bash
git checkout -b feat/tu-nueva-funcionalidad
# o
git checkout -b fix/descripcion-del-bug
# o
git checkout -b docs/actualizacion-readme
```

---

## 📝 Convenciones de Commits

Usamos **Conventional Commits** para historial limpio y changelogs automáticos.

### Formato
```
<tipo>(<scope>): <descripción corta>

[cuerpo opcional]

[pie opcional: Fixes #123, Closes #456]
```

### Tipos
| Tipo | Descripción |
|------|-------------|
| `feat` | Nueva funcionalidad |
| `fix` | Corrección de bug |
| `docs` | Cambios en documentación |
| `style` | Formato, punto y coma, etc. (sin cambios funcionales) |
| `refactor` | Refactorización (sin cambios funcionales) |
| `perf` | Mejora de rendimiento |
| `test` | Agregar/actualizar tests |
| `chore` | Tareas de mantenimiento, build, deps |
| `ci` | Cambios en CI/CD |
| `build` | Cambios en sistema de build |
| `revert` | Revertir commit anterior |

### Ejemplos
```bash
feat(lab-digital): add collaborative editing to sandbox
fix(asistencia): student attendance filter for ESTUDIANTE role
docs(readme): add deployment guide for Render
refactor(hoja-vida): extract CV generator to separate service
test(backend): add integration tests for EstudianteController
chore(deps): update spring-boot to 3.3.5
```

---

## 🧪 Testing

### Backend
```bash
cd backend
mvn test                    # Unit tests
mvn verify                  # Integration tests + verify
mvn spotless:check          # Code style
```

### Frontend
```bash
cd frontend
npm test                    # Unit tests (Vitest/Jest)
npm run lint                # ESLint
npm run typecheck           # TypeScript check
npm run build               # Production build
```

### Data Analysis
```bash
cd data-analysis
python -m py_compile scripts/*.py run_all_analysis.py
python run_all_analysis.py  # Full run
```

### Pre-commit (recomendado)
```bash
# Instala husky + lint-staged
npm install -g husky lint-staged
npx husky install
npx husky add .husky/pre-commit "npx lint-staged"
```

---

## 📋 Pull Request Checklist

Antes de abrir PR, verifica:

- [ ] **Branch actualizado** con `main` (`git fetch upstream && git rebase upstream/main`)
- [ ] **Tests pasan** localmente (`mvn test`, `npm test`)
- [ ] **Lint pasa** (`npm run lint`, `mvn spotless:check`)
- [ ] **Typecheck pasa** (`npm run typecheck`)
- [ ] **Build pasa** (`npm run build`, `mvn package`)
- [ ] **Commits siguen convención** (Conventional Commits)
- [ ] **PR template completado** (descripción, testing, screenshots si UI)
- [ ] **Issues vinculados** (`Fixes #123`, `Closes #456`)
- [ ] **Documentación actualizada** si aplica (README, DESIGN.md, comentarios código)

### Template PR
Usa la plantilla en `.github/PULL_REQUEST_TEMPLATE.md` — se carga automáticamente.

---

## 🏗️ Arquitectura & Patrones

### Backend (Spring Boot)
- **Arquitectura por capas**: Controller → Service → Repository → Model
- **DTOs** para request/response (nunca exponer entidades JPA)
- **Validación**: Bean Validation (`@Valid`, `@NotNull`, `@Size`, etc.)
- **Excepciones**: `GlobalExceptionHandler` + custom exceptions
- **Transacciones**: `@Transactional` en services
- **Paginación**: `Pageable` + `Page<T>` en repositories

### Frontend (React + Vite)
- **Componentes funcionales** + hooks
- **CSS Variables** design system (ver `index.css`)
- **API service** centralizado (`services/api.js`) con fallback local
- **Roles**: `currentRole` prop + `roleTabs` config en Navbar
- **Estado**: `useState`/`useEffect` local, Context para globales
- **Tema**: `data-theme` attribute + CSS vars (`dark`/`light`/`lovecraft`)

### Data Analysis (Python)
- Scripts modulares por dominio (`analisis_registro.py`, etc.)
- Pandas + NumPy + SciPy + Matplotlib/Seaborn
- Output: PNG (gráficas) + CSV (datos) en `reports/`
- Orquestador: `run_all_analysis.py`

---

## 🎨 Design System

### Colores (CSS Variables)
```css
:root {
  --accent: #2563eb;        /* Primary blue */
  --success: #16a34a;       /* Green */
  --warning: #d97706;       /* Amber */
  --danger: #dc2626;        /* Red */
  --lovecraft: #00ff88;     /* Cthulhu green */
  --eldritch: #b866ff;      /* Eldritch purple */
  --miskatonic: #c9a84c;    /* Miskatonic gold */
  --arkham: #cc3333;        /* Arkham red */
}
```

### Tema Lovecraft
Activado con `data-theme="lovecraft"` en `<html>`:
- Colores cósmicos, glitch text, scanlines CRT, vignette
- Partículas estelares + nebulosas animadas
- Botones eldritch, runas, medidor de cordura

### Iconos
- Emojis nativos (accesibles, sin dependencias)
- SVGs inline para logo/branding

---

## 🐛 Reportar Bugs

Usa la plantilla **Bug Report** en GitHub Issues (`.github/ISSUE_TEMPLATE/bug_report.yml`).

Incluye:
1. Componente afectado
2. Pasos para reproducir
3. Comportamiento esperado vs actual
4. Logs/stack traces
5. Navegador/entorno

---

## ✨ Solicitar Features

Usa la plantilla **Feature Request** (`.github/ISSUE_TEMPLATE/feature_request.yml`).

Incluye:
1. Problema que resuelve
2. Solución propuesta
3. Alternativas consideradas
4. Prioridad
5. Mockups/referencias si UI

---

## 🔒 Seguridad

- **NO** commitees secrets, tokens, passwords
- Usa variables de entorno (`.env` local, Render env vars en prod)
- Reporta vulnerabilidades vía **Security Advisories** (pestaña Security)
- Ver `SECURITY.md` para política completa

---

## 📚 Recursos

- [Conventional Commits](https://www.conventionalcommits.org/)
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [React Docs](https://react.dev/)
- [Vite Guide](https://vitejs.dev/guide/)
- [Tailwind CSS](https://tailwindcss.com/docs)
- [GitHub Actions](https://docs.github.com/en/actions)

---

## 💬 Comunidad

- **Issues**: Bugs, features, preguntas
- **Discussions**: Ideas, ayuda, show-and-tell
- **Discord/Slack**: (por configurar)

---

## 📄 Licencia

Al contribuir, aceptas que tu código se licencie bajo la misma licencia del proyecto (**Uso Institucional**).

---

**¿Dudas?** Abre un issue con label `question` o pregunta en Discussions.

¡Gracias por contribuir a EDU.CORE! 🎓