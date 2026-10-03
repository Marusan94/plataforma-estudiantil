package com.edu.plataforma.config;

import com.edu.plataforma.model.*;
import com.edu.plataforma.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final EstudianteRepository estudianteRepository;
    private final DocenteRepository docenteRepository;
    private final FamiliarRepository familiarRepository;
    private final PerfilEstudianteRepository perfilRepository;
    private final HabilidadRepository habilidadRepository;
    private final ProyectoRepository proyectoRepository;
    private final CertificadoRepository certificadoRepository;
    private final MateriaRepository materiaRepository;
    private final GrupoRepository grupoRepository;
    private final MatriculaRepository matriculaRepository;
    private final NotaRepository notaRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final CategoriaNecesidadRepository categoriaRepository;
    private final TipoApoyoRepository tipoApoyoRepository;
    private final ProfesionalRepository profesionalRepository;
    private final NecesidadApoyoRepository necesidadRepository;
    private final SeguimientoRepository seguimientoRepository;
    private final VinculoFamiliarRepository vinculoRepository;
    private final AccesoFamiliarRepository accesoFamiliarRepository;

    public DataLoader(UsuarioRepository usuarioRepository,
                      EstudianteRepository estudianteRepository,
                      DocenteRepository docenteRepository,
                      FamiliarRepository familiarRepository,
                      PerfilEstudianteRepository perfilRepository,
                      HabilidadRepository habilidadRepository,
                      ProyectoRepository proyectoRepository,
                      CertificadoRepository certificadoRepository,
                      MateriaRepository materiaRepository,
                      GrupoRepository grupoRepository,
                      MatriculaRepository matriculaRepository,
                      NotaRepository notaRepository,
                      AsistenciaRepository asistenciaRepository,
                      CategoriaNecesidadRepository categoriaRepository,
                      TipoApoyoRepository tipoApoyoRepository,
                      ProfesionalRepository profesionalRepository,
                      NecesidadApoyoRepository necesidadRepository,
                      SeguimientoRepository seguimientoRepository,
                      VinculoFamiliarRepository vinculoRepository,
                      AccesoFamiliarRepository accesoFamiliarRepository) {
        this.usuarioRepository = usuarioRepository;
        this.estudianteRepository = estudianteRepository;
        this.docenteRepository = docenteRepository;
        this.familiarRepository = familiarRepository;
        this.perfilRepository = perfilRepository;
        this.habilidadRepository = habilidadRepository;
        this.proyectoRepository = proyectoRepository;
        this.certificadoRepository = certificadoRepository;
        this.materiaRepository = materiaRepository;
        this.grupoRepository = grupoRepository;
        this.matriculaRepository = matriculaRepository;
        this.notaRepository = notaRepository;
        this.asistenciaRepository = asistenciaRepository;
        this.categoriaRepository = categoriaRepository;
        this.tipoApoyoRepository = tipoApoyoRepository;
        this.profesionalRepository = profesionalRepository;
        this.necesidadRepository = necesidadRepository;
        this.seguimientoRepository = seguimientoRepository;
        this.vinculoRepository = vinculoRepository;
        this.accesoFamiliarRepository = accesoFamiliarRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() > 0) return;

        // 1. Administrador
        Usuario adminUser = new Usuario(null, "Admin Sistema", "admin@instituto.edu.co", "admin123", "ADMIN", "ACTIVO");
        usuarioRepository.save(adminUser);

        // 2. Docentes
        Usuario uDoc1 = usuarioRepository.save(new Usuario(null, "Prof. Carlos Mendoza", "carlos.mendoza@instituto.edu.co", "doc123", "DOCENTE", "ACTIVO"));
        Docente doc1 = new Docente();
        doc1.setUsuario(uDoc1);
        doc1.setEspecialidad("Ingenieria de Software y Arquitectura");
        doc1.setNivelAcademico("Magister");
        doc1.setDepartamento("Tecnologia");
        docenteRepository.save(doc1);

        Usuario uDoc2 = usuarioRepository.save(new Usuario(null, "Prof. Elena Gomez", "elena.gomez@instituto.edu.co", "doc123", "DOCENTE", "ACTIVO"));
        Docente doc2 = new Docente();
        doc2.setUsuario(uDoc2);
        doc2.setEspecialidad("Bases de Datos y Analitica");
        doc2.setNivelAcademico("Especialista");
        doc2.setDepartamento("Tecnologia");
        docenteRepository.save(doc2);

        // 3. Materias y Grupos
        Materia mWeb = materiaRepository.save(new Materia("Desarrollo Web Full Stack", "PROG-301", doc1));
        Materia mBD = materiaRepository.save(new Materia("Bases de Datos Relacionales", "BD-201", doc2));
        Materia mAlgo = materiaRepository.save(new Materia("Algoritmos y Estructuras", "ALGO-101", doc1));

        Grupo gWeb1 = grupoRepository.save(new Grupo("G-WEB-01", mWeb, 3));
        Grupo gBD1 = grupoRepository.save(new Grupo("G-BD-01", mBD, 2));
        Grupo gAlgo1 = grupoRepository.save(new Grupo("G-ALGO-01", mAlgo, 1));

        // 4. Bienestar: Categorias, Tipos y Profesional
        CategoriaNecesidad catEmocional = categoriaRepository.save(new CategoriaNecesidad("Emocional"));
        CategoriaNecesidad catAcademica = categoriaRepository.save(new CategoriaNecesidad("Academica"));
        CategoriaNecesidad catEconomica = categoriaRepository.save(new CategoriaNecesidad("Economica"));

        tipoApoyoRepository.save(new TipoApoyo("Acompanamiento Psicologico", "Sesiones de orientacion y salud mental"));
        tipoApoyoRepository.save(new TipoApoyo("Tutoria Academica", "Refuerzo personalizado en materias con dificultad"));
        tipoApoyoRepository.save(new TipoApoyo("Subsidio de Transporte/Alimentacion", "Apoyo para sostenimiento"));

        Profesional psicologa = profesionalRepository.save(new Profesional("Dra. Mariana Restrepo", "Psicologia y Orientacion", "Psicologa de Bienestar", "mariana.restrepo@instituto.edu.co"));
        Profesional orientador = profesionalRepository.save(new Profesional("Lic. Andres Castano", "Acompanamiento Estudiantil", "Coordinador de Apoyo", "andres.castano@instituto.edu.co"));

        // 5. Estudiantes y Perfiles
        String[][] estudiantesData = {
            {"Santiago Perez", "santiago.perez@estudiante.edu.co", "Desarrollo de Software", "3", "Masculino"},
            {"Valeria Ramirez", "valeria.ramirez@estudiante.edu.co", "Desarrollo de Software", "3", "Femenino"},
            {"Mateo Morales", "mateo.morales@estudiante.edu.co", "Desarrollo de Software", "2", "Masculino"},
            {"Camila Torres", "camila.torres@estudiante.edu.co", "Diseno Grafico Digital", "2", "Femenino"},
            {"Daniela Ortiz", "daniela.ortiz@estudiante.edu.co", "Desarrollo de Software", "1", "Femenino"},
            {"Juan Diego Marin", "juan.marin@estudiante.edu.co", "Redes y Telecomunicaciones", "3", "Masculino"}
        };

        List<Estudiante> estudiantesCreados = new ArrayList<>();
        for (int i = 0; i < estudiantesData.length; i++) {
            String[] data = estudiantesData[i];
            Usuario u = usuarioRepository.save(new Usuario(null, data[0], data[1], "est123", "ESTUDIANTE", "ACTIVO"));
            Estudiante e = new Estudiante();
            e.setUsuario(u);
            e.setPrograma(data[2]);
            e.setSemestre(Integer.parseInt(data[3]));
            e.setGenero(data[4]);
            e.setCodigoEstudiante("EST-2026-" + String.format("%03d", i + 1));
            e.setFechaNacimiento(LocalDate.of(2003 + (i % 3), 1 + (i * 2 % 12), 10 + i));
            estudiantesCreados.add(estudianteRepository.save(e));
        }

        // Perfil para Santiago Perez (Estudiante 0)
        Estudiante santiago = estudiantesCreados.get(0);
        PerfilEstudiante perfilSantiago = new PerfilEstudiante();
        perfilSantiago.setEstudiante(santiago);
        perfilSantiago.setResumen("Estudiante apasionado por el desarrollo fullstack con Spring Boot y React. Enfocado en soluciones limpias y escalables.");
        perfilSantiago.setIntereses("Arquitectura de software, APIs REST, React, Cloud Computing, Inteligencia Artificial");
        perfilSantiago.setExperiencia("Monitor academico de Algoritmos durante 2025. Desarrollador Junior freelance en proyectos web.");
        perfilSantiago.setFotoUrl("https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300");
        perfilRepository.save(perfilSantiago);

        habilidadRepository.save(new Habilidad("Java & Spring Boot", "Avanzado", "Tecnica", perfilSantiago));
        habilidadRepository.save(new Habilidad("React & Tailwind CSS", "Avanzado", "Tecnica", perfilSantiago));
        habilidadRepository.save(new Habilidad("Trabajo en Equipo", "Avanzado", "Blanda", perfilSantiago));
        habilidadRepository.save(new Habilidad("Resolucion de Problemas", "Avanzado", "Blanda", perfilSantiago));

        Proyecto proy1 = new Proyecto();
        proy1.setPerfil(perfilSantiago);
        proy1.setTitulo("Plataforma de E-commerce Educativo");
        proy1.setDescripcion("Sistema completo con catalogo, carrito, checkout y microservicios.");
        proy1.setUrl("https://github.com/santiagoperez/ecommerce-demo");
        proy1.setTecnologias("React, Spring Boot, PostgreSQL, Docker");
        proyectoRepository.save(proy1);

        Certificado cert1 = new Certificado();
        cert1.setPerfil(perfilSantiago);
        cert1.setNombre("Certificacion Profesional en Backend con Spring");
        cert1.setInstitucion("Oracle University / Java Foundation");
        cert1.setFecha(LocalDate.of(2025, 11, 20));
        cert1.setUrlArchivo("https://certificados.org/oracle-spring-santiago.pdf");
        certificadoRepository.save(cert1);

        // Perfil para Valeria Ramirez (Estudiante 1)
        Estudiante valeria = estudiantesCreados.get(1);
        PerfilEstudiante perfilValeria = new PerfilEstudiante();
        perfilValeria.setEstudiante(valeria);
        perfilValeria.setResumen("Futura ingeniera de software enfocada en desarrollo frontend y accesibilidad web.");
        perfilValeria.setIntereses("UI/UX, TypeScript, Accesibilidad web, React, Next.js");
        perfilValeria.setExperiencia("Desarrollo de landing pages accesibles para microempresas locales.");
        perfilValeria.setFotoUrl("https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=300");
        perfilRepository.save(perfilValeria);

        habilidadRepository.save(new Habilidad("React & Next.js", "Avanzado", "Tecnica", perfilValeria));
        habilidadRepository.save(new Habilidad("Comunicacion Asertiva", "Avanzado", "Blanda", perfilValeria));

        // 6. Familiares y Vinculos
        Usuario uFam1 = usuarioRepository.save(new Usuario(null, "Marta Morales", "marta.morales@familia.com", "fam123", "FAMILIAR", "ACTIVO"));
        Familiar fam1 = new Familiar();
        fam1.setUsuario(uFam1);
        fam1.setParentesco("Madre");
        fam1.setTelefono("3104567890");
        fam1.setDireccion("Cra 45 # 60-20, Medellin");
        familiarRepository.save(fam1);

        vinculoRepository.save(new VinculoFamiliarEstudiante(fam1, santiago, true));
        accesoFamiliarRepository.save(new AccesoFamiliar(fam1, "Inicio de sesion exitoso en la plataforma"));
        accesoFamiliarRepository.save(new AccesoFamiliar(fam1, "Consulta de boletin de calificaciones y asistencia"));

        Usuario uFam2 = usuarioRepository.save(new Usuario(null, "Jorge Ramirez", "jorge.ramirez@familia.com", "fam123", "FAMILIAR", "ACTIVO"));
        Familiar fam2 = new Familiar();
        fam2.setUsuario(uFam2);
        fam2.setParentesco("Padre");
        fam2.setTelefono("3157891234");
        fam2.setDireccion("Calle 50 # 30-15, Medellin");
        familiarRepository.save(fam2);

        vinculoRepository.save(new VinculoFamiliarEstudiante(fam2, valeria, true));
        accesoFamiliarRepository.save(new AccesoFamiliar(fam2, "Consulta de perfil academico"));

        // 7. Matriculas
        for (Estudiante e : estudiantesCreados) {
            matriculaRepository.save(new Matricula(e, mWeb, gWeb1, "2026-1"));
            matriculaRepository.save(new Matricula(e, mBD, gBD1, "2026-1"));
        }

        // 8. Calificaciones (Notas) - Algunos con bajo rendimiento para disparar alertas
        notaRepository.save(new Nota(4.8, "Parcial 1", LocalDate.of(2026, 2, 15), "Excelente desempeno en API REST", santiago, mWeb, gWeb1));
        notaRepository.save(new Nota(4.5, "Quiz 1", LocalDate.of(2026, 2, 28), "Buen dominio de componentes", santiago, mWeb, gWeb1));
        notaRepository.save(new Nota(4.2, "Taller SQL", LocalDate.of(2026, 3, 5), "Consultas optimizadas", santiago, mBD, gBD1));

        notaRepository.save(new Nota(4.9, "Parcial 1", LocalDate.of(2026, 2, 15), "Desarrollo frontend impecable", valeria, mWeb, gWeb1));
        notaRepository.save(new Nota(4.6, "Quiz 1", LocalDate.of(2026, 2, 28), "Respuestas precisas", valeria, mWeb, gWeb1));

        // Mateo Morales (bajo rendimiento simulado)
        Estudiante mateo = estudiantesCreados.get(2);
        notaRepository.save(new Nota(2.4, "Parcial 1", LocalDate.of(2026, 2, 15), "Dificultades en persistencia JPA", mateo, mWeb, gWeb1));
        notaRepository.save(new Nota(2.8, "Quiz 1", LocalDate.of(2026, 2, 28), "Conceptos de relaciones incompletos", mateo, mWeb, gWeb1));

        // Daniela Ortiz (bajo rendimiento simulado)
        Estudiante daniela = estudiantesCreados.get(4);
        notaRepository.save(new Nota(2.5, "Taller Inicial", LocalDate.of(2026, 3, 2), "Requiere asesoria en sintaxis", daniela, mAlgo, gAlgo1));

        // 9. Asistencias (ultimas 3 semanas)
        LocalDate[] fechas = {
            LocalDate.of(2026, 2, 10),
            LocalDate.of(2026, 2, 17),
            LocalDate.of(2026, 2, 24),
            LocalDate.of(2026, 3, 3)
        };

        for (LocalDate f : fechas) {
            asistenciaRepository.save(new Asistencia(f, "PRESENTE", "Puntual", false, santiago, gWeb1));
            asistenciaRepository.save(new Asistencia(f, "PRESENTE", "Puntual", false, valeria, gWeb1));
        }
        // Ausencias para Mateo (ausentismo)
        asistenciaRepository.save(new Asistencia(fechas[0], "PRESENTE", null, false, mateo, gWeb1));
        asistenciaRepository.save(new Asistencia(fechas[1], "AUSENTE", "No presento excusa", false, mateo, gWeb1));
        asistenciaRepository.save(new Asistencia(fechas[2], "AUSENTE", "Falta recurrente", false, mateo, gWeb1));
        asistenciaRepository.save(new Asistencia(fechas[3], "JUSTIFICADO", "Incapacidad medica", true, mateo, gWeb1));

        // 10. Solicitudes de Bienestar y Seguimientos
        NecesidadApoyo nec1 = new NecesidadApoyo();
        nec1.setEstudiante(mateo);
        nec1.setTipo("Academico");
        nec1.setDescripcion("El estudiante manifiesta confusion con los conceptos de programacion orientada a objetos y dificultad para concentrarse.");
        nec1.setFecha(LocalDate.of(2026, 2, 20));
        nec1.setPrioridad("ALTA");
        nec1.setEstado("EN_ATENCION");
        nec1.setCategoria(catAcademica);
        nec1.setProfesional(orientador);
        NecesidadApoyo necGuardada = necesidadRepository.save(nec1);

        seguimientoRepository.save(new Seguimiento(necGuardada, LocalDate.of(2026, 2, 22), "Se programa primera sesion de tutoria con monitor academico de apoyo.", "EN_ATENCION"));
        seguimientoRepository.save(new Seguimiento(necGuardada, LocalDate.of(2026, 3, 1), "El estudiante asistio a la monitoria y mostro avances en logica basica.", "EN_ATENCION"));

        NecesidadApoyo nec2 = new NecesidadApoyo();
        nec2.setEstudiante(daniela);
        nec2.setTipo("Psicologico");
        nec2.setDescripcion("Solicitud de acompanamiento por altos niveles de ansiedad previo a entregas de proyectos.");
        nec2.setFecha(LocalDate.of(2026, 3, 4));
        nec2.setPrioridad("MEDIA");
        nec2.setEstado("PENDIENTE");
        nec2.setCategoria(catEmocional);
        nec2.setProfesional(psicologa);
        necesidadRepository.save(nec2);

        System.out.println(">>> SEED COMPLETO: Base de datos institucional inicializada exitosamente con datos realistas.");
    }
}