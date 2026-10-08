# 🗄️ Administrar la Base de Datos — EDU.CORE / Angeek Box

Tienes **3 formas** de trabajar con la base de datos. De la más fácil a la más pro.

---

## 📍 ¿Dónde vive la base de datos?

```
C:\Users\USUARIO\plataforma-angeek\backend\data\edudb.mv.db
```

Es un archivo (0.16 MB). Todo vive ahí: estudiantes, notas, cursos, asistencia, etc.

**Para respaldarla (backup)**: copia ese archivo a otra carpeta. Eso es TODO el respaldo.
**Para restaurarla**: pega el archivo de vuelta y reinicia el servidor.

---

## 🟢 FORMA 1: Consola Web H2 (la más fácil — sin instalar nada)

### ⚠️ IMPORTANTE: la URL debe ser EXACTA

La consola H2 viene con `jdbc:h2:~/test` por defecto que **no sirve**.
Debes cambiarla por esta (con `AUTO_SERVER=TRUE` al final):

```
jdbc:h2:file:C:/Users/USUARIO/plataforma-angeek/backend/data/edudb;AUTO_SERVER=TRUE
```

> Si te sale el error `Database "..." not found`, es porque la URL está mal escrita o
> le falta el `;AUTO_SERVER=TRUE`.

### Pasos

1. Abre el servidor: doble clic en `iniciar_angeek.bat`
2. En el navegador ve a: **http://localhost:8080/h2-console**
3. Llena estos datos **exactamente así**:

| Campo | Valor (copiar tal cual) |
|---|---|
| Driver Class | `org.h2.Driver` |
| JDBC URL | `jdbc:h2:file:C:/Users/USUARIO/plataforma-angeek/backend/data/edudb;AUTO_SERVER=TRUE` |
| User Name | `sa` |
| Password | *(déjalo vacío)* |

4. Click **Connect**. Se abre el editor SQL.
5. Escribe consultas abajo y click **Run** (o Ctrl+Enter).

### Consultas para empezar a probar

```sql
-- Ver todos los estudiantes con su promedio (¡copia y pega!)
SELECT u.nombre, e.codigo_estudiante, e.programa, e.semestre,
       ROUND(AVG(n.valor), 2) AS promedio
FROM estudiantes e
JOIN usuarios u ON u.id = e.usuario_id
LEFT JOIN notas n ON n.estudiante_id = e.id
GROUP BY u.nombre, e.codigo_estudiante, e.programa, e.semestre
ORDER BY promedio DESC;
```

```sql
-- Ranking rápido deRisko: promedio menor a 3.0
SELECT u.nombre, ROUND(AVG(n.valor), 2) AS promedio
FROM notas n
JOIN estudiantes e ON e.id = n.estudiante_id
JOIN usuarios u ON u.id = e.usuario_id
GROUP BY u.nombre
HAVING AVG(n.valor) < 3.0;
```

### 💾 Atajo: guarda la configuración para no repetirla

Una vez conectado, arriba aparece **Saved Settings**. Escribe un nombre
(por ejemplo `AngeekBox`) en **Setting Name** y dale **Save**. Las próximas
veces solo seleccionas esa opción y das Connect.

---

## 🔵 FORMA 2: Script SQL directo (rápido, sin navegador)

Creé **`backend/consulta-sql.bat`**. Lo ejecutas con doble clic o desde la terminal:

```powershell
# Consulta rápida
cd C:\Users\USUARIO\plataforma-angeek\backend
.\consulta-sql.bat "SELECT COUNT(*) FROM notas"

# Ejemplo real: ranking de estudiantes por promedio
.\consulta-sql.bat "SELECT u.nombre, ROUND(AVG(n.valor),2) AS prom FROM notas n JOIN estudiantes e ON e.id=n.estudiante_id JOIN usuarios u ON u.id=e.usuario_id GROUP BY u.nombre ORDER BY prom DESC"

# Cargar un archivo .sql completo
.\consulta-sql.bat -f CONSULTAS.sql
```

Salida real de ejemplo:
```
NOMBRE           | PROM
Valeria Ramirez  | 4.73
Santiago Perez   | 4.5
Camila Torres    | 4
Juan Diego Marin | 3.77
Mateo Morales    | 2.6
Daniela Ortiz    | 2.5
(6 rows, 15 ms)
```

**Funciona aunque la aplicación esté corriendo** (por `AUTO_SERVER=TRUE`).

---

## 🟣 FORMA 3: API REST (lo que usa la plataforma de verdad)

Creé **`backend/CONSULTAS.sql`** con **15 consultas reales** de gestión:

| # | Consulta | Para qué sirve |
|---|----------|----------------|
| 1 | Listar usuarios por rol | Ver quién está registrado |
| 2 | Estudiantes con ficha | Ver programa, semestre, código |
| 3 | **Boletín de calificaciones** | El reporte que ve el docente |
| 4 | Promedio por materia | Ver dónde rinden bien/mal |
| 5 | **Estudiantes en riesgo** | Alerta temprana (promedio < 3) |
| 6 | % asistencia por estudiante | Detectar ausentismo |
| 7 | Rendimiento por materia | Vista del docente |
| 8 | Quién está en cada materia | Matrículas |
| 9 | Casos de bienestar activos | Seguimiento psicosocial |
| 10 | Familiar vinculado a cada estudiante | Portal familiar |
| 11 | Catálogo de cursos | Módulo Cursos |
| 12 | Actividades por curso | Módulo Actividades |
| 13 | Recursos de biblioteca | Módulo Biblioteca |
| 14 | Progreso de estudiantes | Módulo Docente |
| 15 | **Resumen ejecutivo** | Los KPIs del admin |

**Cómo usarlo:**
- Abre `CONSULTAS.sql` en un editor de texto
- Copia la consulta que quieras
- Pégala en la consola H2 y dale Run
- Cambia los `WHERE id = 1` por el estudiante que necesites

---

## 🟣 FORMA 3: API REST (lo que usa la plataforma de verdad)

La plataforma NO lee la base de datos directo — usa la API. Puedes hacer lo mismo desde Postman o PowerShell:

### Ejemplos

```powershell
# Ver todos los estudiantes
Invoke-RestMethod http://localhost:8080/estudiantes

# Ver notas de un estudiante (id 2 = Valeria)
Invoke-RestMethod http://localhost:8080/notas/estudiante/2

# Crear un usuario nuevo
Invoke-RestMethod -Uri http://localhost:8080/usuarios -Method POST -ContentType "application/json" -Body '{"nombre":"Ana Torres","correo":"ana@esc.edu","contrasena":"clave123","rol":"ESTUDIANTE"}'

# Crear un curso
Invoke-RestMethod -Uri http://localhost:8080/api/cursos -Method POST -ContentType "application/json" -Body '{"titulo":"Python Basico","codigo":"PY-100"}'

# Registrar progreso
Invoke-RestMethod -Uri http://localhost:8080/api/progreso -Method PUT -ContentType "application/json" -Body '{"estudianteId":2,"cursoId":1,"porcentaje":75,"estado":"EN_CURSO"}'
```

---

## 🧪 FORMA 4: Verificar consultas automáticamente

Si cambiaste `CONSULTAS.sql` y quieres comprobar que todas funcionan:

```powershell
cd C:\Users\USUARIO\plataforma-angeek\backend
.\consulta-sql.bat -f CONSULTAS.sql
```

O compila y corre el verificador (revisa que no haya errores de sintaxis en las 15):

```powershell
javac -cp C:\Users\USUARIO\.m2\repository\com\h2database\h2\2.2.224\h2-2.2.224.jar TestConsultas.java
java -cp "C:\Users\USUARIO\.m2\repository\com\h2database\h2\2.2.224\h2-2.2.224.jar;." TestConsultas
```

---

## 🧑‍🏫 "Lo que se haría en la vida real" — Guía por rol

> **Nota importante**: las consultas 11-14 (cursos, biblioteca, progreso) salen en 0 filas
> porque esos módulos son **gestores vacíos**: los llenas tú desde la interfaz web
> (pestañas Cursos, Biblioteca, Docente). Los datos del sistema académico legacy
> (usuarios, notas, asistencias, materias) sí vienen sembrados.

### 📋 Si eres RECTOR / ADMIN
- **Ver KPIs generales** → Consulta 15 (Resumen Ejecutivo)
- **Ver quién está en riesgo** → Consulta 5, y activa alertas
- **Exportar datos para Excel** → En la consola H2, click en el resultado y copia, o usa la API
- **Respaldar todo** → Copia `edudb.mv.db` a una USB cada semana

### 👨‍🏫 Si eres DOCENTE
- **Ver notas de tu grupo** → Consulta 3 (cambia el id del estudiante)
- **Ver rendimiento de tu materia** → Consulta 7
- **Pasar lista** → La plataforma ya lo hace en el módulo Asistencia
- **Detectar quién va mal** → Consulta 5

### 👩‍🎓 Si eres ESTUDIANTE
- **Ver tu boletín** → Consulta 3 con TU id de estudiante
- **Ver tu progreso** → Consulta 14 con TU id
- **Entender tu asistencia** → Consulta 6

### 👨‍👩‍👧 Si eres PADRE
- **Ver a tu hijo** → Consulta 10 para saber quién está vinculado
- **Ver su desempeño** → Consulta 3 con el id de tu hijo

### 🧠 Si eres BIENESTAR
- **Ver casos activos** → Consulta 9
- **Ver quién necesita apoyo** → Cruza consulta 9 con consulta 5

---

## ⚠️ Reglas importantes

1. **NO borres datos a mano** desde la consola si no sabes qué estás borrando
2. **SIEMPRE haz backup** antes de experimenting
3. La contraseña de los estudiantes es `est123`, docentes `doc123`, admin `admin123`
4. Para **resetear todo** y volver al seed original: borra `edudb.mv.db`, reinicia el servidor

---

## 🆘 Si algo sale mal

| Problema | Solución |
|----------|----------|
| `Database "..." not found` | La URL está mal. Debe ser la ABSOLUTA y terminar en `;AUTO_SERVER=TRUE` |
| `Table "NOTAS" not found` | Se conectó a una base **vacía** (URL mal escrita). Verifica que apunte a `.../plataforma-angeek/backend/data/edudb` |
| `Database may be already in use` | Another tool opened it without `AUTO_SERVER=TRUE`. Agrega ese parámetro a la URL |
| La consola H2 no conecta | Verifica la URL exacta de arriba;Driver: `org.h2.Driver`, User: `sa`, Password vacío |
| "No hay resultados" | Tu filtro WHERE está muy específico; quítalo y prueba |
| Quiero empezar de cero | Borra `backend/data/edudb.mv.db` y reinicia el servidor |
| La API no responde | ¿El servidor está corriendo? Debe estar en :8080 |
| Cambié algo y no se ve | Reinicia el servidor (Ctrl+C y vuelve a `iniciar_angeek.bat`) |

---

## 🔌 Conectar desde DBeaver, DataGrip o PostgreSQL tools

Como la base usa `AUTO_SERVER=TRUE`, puedes abrirla con **cualquier cliente JDBC**:

| Campo | Valor |
|---|---|
| Driver | H2 (o el driver H2 que te brinde la herramienta) |
| JDBC URL | `jdbc:h2:file:C:/Users/USUARIO/plataforma-angeek/backend/data/edudb;AUTO_SERVER=TRUE` |
| User | `sa` |
| Password | *(vacío)* |

Esto te permite abrir la base desde herramientas de administración graphical
(DBeaver, Navicat, HeidiSQL) para hacer consultas, ver el diagrama de tablas
y exportar datos.

---

**Resumen en 30 segundos:**
1. Abre `iniciar_angeek.bat`
2. Ve a http://localhost:8080/h2-console
3. JDBC URL → `jdbc:h2:file:C:/Users/USUARIO/plataforma-angeek/backend/data/edudb;AUTO_SERVER=TRUE`
4. Driver: `org.h2.Driver` | User: `sa` | Password: vacío
5. Click Connect, y pega consultas de `CONSULTAS.sql` y dale Run
6. Para respaldar: copia `backend/data/edudb.mv.db`

¿Necesitas que agregue más consultas específicas para tu caso?