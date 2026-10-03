package com.edu.plataforma;

import com.edu.plataforma.dto.UsuarioCreateDTO;
import com.edu.plataforma.dto.UsuarioDTO;
import com.edu.plataforma.model.Usuario;
import com.edu.plataforma.repository.UsuarioRepository;
import com.edu.plataforma.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void testCrearYConsultarUsuario() {
        UsuarioCreateDTO dto = new UsuarioCreateDTO();
        dto.setNombre("Prueba Unitaria");
        dto.setCorreo("test.unitario@instituto.edu.co");
        dto.setContrasena("claveSegura123");
        dto.setRol("ESTUDIANTE");
        dto.setPrograma("Desarrollo de Software");
        dto.setSemestre(1);

        UsuarioDTO creado = usuarioService.crearUsuario(dto);

        assertNotNull(creado.getId());
        assertEquals("Prueba Unitaria", creado.getNombre());
        assertEquals("test.unitario@instituto.edu.co", creado.getCorreo());
        assertEquals("ESTUDIANTE", creado.getRol());

        UsuarioDTO consultado = usuarioService.obtenerPorId(creado.getId());
        assertEquals(creado.getId(), consultado.getId());
    }

    @Test
    public void testFiltrarPorRol() {
        List<UsuarioDTO> docentes = usuarioService.listarPorRol("DOCENTE");
        assertNotNull(docentes);
        assertTrue(docentes.size() >= 2);
    }
}