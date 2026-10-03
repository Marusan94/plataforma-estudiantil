# EDU.CORE ROBOT DE DECISIONES: PAPER TÉCNICO COMPLETO

## 1. Introducción

Este documento formaliza la arquitectura, el diseño de interfaz y la lógica de interacción del asistente robótico integrado en EDU.CORE. El robot actúa como un agente proactivo que rota decisiones, recuerda alertas y responde al contexto del usuario sin requerir que éste escriba constantemente. La visión es convertir al robot en una extensión visual del dashboard, no en una ventana de chat tradicional.

## 2. Objetivos de diseño

- **Identidad visual**: pixel art vintage integrado al tema oscuro moderno.
- **Proactividad**: el robot no espera; cada ciclo de 8 segundos presenta una nueva decisión.
- **Contextualidad**: los mensajes cambian según el rol y la pestaña activa.
- **Percepción de movimiento**: animaciones CSS suaves que evitan la sensación de estático.
- **Uso de voz mínima**: no hay audio real, pero la boca del robot se anima como si hablara.

## 3. Arquitectura lógica

La aplicación sigue el patrón de componente React:

1. **Estado**: `open` abre/cierra el panel; `index` indica la decisión actual.
2. **Efectos**: `useEffect` reinicia el índice cuando cambia el rol o la pestaña; otro efecto rota el índice cada 8 segundos.
3. **Renderizado**: siempre se muestra el avatar robot; si `open` es true, se renderiza el panel overlay.

## 4. Tabla de decisiones

La tabla `DECISIONS` indexada por rol contiene objetos:

```js
{ role: 'ALERTA', text: '...', action: '...' }
```

Cada objeto tiene una etiqueta de color (`ALERTA`, `RECOMENDACIÓN`, etc.), un mensaje corto y una acción sugerida.

## 5. Lógica de relevancia

La función `relevanceScore` evalúa qué tan relevante es cada decisión para la pestaña actual:

```js
const map = { dashboard: 2, 'lab-digital': 1, academico: 1 };
const keywords = { alerta: ['alerta','riesgo'], recordatorio: ['recordatorio'] };
```

El algoritmo suma puntos si el texto contiene palabras clave de la categoría y un peso según la pestaña. Esto asegura que el usuario ve primero lo más útil para el contexto.

## 6. Componente `CopilotWidget`

El componente principal importa `React` hooks, define la tabla de decisiones y retorna un wrapper `<div className="robot-core">`.

### 6.1 Avatar robot

El avatar es un conjunto de divs:

```html
<button className="robot-avatar">
  <div className="avatar-head">
    <div className="avatar-eye left"></div>
    <div className="avatar-eye right"></div>
    <div className="avatar-mouth"></div>
  </div>
  <div className="avatar-body"></div>
</button>
```

Cada parte tiene reglas CSS: borde, tamaño y animación. La cabeza tiene dos ojos animados; la boca se anima para simular conversación.

### 6.2 Panel de decisión

El panel overlay se muestra con clase `.robot-panel`. Contiene:

- **Header**: título a modo terminal.
- **Screen**: etiqueta de estado + texto de decisión + botón de acción.
- **Nav**: flechas y dots indicadores.
- **Input**: para futuras integraciones con IA.

## 7. Estilos CSS

El archivo `index.css` define:

- `.robot-core`: contenedor posicionado.
- `.robot-panel`: panel oscuro con borde cyan.
- `.avatar-head`, `.avatar-body`: formas pixel.
- `.avatar-eye`: puntos luminosos con parpadeo.
- `.avatar-mouth`: barra que cambia de altura para simular habla.
- `@keyframes avatar-bob`: movimiento vertical.
- `@keyframes eye-blink`: parpadeo.
- `@keyframes mouth-talk`: animación de habla.

## 8. Pixel Art y movimiento

El robot no usa imágenes; se define con CSS. El efecto pixel se logra con:

- Border-radius bajo (0-4px) en vez de redondeos pronunciados.
- Box-shadow con offsets duros para simular sombras pixel.
- Textura sólida y colores contrastados.
- Animaciones `step-end` para mouth-talk, evitando interpolaiones suaves.
- Movimiento vertical `avatar-bob` que da sensación de que el robot camina o flota.

## 9. Interacción con el usuario

1. **Click en robot**: abre el panel.
2. **Rotación automática**: cada 8 s el texto cambia.
3. **Navegación manual**: flechas ← → cambian el índice sin esperar el intervalo.
4. **Input**: la fila inferior acepta comandos; por ahora es visual.

## 10. Accesibilidad

- Uso de `<button>` para interacción.
- Animaciones pueden reducirse via `prefers-reduced-motion`.
- Contraste alto entre fondo oscuro y texto cyan.

## 11. Testing sugerido

- Verificar que la rotación cambie el texto cada 8 s.
- Confirmar que las flechas cambien la decisión y pausen la auto-rotación temporalmente.
- Comprobar que el avatar se mueva y parpadee en todos los roles.
- Validar que el panel no se salga de la pantalla en móviles.

## 12. Roadmap

- Conectar `DECISIONS` a un endpoint real.
- Agregar speech synthesis para voz sintética.
- Implementar expresiones faciales según el `role` de la decisión.
- Exportar el avatar a un icono PWA estático.
- Añadir modo oscuro/claro para todo el panel.

## 13. Conclusión

La arquitectura actual es ligera, funcional y visualmente distintiva. El robot en pixel art representa una alternativa al chatbot tradicional, ofreciendo una experiencia más lúdica y reactiva. La lógica de relevancia asegura que cada mensaje sea contextual, mientras que las animaciones CSS mantienen el movimiento sin overhead.
