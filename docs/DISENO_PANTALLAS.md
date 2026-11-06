# GUÍA DE DISEÑO — CodeMentor AI (pantallas pendientes)

Extraída del borrador visual (HTML) que ya no se necesita. **Es solo una referencia visual y de textos.** La app de Android Studio es el producto final y su lógica manda: los números de ejemplo que aparecen aquí (Alex, 1.240 XP, racha 5, etc.) **no existen** en la app real; todo se calcula desde el progreso guardado en Firebase y un usuario nuevo empieza en cero.

Convenciones: medidas en dp (1 px del borrador = 1 dp). Fuente Inter. Los colores ya definidos en `Color.kt` se reutilizan; los que faltan están en la sección "Colores adicionales". Lo que esta guía no define → se pregunta al dueño del proyecto, no se inventa.

Pantallas ya hechas (no están aquí): Bienvenida, Crear cuenta, Iniciar sesión, Recuperar contraseña.

---

## 0. Elementos comunes

- **Fondo de pantalla** `AppBackground`. Margen lateral 22. Las pantallas con pestañas dejan 98 dp libres al final del scroll (alto de la barra inferior).
- **topline** (cabecera de pantalla): fila con `justify space-between`, separación 12, margen inferior 22. Contiene botón atrás (36×36, borde `Line`, esquina 12, fondo blanco, símbolo "‹" 21 sp) y a la derecha una etiqueta o pill.
- **eyebrow**: 10 sp, mayúsculas, espaciado 1.2, peso 850, color `Primary`, margen inferior 10.
- **title**: 26 sp, interlineado 1.16, espaciado -0.8, peso 850 (variante `sm` 23 sp). **sub**: 13 sp, `Muted`, interlineado 1.55.
- **Tarjeta (card)**: fondo blanco, borde `Line`, esquina 18, padding 16, sombra suave (ya existe `CardShadow`).
- **Pill**: 10 sp, peso 800, padding 6×9, esquina total, fondo `PrimarySoft` / texto `Primary`. Variantes: `mint` (fondo `Mint`, texto `MintInk`), `yellow` (fondo `#FDF0DA`, texto `#B9731A`), `rose/red` (fondo `#FDE8E7`, texto `#C24A4A`).
- **Barra de progreso**: alto 7, esquina total, pista `#E4E9F3`, relleno con degradado horizontal `#5B8DEF → #35C79A`.
- **Section heading**: fila con título 14 sp peso 700 a la izquierda y un link-btn (10 sp, `Primary`, peso 800) opcional a la derecha; margen vertical 22 arriba / 10 abajo.
- **Fila (rowline)**: icono 36×36 (esquina 12, fondo `#E5EDFD`, color `Primary`) + bloque con título 12 sp negrita y subtítulo 10 sp `Muted` (margen 4) + chevron "›" (19 sp, `#9AA5BD`) opcional. Padding vertical 13, separador inferior `Line` (la última sin separador).
- **statbox**: fondo blanco, borde `Line`, esquina 15, padding 13: icono emoji 17 sp, número 20 sp peso 800 (margen 8/2), leyenda 10 sp `Muted`. Se usan de a dos (grid 2 columnas, separación 10).
- **Avatar**: 40×40, esquina 14, degradado `#C9DAFB → #BDEFD9`, iniciales (máx. 2 letras) en `#3A63C2`, 17 sp peso 900. En Perfil: 76×76, esquina 25, 30 sp.
- **Botón**: ya existen `PrimaryButton`/`SecondaryButton` (alto 48; variante `sm` alto 39, 12 sp; `ghost` = blanco con borde `Line`, texto `Ink`).
- **Botón fijo inferior** (`bottom-fixed`): sticky al final de la pantalla, padding 18/22/26, fondo con degradado transparente→`AppBackground` en los 20 dp superiores. Ya existe el patrón en las pantallas hechas.
- **Barra de pestañas inferior**: alto 73, fondo blanco 93 % con desenfoque, borde superior `Line`, 5 botones iguales. Cada uno: icono dentro de un cuadro 26×26 (esquina 9) + texto 9 sp peso 650. Inactivo `#8A96B3`; activo `Primary` con el cuadro del icono en `PrimarySoft`. Pestañas (en orden): **Inicio** (casa), **Aprender** (libro), **Retos** (diana), **Tutor IA** (destello), **Perfil** (usuario). Iconos de trazo 1.8, 19 dp, redondeados.
- **Hoja inferior (sheet)**: fondo oscuro `#1F2A44` al 45 %; panel blanco con esquinas superiores 24, padding 22/20/30, máx. 80 % de alto; título 18 sp, texto 12 sp `Muted`; botones apilados con margen 10. Se cierra tocando fuera o con atrás.
- **Toast**: mensajes cortos (≈2.6 s).
- **Estado vacío**: caja con borde punteado `#CBD3E6`, fondo `AppBackground`, esquina 14, texto 12 sp `Muted` centrado.

### Colores adicionales
`Yellow #FDF0DA`, `YellowInk #B9731A`, `Rose #FDE8E7`, `RoseInk #C24A4A`, `Done/Verde #35C79A` (sombra `#229A77`), `VerdeNodo #2FB38A`, `Locked #DDE4F2` (sombra `#C2CCE0`, texto `#8A96B3`), `Hero degradado #3F5FD8 → #6A5CE6`, `CodeBg #1C2540`, `CodeText #E9EDFA`, `CodeLineNumber #7F8AA6`, sintaxis: palabra clave `#B3A8FF`, número `#FFC46B`, cadena `#8CE0B7`, función `#80C9FF`. Salida esperada: fondo `#EDFAF4`, borde `#CDEEDD`, texto `#1B7A5C`. Retroalimentación buena: fondo `#E4F7EE`/texto `#1B7A5C`; mala: fondo `#FDECEC`/texto `#A83E3E`. Pista: fondo `#FDF0DA`/texto `#8A5A12`. Tarjeta azul suave: fondo `#E5EDFD`, borde `#D3E2FC`. Elegida (choice): borde `Primary`, fondo `#EEF3FE`.

---

## 1. Personalización (3 pantallas, sin barra de pestañas, con botón fijo inferior)

### 1.1 Nivel (paso 1 de 2)
- topline: botón atrás · indicador de pasos (2 barritas 19×4, la activa en `Primary`, el resto `#E1E6F1`) · "1 de 2".
- eyebrow "Personalicemos tu ruta" · título "¿Qué tanto sabes de programación?" · sub "No hay respuestas buenas o malas. Empezaremos desde tu nivel."
- Lista de opciones (margen superior 23, separación 9). **Choice**: ancho completo, padding 14, borde 1.5 `Line`, esquina 15, fondo blanco; icono 34×34 (esquina 11, fondo `#EAF0FD`, emoji 16 sp) + texto (12 sp peso 750, con subtexto 10 sp peso 500 `Muted`) + radio a la derecha (17 dp, borde 1.5 `#CBD3E6`; seleccionado: borde `Primary` con punto interior 9 dp `Primary`).
  - 🌱 **Estoy empezando** — "Quiero aprender desde cero." → valor `Principiante`
  - 🧩 **Conozco lo básico** — "He visto variables y condiciones." → `Básico`
  - 🚀 **Ya he programado** — "Quiero practicar y avanzar." → `Intermedio`
- Botón fijo: "Continuar →".

### 1.2 Objetivo (paso 2 de 2)
- topline con "2 de 2" (ambas barritas activas). eyebrow "Un objetivo a la vez" · título "¿Qué quieres lograr?" · sub "Escoge lo que más se parezca a tu meta."
  - 🧠 **Entender la programación** — "Comprender la lógica, no memorizar."
  - 🎓 **Aprobar mi materia** — "Reforzar temas vistos en clase."
  - 🧩 **Resolver problemas** — "Practicar cómo pensar como programador."
  - 🛠️ **Crear mis proyectos** — "Pasar de los ejercicios a algo real."
- Botón fijo: "Crear mi ruta →".

### 1.3 Ruta lista
- topline: botón atrás · pill mint "✓ Lista para empezar".
- Ilustración centrada: cuadro 90×90, esquina 29, degradado `#CFE0FC → #C9F2E1`, emoji 🚀 (42 sp), sombra azul suave; margen 23 arriba / 19 abajo.
- Centrado: eyebrow "Tu camino empieza aquí" · título "Tu ruta está lista" · sub "Creamos una base para que avances a tu ritmo. Podrás cambiar tus preferencias más adelante."
- Tarjeta (margen superior 20) con 3 filas: 🌱 **Tu nivel** (valor elegido) · 🎯 **Tu objetivo** (valor elegido) · ⏱️ **Ritmo recomendado** — "10 minutos al día".
- Botón fijo: "Entrar a CodeMentor AI →".

---

## 2. Inicio (pestaña Inicio)

- topline (margen inferior 19): a la izquierda saludo = avatar + bloque con título "Hola, **{nombre}** 👋" (22 sp, peso 800) y "Hoy puedes aprender algo nuevo." (11 sp `Muted`); a la derecha pill amarillo con icono de llama y "{N} días" (racha).
- **Hero card** (degradado `#3F5FD8 → #6A5CE6`, esquina 18, padding 18, círculo decorativo blanco 15 % de 120 dp arriba a la derecha, sin borde): eyebrow `#CBDAFF` "Continúa donde quedaste" · título de la lección/módulo (19 sp, máx. ancho 205) · texto 11 sp `#DCE4FA` (máx. ancho 220) · fila "Tu avance en la lección" / "X de N" · barra con pista blanca 19 % y relleno `#6FDDB4` · botón blanco (texto `#3A63C2`, alto 40, ancho máx. 140) "Seguir aprendiendo →".
- Sección "Tu día en números" con enlace "Ver perfil": dos statbox → ⚡ XP ("puntos de experiencia") y 🎯 "{hechos}/{meta}" ("retos de hoy").
- Sección "Recomendado para ti" con enlace "Preguntar" (va al Tutor): tarjeta con icono ✨ (42×42), título en negrita, texto 10 sp `Muted` y botón secundario `sm` ("Resolver reto" → Retos). Texto de ejemplo: "Practica comparaciones — Un reto de 5 minutos para reforzar lo aprendido." (el criterio real lo define la lógica de la app).

---

## 3. Ruta de aprendizaje (pestaña Aprender)

- topline: bloque con eyebrow "Tu progreso" y título `sm` "Ruta de aprendizaje" · pill mint "{N}% completado".
- sub "Cada concepto te prepara para el siguiente. Avanza sin saltarte las bases." · fila "Progreso general" / "{N}%" · barra de progreso.
- **Lista en línea de tiempo** (margen izquierdo 5, relleno izquierdo 25, línea vertical punteada `#D5DDF0` a 10 dp del borde): cada módulo es una tarjeta (borde `Line`, esquina 15, padding 13/13/12) con un nodo circular de 21 dp a la izquierda (borde 3).
  - **Completado**: nodo `#2FB38A` con borde `#CDF2E3` y "✓" blanco; pill mint "Completado" + "{N} lecciones".
  - **En curso**: nodo `Primary` con borde `#D3E2FC` y halo; tarjeta con borde 1.5 `#8FB0F5` y fondo `#F4F8FF`; pill "En curso" + link "Continuar →".
  - **Bloqueado**: nodo blanco con borde `#D8DFEF`; pill gris (fondo `#EEF1F8`, texto `#7A86A3`) "Bloqueado" + "{N} lecciones".
  - **Próximamente** (el mini proyecto): pill gris "Próximamente" + "Proyecto final".
- Módulos de partida (título — descripción): **Fundamentos** — "Qué es programar y cómo resolver un problema." · **Variables y tipos de datos** — "Guarda y utiliza información." · **Condicionales** — "Haz que tu programa tome decisiones." · **Bucles** — "Repite instrucciones sin escribirlas de nuevo." · **Funciones** — "Organiza tu código en bloques reutilizables." · **Mini proyecto** — "Combina lo aprendido para resolver un caso real." (cantidad de lecciones por módulo: la propone Claude Code y la aprueba el dueño).

---

## 4. Lección (sin barra de pestañas)

- topline: botón atrás (vuelve a la Ruta) · "Lección {X} de {N}" · pill "+20 XP" (el XP real lo define el currículo).
- Barra de progreso de la lección (margen inferior 21).
- eyebrow "Concepto clave · Python" · título (26 sp) · sub explicación.
- **Bloque de código** (margen superior 20): fondo `CodeBg`, esquina 15, padding 15/14, monoespaciada 11 sp, interlineado 1.85, números de línea de 20 dp de ancho, scroll horizontal; colores de sintaxis arriba.
- **Salida esperada**: caja con fondo `#EDFAF4`, "Salida esperada" en negrita y el resultado en monoespaciada (margen superior 12).
- Sección "Comprueba lo aprendido" con pill "Pregunta rápida" + enunciado 13 sp. Opciones (`answer-choice`): ancho completo, borde `Line`, esquina 12, padding 12, 12 sp peso 600, separación 8. Correcta: borde `#2FB38A`, fondo `#EAF9F2`. Incorrecta: borde `#F3A9A6`, fondo `#FEF1F0`. Debajo, caja de retroalimentación (buena/mala).
- Tarjeta azul suave: "¿Necesitas una explicación diferente?" (11 sp negrita, `#3A63C2`) · "Pregunta al tutor para ver el concepto con otro ejemplo." (10 sp) · botón secundario `sm` "Preguntar al tutor IA".
- Botón primario (margen 20): "Ir a practicar →".
- Ejemplo de contenido (lección Condicionales, ya aprobado como base): título "¿Qué es un condicional?" · "Un condicional permite que un programa tome decisiones según si una condición es verdadera o falsa." · código `edad = 20 / if edad >= 18: / print("Eres mayor de edad") / else: / print("Eres menor de edad")` · salida "Eres mayor de edad" · pregunta "Si la variable **edad** vale 16, ¿qué se imprime?" con opciones "Eres mayor de edad" y "Eres menor de edad" (correcta la segunda) · acierto: "¡Exacto! Como 16 es menor que 18, se ejecuta el bloque else." · fallo: "Todavía no. Revisa la condición: 16 >= 18 es falsa, por eso se ejecuta else."

---

## 5. Glitch City (pestaña Retos)

### 5.1 Pantalla principal
- Cabecera pegajosa (sticky, fondo `AppBackground`): pills "◆ {gemas}" (azul), "🔥 {racha}" (amarillo), "{XP} XP" (mint).
- Banner "stg": degradado hero, esquina 16, padding 11/14: "Glitch City" (13 sp negrita) + "Tu título: {título equipado}" (10 sp).
- Fila de 4 botones secundarios `Explorar | Crear | Competir | Títulos` (el activo con fondo `Primary` y texto blanco; alto de botón con padding 10, 11 sp).
- Al final: enlace gris subrayado "Reiniciar mi progreso en Glitch City" → hoja inferior: título "¿Reiniciar Glitch City?" · texto "Se reinician descubrimientos, creaciones, récords, títulos y gemas de los retos. Tu XP se conserva." · botones "Sí, reiniciar" y "Cerrar".
- **Explorar**: texto "Experimenta con los sistemas de la ciudad. No hay vidas ni castigos: cambia valores y mira qué pasa." Tarjetas por laboratorio: icono emoji + título + "{tema} · {n}/{total} descubrimientos" + botón "Entrar".
- **Crear**: texto "Construye reglas con lo que aprendiste, pruébalas y publícalas cuando cumplan los objetivos." Tarjetas: "{tema} · Publicada ✓ / Sin publicar" + botón "Crear" o "Editar".
- **Competir**: texto "Programa al dron con bloques. Menos bloques = más estrellas. Compites contra tus propios récords y los intentos son ilimitados." Camino de nodos circulares 66×66 (sombra inferior sólida 6 dp): hecho `#35C79A`, actual `Primary` (pulso animado, etiqueta "EMPEZAR" sobre el nodo), bloqueado `#DDE4F2`; los niveles especiales (4 y 5) con esquina 22 y color ámbar `#F4B13E` (sombra `#B9731A`) cuando están hechos/actuales. Bajo cada nodo, nombre del nivel y, si hay récord, "Récord: {n} bloques ★★☆". Desplazamiento horizontal en zigzag (0, 46, 70, 46, 0 dp). Tocar un nivel bloqueado → toast "Completa el nivel anterior para desbloquear este."
- **Títulos**: texto "Los títulos se ganan demostrando habilidades. Equipa el que quieras presumir." Tarjeta con filas: nombre + "Ganado: / Cómo ganarlo: {descripción}" y a la derecha pill mint "Equipado", botón-pill "Equipar" o pill "Bloqueado".

Títulos (id, nombre, condición): t0 **Aprendiz de Glitch** (inicial) · t1 **Explorador Curioso** (3 descubrimientos en los laboratorios) · t2 **Arquitecto de Precios** (publicar la Tienda de ofertas) · t3 **Director de NPC** (publicar las reglas del NPC) · t4 **Piloto de Bucles** (completar el nivel 3 del dron) · t5 **Cazabugs** (depurar el dron del nivel especial 4) · t6 **Eficiencia Total** (3 estrellas en 3 niveles) · t7 **Récord Personal** (mejorar uno de tus récords).

### 5.2 Actividad (laboratorio) — sin barra de pestañas propia (mantiene "Retos" activa)
- topline: atrás (vuelve al mapa) · etiqueta "Explorar"/"Crear" · (en el borrador aquí había un pill "Sin vidas": **se elimina**).
- Título `sm` "{emoji} {nombre}" · sub: Explorar "Sin vidas ni castigos: cambia los valores y observa qué pasa." / Crear "Ajusta las reglas hasta cumplir todos los objetivos y publica tu creación."
- Controles por parámetro: slider (con etiqueta "{nombre}: **{valor}**"), selector desplegable, o campo de texto.
- Bloque de código en vivo (monoespaciado, fondo `CodeBg`) + caja de resultado (fondo `#E4F7EE`, esquina 16, padding 14, 12 sp).
- Sección "Descubrimientos" (Explorar) / "Objetivos" (Crear): filas con ✅ (cumplido) o ⬜ y el texto.
- Solo en Crear: botón "Publicar creación" (deshabilitado hasta cumplir todos los objetivos; si ya estaba publicada dice "Actualizar creación").

**Laboratorios de Explorar** (cada descubrimiento nuevo da XP y gemas; la cantidad la define el motor de reglas):
1. 🚦 **Semáforo inteligente** — "Variables y condicionales". Parámetro: Segundos restantes (0–60, inicia 30). Código: `tiempo = {t}` / `if tiempo > 40: luz = "verde"` / `elif tiempo > 10: luz = "amarilla"` / `else: luz = "roja"`. Resultado: punto de color + "Luz {verde|amarilla|roja}" (verde `#35C79A`, amarilla `#F4B13E`, roja `#E5646A`). Descubrimientos: "Deja el semáforo en rojo" · "Consigue luz verde".
2. 🛒 **Cajero de la tienda** — "Operadores y condicionales". Parámetros: Precio unitario (1–50, inicia 20), Cantidad (1–10, inicia 2), Descuento por 5 o más (%) (0–50, inicia 10). Regla: total = precio × cantidad; si cantidad ≥ 5, se aplica el descuento. Código: `total = {p} * {q}` / `if {q} >= 5:` / `    total = total * (1 - {d}/100)`. Resultado "Total: {t}". Descubrimientos: "Paga menos de 40 comprando 5 o más" · "Comprueba que con menos de 5 no hay descuento" (cantidad < 5, descuento > 0 y total = precio × cantidad).
3. 🏢 **Ascensor de la torre** — "Ciclos". Parámetros: Último piso (1–12, inicia 6), Saltar de a (1–3, inicia 1). Código: `for piso in range(1, {n+1}, {s}):` / `    visitar(piso)`. Resultado "Pisos: 1, 2, … ({k} vueltas)". Descubrimientos: "Haz que el ascensor se detenga en 5 pisos" · "Salta de 3 en 3 hasta un piso 10 o más".
4. 🤖 **Robot repartidor** — "Arreglos y funciones". Parámetros: Paquetes (texto separado por comas, inicia "4,8,2,7", máx. 12 números válidos), Función (mayor / suma / ordenar, inicia mayor). Código: `paquetes = [{lista}]` / `resultado = {función}(paquetes)`. Resultado "Resultado: …" ("La lista está vacía" si no hay números). Descubrimientos: "Prueba las tres funciones del robot" · "Usa una lista de 6 o más paquetes".

**Laboratorios de Crear** (se publican al cumplir todos los objetivos):
1. 🏷️ **Tienda de ofertas** — "Condicionales y funciones". Parámetros: Descuento desde (compra mínima) (10–100, paso 10, inicia 100), Porcentaje de descuento (5–50, paso 5, inicia 5). Código: `def precio_final(compra):` / `    if compra >= {u}:` / `        return compra * (1 - {d}/100)` / `    return compra`. Resultado: líneas "compra 30 → paga …", "compra 60 → paga …", "compra 90 → paga …". Objetivos: "La compra de 30 no recibe descuento" · "La compra de 60 recibe descuento" · "La compra de 90 cuesta 72 o menos".
2. 🧙 **Reglas para un NPC** — "Condicionales". Parámetros: Huye si el jugador está a ≤ (0–5, inicia 0), Saluda si está a ≤ (1–9, inicia 1), Prueba: distancia del jugador (0–10, inicia 5). Código: `if distancia <= {h}: accion = "huir"` / `elif distancia <= {s}: accion = "saludar"` / `else: accion = "pasear"`. Resultado: fila de 11 casillas (🧙 en 0, 🧑 en la distancia de prueba, "·" en el resto) + "Con distancia {x}: {acción}". Objetivos: "Huye cuando el jugador está a 1 de distancia" · "Saluda cuando está a 4" · "Pasea cuando está a 8 o más".

### 5.3 Actividad (nivel del dron)
- topline: atrás · "Competir · Nivel {n}".
- Título `sm` (nombre del nivel) · sub "Lleva el dron 🚁 hasta el paquete 📦. Un bloque con ×n repite su movimiento; tú compites contra tu récord." (+ " Máximo {n} bloques." si aplica).
- **Cuadrícula 5×5** (separación 4, celdas cuadradas, esquina 10): libre `#EEF1F8`, muro `#1F2A44`, camino recorrido `#C5D6F7`, choque `#F3A9A6`; 🚁 en la posición actual, 📦 en la meta.
- Fila "Repetir:" con botones ×1 ×2 ×3 ×4 (el activo en `Primary` con texto blanco).
- Paleta de movimientos (botones secundarios en fila que envuelve): ↑ ↓ ← → →↓ ↓→ (los dos últimos son bloques de dos pasos).
- Sección "Tu programa ({n} bloques)" con una pill por bloque (flechas + "×n" si n>1; máx. 12 bloques) o el texto "Añade bloques con los botones de arriba.".
- Botones: "▶ Ejecutar", "Borrar último", "Limpiar", "Idea" (muestra la pista en caja ámbar "💡 …").
- Retroalimentación: vacío → "Tu programa está vacío: añade algunos bloques." · se sale → "El dron se sale del mapa. Revisa el último bloque." · muro → "El dron choca con un muro. Revisa el último bloque." · otra casilla → "El dron termina en otra casilla. Ajusta tu programa y prueba otra vez: no pasa nada." · pasa el máximo → "Llegaste, pero con {n} bloques. Este reto permite máximo {m}." · éxito → "¡Entrega lograda con {n} bloques! ★★☆" + (nuevo récord / "Tu récord es {n}: ¿puedes mejorarlo?").
- Si hay récord: "Récord personal: {n} bloques ★★☆".
- Estrellas: ≤ par[0] bloques = 3, ≤ par[1] = 2, más = 1.
- Movimiento: el dron ejecuta cada bloque n veces, paso a paso; se detiene al llegar a la meta; choca con muros (#) y se sale del mapa (error aparte).

**Niveles** (S = salida, G = meta, # = muro, . = libre; `par` = [3 estrellas, 2 estrellas]):
1. **Primer vuelo** — `S....` `.....` `.....` `.....` `....G` · par [2,4] · idea: "Un bloque con ×4 repite el movimiento 4 veces."
2. **Callejón con muros** — `S#...` `.#.#.` `.....` `.###.` `#...G` · par [3,5] · idea: "Baja por la izquierda y rodea los muros."
3. **Escalera de edificios** — `S.###` `#..##` `##..#` `###..` `####G` · par [2,4] · idea: "Prueba el bloque →↓ repetido varias veces."
4. **Especial: depura el dron** — `S....` `.###.` `.#...` `.#.#.` `...#G` · par [2,4] · el programa viene precargado: →×4, ↓×2, →×2, ↓×2 (hay que quitar lo que sobra) · idea: "Tras bajar 2, el dron se sale por la derecha. Quita lo que sobra."
5. **Especial: reto relámpago** — `S.#..` `..#.#` `.##..` `...#.` `#...G` · par [4,6] · máximo 6 bloques · idea: "Baja, avanza, baja y avanza otra vez."
Cada nivel se desbloquea al completar el anterior.

---

## 6. Logros (accesible desde Perfil; mantiene la pestaña Perfil activa)

- topline: atrás · título `sm` "Mis logros" · pill mint "{obtenidos} / {total}".
- sub "Logros por comprender, depurar y mejorar. Cada uno tiene niveles de dominio."
- Tarjeta resumen: "**{n} de {total} niveles de dominio**" + "Premian comprender, depurar y mejorar, no solo acertar ni acumular XP." (11 sp). Si no hay logros: estado vacío "Aún no tienes logros. Resuelve un reto sin ayuda o explica por qué funciona una solución."
- Por categoría: heading + grid de 2 columnas (separación 10) de tarjetas: medalla 44×44 (esquina 15, fondo `#FDF0D2`, emoji 22 sp), nombre (11 sp negrita), descripción (9 sp `Muted`), barra de progreso 6 dp, "{v} / {siguiente} para {Bronce|Plata|Oro}" o "Nivel máximo", pill ("Bloqueado" o "{Nivel} · {x}/{n}" en mint) y "Recompensa: +{n} ◆" u "obtenida". Los bloqueados con opacidad 78 %.
- Botón "Seguir practicando" (→ Retos) al final.

Logros (categoría · nombre · descripción · umbrales Bronce/Plata/Oro · gemas por nivel · icono):
1. Comprensión · **Explica el porqué** · "Responde bien la pregunta “¿por qué funciona?” después de resolver un reto." · 1/3/5 · 10/25/50 · 🧠
2. Resolución de problemas · **Solución autónoma** · "Resuelve retos de la etapa 2 o superior sin pistas y sin errores." · 1/3/6 · 15/30/60 · 🧩
3. Pensamiento algorítmico · **Estratega** · "Elige la estrategia adecuada al primer intento en retos de estrategia." · 1/2/3 · 15/30/60 · ♟️
4. Depuración · **Cazador de errores** · "Encuentra el error en retos de depuración." · 1/2 · 15/30 · 🐛
5. Depuración · **Casos límite** · "Resuelve retos que dependen de un caso límite." · 1/2 · 15/30 · 🎯
6. Eficiencia · **Medidor de costo** · "Resuelve retos de complejidad temporal y espacial." · 1/2 · 20/40 · ⏱️
7. Constancia y progreso · **Aprender del error** · "Acierta un reto después de haber fallado antes." · 1/3/5 · 10/25/40 · 🔁
8. Constancia y progreso · **Subir de etapa** · "Completa etapas del mapa." · 1/3/6 · 20/40/80 · 🚩
9. Constancia y progreso · **Hábito** · "Practica varios días seguidos (vale menos que el dominio)." · 3/7/14 días · 5/10/15 · 🔥
- El logro del borrador "Repaso activo (recuperar corazones)" **se elimina** (no hay vidas).
- Varios logros dependen de tipos de reto que aún no existen (estrategia, complejidad, caso límite); **Claude Code debe proponer cómo se miden o cuáles se difieren**, sin inventar.

---

## 7. Perfil (pestaña Perfil)

- topline: título `sm` "Mi perfil" · botón cuadrado 36×36 con ⚙ (abre Configuración).
- Héroe centrado (padding 14/0/20): avatar 76×76, nombre (20 sp, peso 800), "Nivel {n} · {título de rango}" (11 sp `Muted`), pill "✦ Aprendiz constante" (margen 12; el texto del pill lo define la lógica).
- Tarjeta "Tu experiencia": "Tu experiencia" (12 sp negrita) + pill amarillo "{XP} XP"; fila "{X} XP para el siguiente nivel" / "{N}%"; barra. (El borrador subía de nivel cada 2000 XP; la regla real la fija el motor de reglas.)
- "Tu actividad": dos statbox → 🏅 "{n}" "logros obtenidos" · 🎯 "{n}" "reto completado/retos completados".
- "Objetivo de aprendizaje": tarjeta con fila 🎯 **Meta diaria** ("Define cuántos retos quieres completar al día."), etiqueta "RETOS POR DÍA" (10 sp, peso 800, mayúsculas) y selector: "1 reto · Ritmo tranquilo", "2 retos · Ritmo recomendado", "3 retos · Ritmo constante", "5 retos · Reto intensivo"; nota "Puedes cambiar tu objetivo cuando quieras; lo importante es mantener un ritmo sostenible." (10 sp).
- "Cuenta" (al fondo): tarjeta con 3 filas con chevron: 🏆 **Mis logros** ("Insignias y metas alcanzadas") · ⚙️ **Configuración y accesibilidad** ("Visualización, audio y preferencias") · ↪ **Cerrar sesión** (título y icono en rojo `#C24A4A`, fondo del icono `#FDECEC`, "Volver a la pantalla inicial").

---

## 8. Configuración (sin barra de pestañas)

- topline: atrás · título `sm` "Configuración". eyebrow "Personaliza tu experiencia" · sub "Organiza las preferencias por tipo: accesibilidad, avisos y privacidad."
- **Accesibilidad visual** (tarjeta): 🔎 **Tamaño del texto** ("Aumenta la lectura en todas las pantallas.") con selector [Estándar · Texto grande · Texto muy grande]; ◐ **Alto contraste** ("Refuerza la diferencia entre texto y fondo.") interruptor; ✨ **Reducir animaciones** ("Reduce movimientos y transiciones visuales.") interruptor.
- **Audio y lectura**: 💬 **Mostrar subtítulos** ("Preferir texto cuando haya contenido de audio o video.") interruptor, activado por defecto; 🔊 **Indicaciones sonoras** ("Usar señales de audio además de las visuales cuando estén disponibles.") interruptor; nota "Estas preferencias son demostrativas; el lector de pantalla y los controles del sistema operativo se configuran por separado." (ajustar el texto si la lógica cambia).
- **Notificaciones**: 🔔 **Recordatorios** ("Recordarte cuándo practicar.") interruptor, activado por defecto.
- **Datos y privacidad**: texto "Esta maqueta guarda el estado de la cuenta y el progreso en el almacenamiento local de tu navegador…" → **debe reescribirse**: ahora los datos se guardan en la cuenta del usuario en Firebase; Claude Code propone el texto. Botón ghost `sm` "Restablecer datos" (con confirmación).
- Enlace centrado "Cerrar sesión".
- **Interruptor (toggle)**: 40×23, esquina total, apagado `#D5DCEC`, encendido `Primary`; perilla blanca de 17 dp que se desplaza 17 dp.

---

## 9. Tutor IA (pestaña Tutor IA) — solo interfaz, sin IA real

- topline: bloque con eyebrow "Siempre puedes preguntar" y título `sm` "Tutor IA" · pill "Demo".
- Cabecera (degradado hero, esquina 18, padding 17): cuadro 41×41 (esquina 14, blanco 13 %) con "✦" 22 sp · título "¡Vamos a resolverlo juntos!" (17 sp) · texto "Pregunta con tus propias palabras. Aprenderás mejor si entiendes el porqué." (10 sp, `#DCE7FF`).
- Burbuja del tutor (fondo blanco, borde `Line`, esquina 14 con la esquina superior izquierda en 5, 11 sp, interlineado 1.6, ancho máx. 94 %): "¡Hola! Soy tu tutor de demostración. Puedes probar una pregunta sugerida y ver cómo aparecería la explicación aquí." Burbuja del usuario: fondo `#E1EAFD`, texto `#2F4CA8`, alineada a la derecha, esquina superior derecha en 5.
- "PRUEBA CON UNA PREGUNTA" (10 sp, peso 800) + chips (borde `#CFDCF8`, fondo `#F5F8FF`, texto `#3A63C2`, esquina total, 10 sp): "¿Qué es un if?" · "if vs. else" (envía "¿Cuál es la diferencia entre if y else?") · "¿Qué es una variable?" (envía "Explícame las variables con un ejemplo").
- Historial del chat y campo "TU PREGUNTA" con input (borde `Line`, esquina 12, padding 12, 11 sp, placeholder "Escribe tu duda...") y botón cuadrado 42 dp `Primary` con "➤".
- Aviso: "Demostración visual: las respuestas son ejemplos predefinidos. Esta versión no está conectada a un servicio de IA."
- Respuestas de ejemplo (por palabra clave): **else/sino/diferencia** → "if evalúa una condición. else define qué hacer cuando esa condición resulta falsa. Por eso normalmente va después de un bloque if." · **if/condicional** → "Un if permite ejecutar un bloque solo cuando una condición es verdadera. Ejemplo: if edad >= 18: print("Mayor de edad"). Si la condición es falsa, puedes usar else para indicar qué hacer en el otro caso." · **variable** → "Una variable es un nombre asociado a un dato. En Python, edad = 20 guarda el valor 20 bajo el nombre edad. Luego puedes usarlo en operaciones y condiciones." · **bucle/for/while** → "Un bucle repite instrucciones. Por ejemplo, for numero in range(3): print(numero) muestra 0, 1 y 2. Es útil cuando no quieres escribir la misma instrucción muchas veces." · por defecto → "Esta vista demuestra cómo se mostraría una respuesta paso a paso. En la versión final, el tutor podría conectarse a un modelo de IA para resolver tu duda con ejemplos personalizados."

---

## 10. Reglas del borrador que sirven de punto de partida para el motor (se revisan en la Fase 12, no se copian a ciegas)

- Cada **descubrimiento** nuevo en Explorar: +10 XP y +2 gemas. Completar todos los descubrimientos de un laboratorio lo marca como completado.
- **Publicar** una creación por primera vez: +30 XP, +5 gemas. Actualizarla no da recompensa.
- **Completar un nivel del dron** por primera vez: +(20 + 10×índice del nivel) XP y +3 gemas; mejorar un récord ya existente: +5 XP y cuenta para el título "Récord Personal".
- **Lección**: pregunta rápida acertada, +20 XP solo la primera vez.
- Niveles de usuario: el borrador usaba 1 nivel cada 2000 XP con rangos Aprendiz / Explorador / Constructor / Experto ("Nivel {n} · {rango} de código").
- **Racha**: un día cuenta cuando se completa al menos una actividad; días consecutivos suman, un día saltado reinicia a 1.
- **Retos de hoy** = actividades completadas hoy (se reinicia cada día), comparadas con la meta diaria (1/2/3/5, por defecto 2).
- Recompensas por logro: se suman las gemas de cada nivel nuevo alcanzado (valores en la tabla de logros).
