# EDU.CORE ROBOT DE DECISIONES: ARQUITECTURA Y PAPER TÉCNICO

## Resumen

Este documento describe la arquitectura y el diseño técnico del asistente proactivo tipo robot integrado en EDU.CORE. El objetivo es reemplazar la interfaz conversacional tradicional por un agente visual en pixel art que se mueve, habla, rota decisiones y reacciona al contexto del usuario (rol y pestaña activa).

## 1. Contexto y objetivos

- **Diferenciación visual**: la plataforma actualmente utiliza una interfaz oscura minimalista; el robot aporta una capa de “personaje” que humaniza la experiencia sin saturar la vista.
- **Proactividad**: el robot no espera al usuario; comienza una secuencia de decisiones en cuanto se renderiza.
- **Contextualidad**: el contenido del mensaje depende del rol (ESTUDIANTE, DOCENTE, FAMILIAR, BIENESTAR, ADMIN) y de la pestaña activa en la UI.

## 2. Arquitectura general

El sistema se compone de tres capas interconectadas:

### 2.1 Capa de datos (Decisión/IRC)

Una tabla de constantes `DECISIONS` indexada por rol. Cada entrada tiene:
- `role`: categoría del mensaje (ALERTA, RECOMENDACIÓN, etc.)
- `text`: contenido legible
- `action`: acción sugerida para el usuario

### 2.2 Capa de lógica (Selección + Rotación)

En React, el componente `CopilotWidget` mantiene el estado `index`. Un `useEffect` con `setInterval` cada 8 segundos incrementa el índice y refresca el texto. Una función `relevanceScore` pondera cada decisión según coincidencias de palabras clave con la pestaña activa (`activeTab`).

Además, cuando el usuario hace clic en las flechas de navegación, se desactiva temporalmente la auto-rotación.

### 2.3 Capa de presentación (Pixel Art Animado)

El robot se dibuja en CSS/HTML como un sprite simplificado pixelado. Tiene una animación `robot-bob` para movimiento vertical y una boca `robot-mouth` que se ilumina para simular habla cuando el `text` se actualiza. El contenedor exterior usa el estilo oscuro ya definido en la plataforma.

## 3. Componentes técnicos

### 3.1 `CopilotWidget.jsx`

- Importa React hooks.
- Define la tabla `DECISIONS`.
- Retorna un `<div>` con clase `robot-core`.
- Contiene el `<button className="robot-avatar">` con `avatar-head` y `avatar-body`.
- Cuando `open` es `true`, renderiza el panel `<div className="robot-panel">`.
- El panel contiene: header, pantalla de decisión, nav, input.

### 3.2 `index.css`

Se agregan las siguientes secciones:
- `.robot-core`, `.robot-panel`, `.robot-header`, etc.
- Keyframes `avatar-bob`, `eye-blink`.
- Clases `.status-*` para colores por tipo de mensaje.
- Pixel-art effect: borde rígido, sin redondeos excesivos, tipografías monoespaciadas.

## 4. Experiencia de usuario

1. **Inicio**: el avatar de robot aparece pulsando con ojos que titilan.
2. **Interacción**: al hacer clic, el panel se desliza hacia arriba con un mensaje contextual.
3. **Auto-rotación**: cada 8 s cambia el mensaje y se actualiza el `status-tag`.
4. **Navegación manual**: las flechas ← → permiten recorrer las decisiones sin esperar.
5. **Cierre**: el `×` oculta el panel pero el avatar sigue parpadeando.

## 5. Rendimiento

- No se usan canvas ni librerías externas; el movimiento es puro CSS.
- Las imágenes no están presentes, solo estilos y texto.
- Los intervalos de rotación son fáciles de cancelar al desmontar.

## 6. Roadmap

- Conectar la tabla `DECISIONS` a `GET /api/v1/decisions`.
- Agregar voz síntesis `speechSynthesis` opcional.
- Añadir expresión facial dinámica según tipo de mensaje.
- Exportar el avatar a SVG estático para PWA service worker cache.

## 7. Conclusión

La arquitectura actual permite probar un asistente proactivo sin IoT ni backends complejos. El robot en pixel art ofrece identidad visual propia y se integra al tema oscuro de EDU.CORE sin perder usabilidad.
