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

        // 2. Docentes (3: web/redes, bd/python, algoritmos)
        Usuario uDoc1 = usuarioRepository.save(new Usuario(null, "Prof. Carlos Mendoza", "carlos.mendoza@instituto.edu.co", "doc123", "DOCENTE", "ACTIVO"));
        Docente doc1 = new Docente();
        doc1.setUsuario(uDoc1);
        doc1.setEspecialidad("Desarrollo Web y Redes de Computadores");
        doc1.setNivelAcademico("Magister");
        doc1.setDepartamento("Tecnologia");
        docenteRepository.save(doc1);

        Usuario uDoc2 = usuarioRepository.save(new Usuario(null, "Prof. Elena Gomez", "elena.gomez@instituto.edu.co", "doc123", "DOCENTE", "ACTIVO"));
        Docente doc2 = new Docente();
        doc2.setUsuario(uDoc2);
        doc2.setEspecialidad("Bases de Datos y Programacion Python");
        doc2.setNivelAcademico("Especialista");
        doc2.setDepartamento("Tecnologia");
        docenteRepository.save(doc2);

        Usuario uDoc3 = usuarioRepository.save(new Usuario(null, "Prof. Laura Vargas", "laura.vargas@instituto.edu.co", "doc123", "DOCENTE", "ACTIVO"));
        Docente doc3 = new Docente();
        doc3.setUsuario(uDoc3);
        doc3.setEspecialidad("Algoritmos y Estructuras de Datos");
        doc3.setNivelAcademico("Magister");
        doc3.setDepartamento("Tecnologia");
        docenteRepository.save(doc3);

        // 3. Materias y Grupos (6 materias, cada una con su grupo)
        Materia mWeb = materiaRepository.save(new Materia("Desarrollo Web Full Stack", "PROG-301", doc1));
        Materia mPython = materiaRepository.save(new Materia("Programacion con Python", "PY-101", doc2));
        Materia mBD = materiaRepository.save(new Materia("Base de Datos SQL", "BD-201", doc2));
        Materia mAlgo = materiaRepository.save(new Materia("Algoritmos y Estructuras", "ALGO-101", doc3));
        Materia mRedes = materiaRepository.save(new Materia("Redes de Computadores", "RED-201", doc1));
        Materia mDiseno = materiaRepository.save(new Materia("Diseno Web y UI", "WEB-201", doc1));

        Grupo gWeb = grupoRepository.save(new Grupo("G-WEB-01", mWeb, 3));
        Grupo gPython = grupoRepository.save(new Grupo("G-PY-01", mPython, 1));
        Grupo gBD = grupoRepository.save(new Grupo("G-BD-01", mBD, 2));
        Grupo gAlgo = grupoRepository.save(new Grupo("G-ALGO-01", mAlgo, 1));
        Grupo gRedes = grupoRepository.save(new Grupo("G-RED-01", mRedes, 3));
        Grupo gDiseno = grupoRepository.save(new Grupo("G-DIS-01", mDiseno, 2));

        // 4. Bienestar: Categorias, Tipos y Profesionales
        CategoriaNecesidad catEmocional = categoriaRepository.save(new CategoriaNecesidad("Emocional"));
        CategoriaNecesidad catAcademica = categoriaRepository.save(new CategoriaNecesidad("Academica"));
        CategoriaNecesidad catEconomica = categoriaRepository.save(new CategoriaNecesidad("Economica"));
        CategoriaNecesidad catVocacional = categoriaRepository.save(new CategoriaNecesidad("Vocacional"));

        tipoApoyoRepository.save(new TipoApoyo("Acompanamiento Psicologico", "Sesiones de orientacion y salud mental"));
        tipoApoyoRepository.save(new TipoApoyo("Tutoria Academica", "Refuerzo personalizado en materias con dificultad"));
        tipoApoyoRepository.save(new TipoApoyo("Subsidio de Transporte/Alimentacion", "Apoyo para sostenimiento"));
        tipoApoyoRepository.save(new TipoApoyo("Orientacion Vocacional", "Acompanamiento para definir perfil profesional"));

        Profesional psicologa = profesionalRepository.save(new Profesional("Dra. Mariana Restrepo", "Psicologia y Orientacion", "Psicologa de Bienestar", "mariana.restrepo@instituto.edu.co"));
        Profesional orientador = profesionalRepository.save(new Profesional("Lic. Andres Castano", "Acompanamiento Estudiantil", "Coordinador de Apoyo", "andres.castano@instituto.edu.co"));

        // 5. Estudiantes (mismos correos, roles y claves de siempre)
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

        Estudiante santiago = estudiantesCreados.get(0);
        Estudiante valeria = estudiantesCreados.get(1);
        Estudiante mateo = estudiantesCreados.get(2);
        Estudiante camila = estudiantesCreados.get(3);
        Estudiante daniela = estudiantesCreados.get(4);
        Estudiante juan = estudiantesCreados.get(5);

        // 6. Perfiles unicos por estudiante (foto local, nunca URL externa)
        PerfilEstudiante perfilSantiago = crearPerfil(santiago,
                "Estudiante apasionado por el desarrollo fullstack con Spring Boot y React. Enfocado en soluciones limpias y escalables.",
                "Arquitectura de software, APIs REST, React, Cloud Computing, Inteligencia Artificial",
                "Monitor academico de Algoritmos durante 2025. Desarrollador Junior freelance en proyectos web.",
                "/img/photo-santiago.jpg");
        habilidadRepository.save(new Habilidad("Java & Spring Boot", "Avanzado", "Tecnica", perfilSantiago));
        habilidadRepository.save(new Habilidad("React & Tailwind CSS", "Avanzado", "Tecnica", perfilSantiago));
        habilidadRepository.save(new Habilidad("Trabajo en Equipo", "Avanzado", "Blanda", perfilSantiago));
        habilidadRepository.save(new Habilidad("Resolucion de Problemas", "Avanzado", "Blanda", perfilSantiago));
        Proyecto proySantiago = new Proyecto();
        proySantiago.setPerfil(perfilSantiago);
        proySantiago.setTitulo("Plataforma de E-commerce Educativo");
        proySantiago.setDescripcion("Sistema completo con catalogo, carrito, checkout y microservicios.");
        proySantiago.setUrl("https://github.com/santiagoperez/ecommerce-demo");
        proySantiago.setTecnologias("React, Spring Boot, PostgreSQL, Docker");
        proyectoRepository.save(proySantiago);
        Certificado certSantiago = new Certificado();
        certSantiago.setPerfil(perfilSantiago);
        certSantiago.setNombre("Certificacion Profesional en Backend con Spring");
        certSantiago.setInstitucion("Oracle University / Java Foundation");
        certSantiago.setFecha(LocalDate.of(2025, 11, 20));
        certSantiago.setUrlArchivo("/docs/certificado-spring-santiago.pdf");
        certificadoRepository.save(certSantiago);

        PerfilEstudiante perfilValeria = crearPerfil(valeria,
                "Futura ingeniera de software enfocada en desarrollo frontend, accesibilidad web y experiencia de usuario inclusiva.",
                "UI/UX, TypeScript, Accesibilidad web, React, Next.js",
                "Desarrollo de landing pages accesibles para microempresas locales. Voluntaria en semillero de UX.",
                "/img/photo-valeria.jpg");
        habilidadRepository.save(new Habilidad("React & Next.js", "Avanzado", "Tecnica", perfilValeria));
        habilidadRepository.save(new Habilidad("Diseno UI/UX Accesible", "Avanzado", "Tecnica", perfilValeria));
        habilidadRepository.save(new Habilidad("TypeScript", "Intermedio", "Tecnica", perfilValeria));
        habilidadRepository.save(new Habilidad("Comunicacion Asertiva", "Avanzado", "Blanda", perfilValeria));
        Proyecto proyValeria1 = new Proyecto();
        proyValeria1.setPerfil(perfilValeria);
        proyValeria1.setTitulo("Red de Landing Pages Accesibles");
        proyValeria1.setDescripcion("Tres sitios para microempresas con puntaje Lighthouse 100 en accesibilidad y modo alto contraste.");
        proyValeria1.setUrl("https://github.com/valeriaramirez/landing-accesibles");
        proyValeria1.setTecnologias("Next.js, TypeScript, Tailwind CSS, axe-core");
        proyectoRepository.save(proyValeria1);
        Proyecto proyValeria2 = new Proyecto();
        proyValeria2.setPerfil(perfilValeria);
        proyValeria2.setTitulo("Biblioteca de Componentes Inclusivos");
        proyValeria2.setDescripcion("Set de 20 componentes React con ARIA, foco visible y soporte de lector de pantalla.");
        proyValeria2.setUrl("https://github.com/valeriaramirez/ui-inclusiva");
        proyValeria2.setTecnologias("React, Storybook, Jest, Testing Library");
        proyectoRepository.save(proyValeria2);
        Certificado certValeria = new Certificado();
        certValeria.setPerfil(perfilValeria);
        certValeria.setNombre("Certificado en Accesibilidad Web WCAG 2.2");
        certValeria.setInstitucion("Platzi / Escuela de Desarrollo Web");
        certValeria.setFecha(LocalDate.of(2025, 9, 12));
        certValeria.setUrlArchivo("/docs/certificado-wcag-valeria.pdf");
        certificadoRepository.save(certValeria);

        PerfilEstudiante perfilMateo = crearPerfil(mateo,
                "Estudiante de segundo semestre trabajando en reforzar sus bases de programacion orientada a objetos y bases de datos.",
                "Videojuegos, Logica de programacion, Soporte tecnico, Hardware",
                "Auxiliar de sala de sistemas del instituto durante 2025. En tutoria academica de refuerzo.",
                "/img/photo-mateo.jpg");
        habilidadRepository.save(new Habilidad("Java Basico", "Basico", "Tecnica", perfilMateo));
        habilidadRepository.save(new Habilidad("SQL Basico", "Basico", "Tecnica", perfilMateo));
        habilidadRepository.save(new Habilidad("Perseverancia", "Intermedio", "Blanda", perfilMateo));
        Proyecto proyMateo = new Proyecto();
        proyMateo.setPerfil(perfilMateo);
        proyMateo.setTitulo("CRUD de Inventario en Consola");
        proyMateo.setDescripcion("Aplicacion Java de consola para registrar productos de una tienda con archivos planos.");
        proyMateo.setUrl("https://github.com/mateomorales/inventario-consola");
        proyMateo.setTecnologias("Java, Archivos CSV");
        proyectoRepository.save(proyMateo);

        PerfilEstudiante perfilCamila = crearPerfil(camila,
                "Disenadora grafica digital en formacion, explorando la frontera entre diseno visual y desarrollo frontend.",
                "Branding, Ilustracion digital, Figma, Motion graphics, Fotografia",
                "Disenadora voluntaria de piezas para eventos culturales de Medellin. Asistente en taller de serigrafia.",
                "/img/photo-camila.jpg");
        habilidadRepository.save(new Habilidad("Figma & Prototipado", "Avanzado", "Tecnica", perfilCamila));
        habilidadRepository.save(new Habilidad("Teoria del Color", "Intermedio", "Tecnica", perfilCamila));
        habilidadRepository.save(new Habilidad("Ilustracion Digital", "Intermedio", "Tecnica", perfilCamila));
        habilidadRepository.save(new Habilidad("Creatividad", "Avanzado", "Blanda", perfilCamila));
        Proyecto proyCamila1 = new Proyecto();
        proyCamila1.setPerfil(perfilCamila);
        proyCamila1.setTitulo("Identidad Visual Cafeteria Aroma");
        proyCamila1.setDescripcion("Logo, menu y empaques para cafeteria local con manual de marca de 30 paginas.");
        proyCamila1.setUrl("https://github.com/camilatorres/identidad-aroma");
        proyCamila1.setTecnologias("Illustrator, Photoshop, InDesign");
        proyectoRepository.save(proyCamila1);
        Proyecto proyCamila2 = new Proyecto();
        proyCamila2.setPerfil(perfilCamila);
        proyCamila2.setTitulo("Portfolio Interactivo Personal");
        proyCamila2.setDescripcion("Sitio one-page con animaciones scroll y galeria filtrable de ilustraciones propias.");
        proyCamila2.setUrl("https://github.com/camilatorres/portfolio");
        proyCamila2.setTecnologias("HTML, CSS, JavaScript, GSAP");
        proyectoRepository.save(proyCamila2);
        Certificado certCamila = new Certificado();
        certCamila.setPerfil(perfilCamila);
        certCamila.setNombre("Curso Profesional de Diseno UX/UI");
        certCamila.setInstitucion("Domestika / Escuela de Diseno");
        certCamila.setFecha(LocalDate.of(2025, 6, 30));
        certCamila.setUrlArchivo("/docs/certificado-ux-camila.pdf");
        certificadoRepository.save(certCamila);

        PerfilEstudiante perfilDaniela = crearPerfil(daniela,
                "Estudiante de primer semestre dando sus primeros pasos en programacion con Python y logica computacional.",
                "Python, Robotica educativa, Lectura, Dibujo",
                "Sin experiencia laboral previa. Participante del club de robotica del colegio en 2025.",
                "/img/photo-daniela.jpg");
        habilidadRepository.save(new Habilidad("Python Basico", "Basico", "Tecnica", perfilDaniela));
        habilidadRepository.save(new Habilidad("Trabajo en Equipo", "Basico", "Blanda", perfilDaniela));
        Proyecto proyDaniela = new Proyecto();
        proyDaniela.setPerfil(perfilDaniela);
        proyDaniela.setTitulo("Mi Primera Calculadora en Python");
        proyDaniela.setDescripcion("Calculadora por consola con suma, resta y validacion de entradas del usuario.");
        proyDaniela.setUrl("https://github.com/danielaortiz/calculadora-py");
        proyDaniela.setTecnologias("Python");
        proyectoRepository.save(proyDaniela);

        PerfilEstudiante perfilJuan = crearPerfil(juan,
                "Tecnologo en redes en formacion, con interes en infraestructura, administracion Linux y seguridad basica.",
                "Redes LAN/WAN, Linux, Ciberseguridad, IoT, Electronica",
                "Practicante de soporte en el area de sistemas de su colegio. Instalador de redes domesticas por encargo.",
                "/img/photo-juan.jpg");
        habilidadRepository.save(new Habilidad("Direccionamiento IP y Subnetting", "Intermedio", "Tecnica", perfilJuan));
        habilidadRepository.save(new Habilidad("Linux Basico", "Intermedio", "Tecnica", perfilJuan));
        habilidadRepository.save(new Habilidad("Cableado Estructurado", "Basico", "Tecnica", perfilJuan));
        habilidadRepository.save(new Habilidad("Resolucion de Problemas", "Intermedio", "Blanda", perfilJuan));
        Proyecto proyJuan = new Proyecto();
        proyJuan.setPerfil(perfilJuan);
        proyJuan.setTitulo("Red LAN Simulada para un Aula");
        proyJuan.setDescripcion("Diseno y simulacion de red para 30 equipos con VLANs, DHCP y plan de direccionamiento.");
        proyJuan.setUrl("https://github.com/juandiegomarin/red-aula-pkt");
        proyJuan.setTecnologias("Packet Tracer, Subnetting, VLAN");
        proyectoRepository.save(proyJuan);
        Certificado certJuan = new Certificado();
        certJuan.setPerfil(perfilJuan);
        certJuan.setNombre("Fundamentos de Redes y CCNA Introductorio");
        certJuan.setInstitucion("Cisco Networking Academy");
        certJuan.setFecha(LocalDate.of(2025, 10, 5));
        certJuan.setUrlArchivo("/docs/certificado-redes-juan.pdf");
        certificadoRepository.save(certJuan);

        // 7. Familiares y Vinculos (portal familiar con datos de varios estudiantes)
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

        Usuario uFam3 = usuarioRepository.save(new Usuario(null, "Lucia Torres", "lucia.torres@familia.com", "fam123", "FAMILIAR", "ACTIVO"));
        Familiar fam3 = new Familiar();
        fam3.setUsuario(uFam3);
        fam3.setParentesco("Madre");
        fam3.setTelefono("3206549871");
        fam3.setDireccion("Cra 70 # 25-40, Medellin");
        familiarRepository.save(fam3);
        vinculoRepository.save(new VinculoFamiliarEstudiante(fam3, camila, true));
        accesoFamiliarRepository.save(new AccesoFamiliar(fam3, "Consulta de portafolio y calificaciones de diseno"));

        Usuario uFam4 = usuarioRepository.save(new Usuario(null, "Pedro Morales", "pedro.morales@familia.com", "fam123", "FAMILIAR", "ACTIVO"));
        Familiar fam4 = new Familiar();
        fam4.setUsuario(uFam4);
        fam4.setParentesco("Padre");
        fam4.setTelefono("3012345678");
        fam4.setDireccion("Calle 33 # 80-12, Medellin");
        familiarRepository.save(fam4);
        vinculoRepository.save(new VinculoFamiliarEstudiante(fam4, mateo, true));
        accesoFamiliarRepository.save(new AccesoFamiliar(fam4, "Consulta de alertas academicas y asistencias"));

        // 8. Matriculas: matriz diferenciada por estudiante (periodo 2026-1)
        matriculaRepository.save(new Matricula(santiago, mWeb, gWeb, "2026-1"));
        matriculaRepository.save(new Matricula(santiago, mAlgo, gAlgo, "2026-1"));

        matriculaRepository.save(new Matricula(valeria, mPython, gPython, "2026-1"));
        matriculaRepository.save(new Matricula(valeria, mBD, gBD, "2026-1"));
        matriculaRepository.save(new Matricula(valeria, mWeb, gWeb, "2026-1"));

        matriculaRepository.save(new Matricula(mateo, mWeb, gWeb, "2026-1"));
        matriculaRepository.save(new Matricula(mateo, mBD, gBD, "2026-1"));

        matriculaRepository.save(new Matricula(camila, mDiseno, gDiseno, "2026-1"));
        matriculaRepository.save(new Matricula(camila, mAlgo, gAlgo, "2026-1"));

        matriculaRepository.save(new Matricula(daniela, mPython, gPython, "2026-1"));

        matriculaRepository.save(new Matricula(juan, mRedes, gRedes, "2026-1"));
        matriculaRepository.save(new Matricula(juan, mAlgo, gAlgo, "2026-1"));

        // 9. Calificaciones: 3 por materia matriculada, valores y comentarios unicos por estudiante
        // Santiago (alto: 4.8 / 4.5 / 4.2)
        guardarNota(4.8, "Parcial 1", LocalDate.of(2026, 2, 15), "API REST con Spring Boot impecable y bien documentada", santiago, mWeb, gWeb);
        guardarNota(4.5, "Quiz 1", LocalDate.of(2026, 2, 28), "Componentes React reutilizables con hooks correctos", santiago, mWeb, gWeb);
        guardarNota(4.2, "Taller", LocalDate.of(2026, 3, 5), "Despliegue fullstack funcional con detalles menores de estilos", santiago, mWeb, gWeb);
        guardarNota(4.8, "Parcial 1", LocalDate.of(2026, 2, 15), "Analisis de complejidad sobresaliente en ordenamiento", santiago, mAlgo, gAlgo);
        guardarNota(4.5, "Quiz 1", LocalDate.of(2026, 2, 28), "Recursividad aplicada con criterio en el quiz", santiago, mAlgo, gAlgo);
        guardarNota(4.2, "Taller", LocalDate.of(2026, 3, 5), "Taller de grafos resuelto con solucion elegante", santiago, mAlgo, gAlgo);

        // Valeria (alto: 4.9 / 4.7 / 4.6)
        guardarNota(4.9, "Parcial 1", LocalDate.of(2026, 2, 15), "Scripting Python limpio con comprensiones y tipado", valeria, mPython, gPython);
        guardarNota(4.7, "Quiz 1", LocalDate.of(2026, 2, 28), "Quiz de pandas preciso y bien justificado", valeria, mPython, gPython);
        guardarNota(4.6, "Taller", LocalDate.of(2026, 3, 5), "Taller de automatizacion con manejo de errores solido", valeria, mPython, gPython);
        guardarNota(4.9, "Parcial 1", LocalDate.of(2026, 2, 15), "Modelo relacional normalizado sin redundancias", valeria, mBD, gBD);
        guardarNota(4.7, "Quiz 1", LocalDate.of(2026, 2, 28), "Consultas JOIN complejas resueltas de forma optima", valeria, mBD, gBD);
        guardarNota(4.6, "Taller", LocalDate.of(2026, 3, 5), "Taller de vistas y triggers bien implementado", valeria, mBD, gBD);
        guardarNota(4.9, "Parcial 1", LocalDate.of(2026, 2, 15), "Interfaz accesible con ARIA impecable segun WCAG", valeria, mWeb, gWeb);
        guardarNota(4.7, "Quiz 1", LocalDate.of(2026, 2, 28), "Quiz de Next.js con render hibrido dominado", valeria, mWeb, gWeb);
        guardarNota(4.6, "Taller", LocalDate.of(2026, 3, 5), "Taller responsive mobile-first sobresaliente", valeria, mWeb, gWeb);

        // Mateo (bajo: 2.4 / 2.8 / 2.6 -> dispara alertas)
        guardarNota(2.4, "Parcial 1", LocalDate.of(2026, 2, 15), "Dificultad persistente con persistencia JPA y relaciones", mateo, mWeb, gWeb);
        guardarNota(2.8, "Quiz 1", LocalDate.of(2026, 2, 28), "Quiz de componentes incompleto, faltan props clave", mateo, mWeb, gWeb);
        guardarNota(2.6, "Taller", LocalDate.of(2026, 3, 5), "Taller entregado tarde con errores de build sin resolver", mateo, mWeb, gWeb);
        guardarNota(2.4, "Parcial 1", LocalDate.of(2026, 2, 15), "Confunde claves foraneas con indices en el parcial", mateo, mBD, gBD);
        guardarNota(2.8, "Quiz 1", LocalDate.of(2026, 2, 28), "Quiz de SELECT basico aprobado con lo minimo", mateo, mBD, gBD);
        guardarNota(2.6, "Taller", LocalDate.of(2026, 3, 5), "Taller de normalizacion sin aplicar tercera forma normal", mateo, mBD, gBD);

        // Camila (medio-alto: 4.1 / 3.9 / 4.0)
        guardarNota(4.1, "Parcial 1", LocalDate.of(2026, 2, 15), "Sistema de diseno coherente con paleta propia", camila, mDiseno, gDiseno);
        guardarNota(3.9, "Quiz 1", LocalDate.of(2026, 2, 28), "Quiz de teoria del color aprobado con dudas en contraste", camila, mDiseno, gDiseno);
        guardarNota(4.0, "Taller", LocalDate.of(2026, 3, 5), "Maquetado de Figma a CSS fiel al prototipo", camila, mDiseno, gDiseno);
        guardarNota(4.1, "Parcial 1", LocalDate.of(2026, 2, 15), "Pseudocodigo claro y estructurado en el parcial", camila, mAlgo, gAlgo);
        guardarNota(3.9, "Quiz 1", LocalDate.of(2026, 2, 28), "Quiz de pilas y colas con un error de indices", camila, mAlgo, gAlgo);
        guardarNota(4.0, "Taller", LocalDate.of(2026, 3, 5), "Taller de diagramas de flujo completo y ordenado", camila, mAlgo, gAlgo);

        // Daniela (bajo: 2.5 / 2.3 / 2.7 -> dispara alertas, solo Python)
        guardarNota(2.5, "Parcial 1", LocalDate.of(2026, 2, 15), "Primer parcial con errores de sintaxis e indentacion", daniela, mPython, gPython);
        guardarNota(2.3, "Quiz 1", LocalDate.of(2026, 2, 28), "Quiz de variables y tipos con conceptos sin afianzar", daniela, mPython, gPython);
        guardarNota(2.7, "Taller", LocalDate.of(2026, 3, 5), "Taller guiado completado con ayuda del monitor", daniela, mPython, gPython);

        // Juan Diego (medio: 3.8 / 3.6 / 3.9)
        guardarNota(3.8, "Parcial 1", LocalDate.of(2026, 2, 15), "Subnetting y direccionamiento IP bien resueltos", juan, mRedes, gRedes);
        guardarNota(3.6, "Quiz 1", LocalDate.of(2026, 2, 28), "Quiz de modelo OSI con confusion en capa de transporte", juan, mRedes, gRedes);
        guardarNota(3.9, "Taller", LocalDate.of(2026, 3, 5), "Taller de cableado y topologias ejecutado sin fallas", juan, mRedes, gRedes);
        guardarNota(3.8, "Parcial 1", LocalDate.of(2026, 2, 15), "Parcial de busqueda y ordenamiento solido", juan, mAlgo, gAlgo);
        guardarNota(3.6, "Quiz 1", LocalDate.of(2026, 2, 28), "Quiz de complejidad con notacion Big-O parcial", juan, mAlgo, gAlgo);
        guardarNota(3.9, "Taller", LocalDate.of(2026, 3, 5), "Taller de estructuras lineales bien implementado", juan, mAlgo, gAlgo);

        // 10. Asistencias: 4 sesiones por materia matriculada, patron propio por estudiante
        LocalDate[] fechas = {
            LocalDate.of(2026, 2, 10),
            LocalDate.of(2026, 2, 17),
            LocalDate.of(2026, 2, 24),
            LocalDate.of(2026, 3, 3)
        };

        // Santiago: asistencia perfecta
        for (LocalDate f : fechas) {
            guardarAsistencia(f, "PRESENTE", "Santiago puntual y participativo en web", false, santiago, gWeb);
            guardarAsistencia(f, "PRESENTE", "Santiago puntual y participativo en algoritmos", false, santiago, gAlgo);
        }

        // Valeria: asistencia perfecta
        for (LocalDate f : fechas) {
            guardarAsistencia(f, "PRESENTE", "Valeria puntual con aportes en python", false, valeria, gPython);
            guardarAsistencia(f, "PRESENTE", "Valeria puntual con aportes en bases de datos", false, valeria, gBD);
            guardarAsistencia(f, "PRESENTE", "Valeria puntual con aportes en web", false, valeria, gWeb);
        }

        // Mateo: 2 AUSENTE + 1 JUSTIFICADO por materia
        guardarAsistencia(fechas[0], "PRESENTE", "Mateo llego puntual a web", false, mateo, gWeb);
        guardarAsistencia(fechas[1], "AUSENTE", "Mateo no presento excusa en web", false, mateo, gWeb);
        guardarAsistencia(fechas[2], "AUSENTE", "Falta recurrente de Mateo en web", false, mateo, gWeb);
        guardarAsistencia(fechas[3], "JUSTIFICADO", "Incapacidad medica de Mateo en web", true, mateo, gWeb);
        guardarAsistencia(fechas[0], "PRESENTE", "Mateo puntual aunque disperso en BD", false, mateo, gBD);
        guardarAsistencia(fechas[1], "AUSENTE", "Mateo no presento excusa en BD", false, mateo, gBD);
        guardarAsistencia(fechas[2], "AUSENTE", "Falta recurrente de Mateo en BD", false, mateo, gBD);
        guardarAsistencia(fechas[3], "JUSTIFICADO", "Incapacidad medica de Mateo en BD", true, mateo, gBD);

        // Camila: casi perfecta, una justificada en algoritmos
        for (LocalDate f : fechas) {
            guardarAsistencia(f, "PRESENTE", "Camila puntual con bitacora de diseno al dia", false, camila, gDiseno);
        }
        guardarAsistencia(fechas[0], "PRESENTE", "Camila puntual en algoritmos", false, camila, gAlgo);
        guardarAsistencia(fechas[1], "PRESENTE", "Camila puntual en algoritmos", false, camila, gAlgo);
        guardarAsistencia(fechas[2], "JUSTIFICADO", "Camila en feria de diseno con permiso academico", true, camila, gAlgo);
        guardarAsistencia(fechas[3], "PRESENTE", "Camila regreso puntual a algoritmos", false, camila, gAlgo);

        // Daniela: 2 AUSENTE en su unica materia
        guardarAsistencia(fechas[0], "PRESENTE", "Daniela puntual en su primera clase de python", false, daniela, gPython);
        guardarAsistencia(fechas[1], "AUSENTE", "Daniela falto sin aviso a python", false, daniela, gPython);
        guardarAsistencia(fechas[2], "AUSENTE", "Daniela reincide en ausencia a python", false, daniela, gPython);
        guardarAsistencia(fechas[3], "PRESENTE", "Daniela regreso con compromiso a python", false, daniela, gPython);

        // Juan Diego: casi perfecta, una justificada en algoritmos
        for (LocalDate f : fechas) {
            guardarAsistencia(f, "PRESENTE", "Juan puntual con laboratorio de redes al dia", false, juan, gRedes);
        }
        guardarAsistencia(fechas[0], "PRESENTE", "Juan puntual en algoritmos", false, juan, gAlgo);
        guardarAsistencia(fechas[1], "PRESENTE", "Juan puntual en algoritmos", false, juan, gAlgo);
        guardarAsistencia(fechas[2], "PRESENTE", "Juan puntual en algoritmos", false, juan, gAlgo);
        guardarAsistencia(fechas[3], "JUSTIFICADO", "Juan en cita odontologica con excusa valida", true, juan, gAlgo);

        // 11. Solicitudes de Bienestar y Seguimientos (un caso por estudiante afectado)
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

        NecesidadApoyo nec3 = new NecesidadApoyo();
        nec3.setEstudiante(camila);
        nec3.setTipo("Orientacion Vocacional");
        nec3.setDescripcion("La estudiante duda entre profundizar en diseno grafico o migrar al desarrollo frontend; pide orientacion de perfil profesional.");
        nec3.setFecha(LocalDate.of(2026, 2, 12));
        nec3.setPrioridad("BAJA");
        nec3.setEstado("CERRADA");
        nec3.setCategoria(catVocacional);
        nec3.setProfesional(orientador);
        NecesidadApoyo nec3Guardada = necesidadRepository.save(nec3);

        seguimientoRepository.save(new Seguimiento(nec3Guardada, LocalDate.of(2026, 2, 26), "Camila definio ruta hibrida diseno+frontend y cerro el proceso con plan de portafolio.", "CERRADA"));

        System.out.println(">>> SEED COMPLETO: Base de datos institucional inicializada exitosamente con datos realistas.");
    }

    private PerfilEstudiante crearPerfil(Estudiante estudiante, String resumen, String intereses,
                                         String experiencia, String fotoUrl) {
        PerfilEstudiante perfil = new PerfilEstudiante();
        perfil.setEstudiante(estudiante);
        perfil.setResumen(resumen);
        perfil.setIntereses(intereses);
        perfil.setExperiencia(experiencia);
        perfil.setFotoUrl(fotoUrl);
        return perfilRepository.save(perfil);
    }

    private void guardarNota(double valor, String tipo, LocalDate fecha, String comentario,
                             Estudiante estudiante, Materia materia, Grupo grupo) {
        notaRepository.save(new Nota(valor, tipo, fecha, comentario, estudiante, materia, grupo));
    }

    private void guardarAsistencia(LocalDate fecha, String estado, String observaciones,
                                   boolean justificada, Estudiante estudiante, Grupo grupo) {
        asistenciaRepository.save(new Asistencia(fecha, estado, observaciones, justificada, estudiante, grupo));
    }
}
