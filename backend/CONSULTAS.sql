-- =====================================================================
-- EDU.CORE / ANGEEK BOX - Consultas SQL reales de gestion
-- Base de datos: H2 en archivo (backend/data/edudb.mv.db)
-- Como ejecutar: http://localhost:8080/h2-console (ver ADMINISTRAR-BD.md)
-- =====================================================================


-- 1. VER TODOS LOS USUARIOS CON SU ROL
SELECT id, nombre, correo, rol, estado, fecha_registro
FROM usuarios
ORDER BY rol, nombre;


-- 2. LISTAR ESTUDIANTES CON SU FICHA (programa, semestre, codigo)
SELECT e.id AS estudiante_id, e.codigo_estudiante, u.nombre, u.correo,
       e.programa, e.semestre, e.genero
FROM estudiantes e
JOIN usuarios u ON u.id = e.usuario_id
ORDER BY e.id;


-- 3. BOLETIN DE CALIFICACIONES DE UN ESTUDIANTE (el mas importante)
--    Cambia el 1 por el id del estudiante que quieras (1-6)
SELECT m.nombre AS materia, g.nombre AS grupo,
       n.tipo_evaluacion AS evaluacion, n.valor AS nota,
       n.fecha, n.comentario
FROM notas n
JOIN materias m ON m.id = n.materia_id
JOIN grupos g ON g.id = n.grupo_id
WHERE n.estudiante_id = 1
ORDER BY n.fecha DESC;


-- 4. PROMEDIO POR MATERIA DE UN ESTUDIANTE
SELECT m.nombre AS materia,
       ROUND(AVG(n.valor), 2) AS promedio,
       COUNT(n.id) AS notas,
       CASE WHEN AVG(n.valor) >= 3.0 THEN 'APROBADO' ELSE 'REPROBADO' END AS estado
FROM notas n
JOIN materias m ON m.id = n.materia_id
WHERE n.estudiante_id = 1
GROUP BY m.nombre
ORDER BY promedio DESC;


-- 5. ESTUDIANTES EN RIESGO (promedio menor a 3.0) - ALERTA TEMPRANA
SELECT u.nombre, u.correo, e.codigo_estudiante, e.programa,
       ROUND(AVG(n.valor), 2) AS promedio_general
FROM notas n
JOIN estudiantes e ON e.id = n.estudiante_id
JOIN usuarios u ON u.id = e.usuario_id
GROUP BY u.nombre, u.correo, e.codigo_estudiante, e.programa
HAVING AVG(n.valor) < 3.0
ORDER BY promedio_general ASC;


-- 6. PORCENTAJE DE ASISTENCIA POR ESTUDIANTE
SELECT u.nombre, e.codigo_estudiante,
       COUNT(a.id) AS sesiones,
       SUM(CASE WHEN a.estado IN ('PRESENTE','JUSTIFICADO') THEN 1 ELSE 0 END) AS asistidas,
       ROUND(100.0 * SUM(CASE WHEN a.estado IN ('PRESENTE','JUSTIFICADO') THEN 1 ELSE 0 END) / COUNT(a.id), 1) AS pct_asistencia
FROM asistencias a
JOIN estudiantes e ON e.id = a.estudiante_id
JOIN usuarios u ON u.id = e.usuario_id
GROUP BY u.nombre, e.codigo_estudiante
ORDER BY pct_asistencia ASC;


-- 7. RENDIMIENTO POR MATERIA (para el docente)
SELECT m.nombre AS materia, m.codigo,
       COUNT(DISTINCT n.estudiante_id) AS estudiantes,
       ROUND(AVG(n.valor), 2) AS promedio,
       MIN(n.valor) AS nota_menor,
       MAX(n.valor) AS nota_mayor,
       ROUND(100.0 * SUM(CASE WHEN n.valor < 3.0 THEN 1 ELSE 0 END) / COUNT(n.id), 1) AS pct_reprobacion
FROM notas n
JOIN materias m ON m.id = n.materia_id
GROUP BY m.nombre, m.codigo
ORDER BY promedio DESC;


-- 8. QUIENES ESTAN MATRICULADOS EN CADA MATERIA
--    H2 no tiene GROUP_CONCAT: se listan las matriculas fila por fila
SELECT m.nombre AS materia, m.codigo AS codigo_materia,
       u.nombre AS estudiante, e.codigo_estudiante
FROM matriculas mt
JOIN materias m ON m.id = mt.materia_id
JOIN estudiantes e ON e.id = mt.estudiante_id
JOIN usuarios u ON u.id = e.usuario_id
ORDER BY m.nombre, u.nombre;


-- 9. CASOS DE BIENESTAR ACTIVOS
SELECT u.nombre AS estudiante, na.tipo, na.prioridad, na.estado,
       na.descripcion, na.fecha
FROM necesidades_apoyo na
JOIN estudiantes e ON e.id = na.estudiante_id
JOIN usuarios u ON u.id = e.usuario_id
WHERE na.estado <> 'CERRADA'
ORDER BY
  CASE na.prioridad WHEN 'ALTA' THEN 1 WHEN 'MEDIA' THEN 2 ELSE 3 END,
  na.fecha DESC;


-- 10. FAMILIAR VINCULADO A CADA ESTUDIANTE
SELECT f_usuario.nombre AS familiar, f.parentesco, f.telefono,
       e_usuario.nombre AS estudiante, e.codigo_estudiante
FROM vinculos_familiar_estudiante vf
JOIN familiares f ON f.id = vf.familiar_id
JOIN usuarios f_usuario ON f_usuario.id = f.usuario_id
JOIN estudiantes e ON e.id = vf.estudiante_id
JOIN usuarios e_usuario ON e_usuario.id = e.usuario_id
WHERE vf.autorizado = TRUE;


-- 11. CATALOGO DE CURSOS NUEVOS (modulo Cursos/Evaluaciones)
SELECT id, codigo, titulo, categoria, nivel, horas, activo
FROM cursos
ORDER BY id;


-- 12. ACTIVIDADES POR CURSO
SELECT c.titulo AS curso, a.titulo AS actividad, a.tipo, a.orden, a.puntos
FROM actividades a
JOIN cursos c ON c.id = a.curso_id
ORDER BY c.titulo, a.orden;


-- 13. RECURSOS DE BIBLIOTECA
SELECT id, titulo, autor, tipo, categoria, url_archivo
FROM recursos_biblioteca
ORDER BY tipo, titulo;


-- 14. PROGRESO REGISTRADO (modulo Docente)
SELECT u.nombre AS estudiante, c.titulo AS curso,
       pc.porcentaje, pc.estado, pc.fecha_actualizacion
FROM progreso_cursos pc
JOIN cursos c ON c.id = pc.curso_id
JOIN usuarios u ON u.id = pc.estudiante_id
ORDER BY u.nombre, c.titulo;


-- 15. RESUMEN EJECUTIVO (una fila con todo)
SELECT
  (SELECT COUNT(*) FROM usuarios WHERE rol='ESTUDIANTE') AS total_estudiantes,
  (SELECT COUNT(*) FROM usuarios WHERE rol='DOCENTE') AS total_docentes,
  (SELECT COUNT(*) FROM cursos WHERE activo=TRUE) AS cursos_activos,
  (SELECT COUNT(*) FROM notas) AS notas_registradas,
  (SELECT COUNT(*) FROM asistencias) AS asistencias,
  (SELECT COUNT(*) FROM necesidades_apoyo WHERE estado <> 'CERRADA') AS casos_abiertos,
  (SELECT ROUND(AVG(valor),2) FROM notas) AS promedio_general;