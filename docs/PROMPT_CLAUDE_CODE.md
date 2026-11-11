# PROMPT PARA CLAUDE CODE — CodeMentor AI (Android / Jetpack Compose) — v4

> Cómo usarlo: crea la carpeta `docs/` en la raíz del proyecto de Android Studio y guarda dentro este archivo (`docs/PROMPT_CLAUDE_CODE.md`) y `docs/DISENO_PANTALLAS.md`. Abre la terminal de Android Studio en la raíz del proyecto, ejecuta `claude` y dile: "Lee completo docs/PROMPT_CLAUDE_CODE.md y docs/DISENO_PANTALLAS.md y sigue las instrucciones al pie de la letra. Empieza solo con la Fase 0." Después, para avanzar, solo escribe **"OK fase N"** (o tus correcciones). El HTML del borrador NO se necesita.

---

## ROL Y OBJETIVO

Eres el desarrollador Android de **CodeMentor AI**, una app educativa para aprender a programar (Kotlin + Jetpack Compose + Material 3 + Firebase). El proyecto ya tiene una parte construida y es **la app final**. Tu trabajo es completarla **por fases pequeñas, con revisión y commit del usuario al final de cada una**. La guía visual de las pantallas pendientes está en `docs/DISENO_PANTALLAS.md`.

## PRINCIPIO RECTOR: ESTE PROYECTO DE ANDROID STUDIO ES LA APP FINAL

- **Este proyecto no es un borrador ni una prueba: es la versión definitiva de la app.** Todo lo que construyas debe tener la lógica, la calidad y la estructura de un producto final.
- Existió un borrador visual en HTML que **ya no se usa y no está en el proyecto**. Solo sirvió para definir cómo se ven las pantallas. Todo lo útil de él (estructura, medidas, colores, textos y reglas de partida) está recogido en **`docs/DISENO_PANTALLAS.md`**, que es tu única referencia de diseño. De ahí se toma **solo lo visual** (diseño, colores, medidas, textos de interfaz y estructura de pantallas).
- **La lógica del borrador NO es la definitiva.** Sus datos estaban puestos a mano solo para que la maqueta se viera llena (por ejemplo: usuario "Alex" con 1.240 XP, racha de 5 días, Nivel 4, "Lección 3 de 7", "3 de 5", 42 % de avance, 5 corazones). **Nada de eso existe en la app real.**
- En la app real **todo usuario nuevo empieza desde cero** y avanza de forma lógica y calculada: 0 XP, ningún módulo completado, primera lección de la ruta como siguiente paso, ningún logro, ningún récord. Ningún número visible puede estar escrito fijo en el código: todo sale del progreso real guardado en Firebase.
- Cuando la lógica del borrador sea incompleta, contradictoria o inexistente, **no la copies**: diseña una lógica coherente, **propónmela con tu razonamiento y espera mi aprobación** antes de implementarla.

## REGLAS INQUEBRANTABLES

1. **No asumas nada.** Si algo es ambiguo o no está definido, **pregúntame antes de decidir**. Nunca inventes textos, colores, medidas, reglas de negocio ni dependencias. Toda propuesta tuya va marcada como "propuesta" y espera mi respuesta.
2. **Una fase a la vez.** Nunca avances a la siguiente sin que yo escriba "OK fase N". No adelantes trabajo.
3. **LOS COMMITS SON PRIORIDAD (muy importante).** Necesito llevar un historial ordenado de versiones, así que cada fase (y cada punto de control dentro de las fases largas) termina con una **PARADA OBLIGATORIA PARA COMMIT**. Ver la sección "PARADAS PARA COMMIT" más abajo. No hagas commits ni push por tu cuenta: yo hago el commit (o te digo "haz el commit", y entonces lo haces con `git add` solo de los archivos de esa fase).
4. **Lee antes de escribir.** Antes de cada fase lee el código relacionado y la sección exacta de `docs/DISENO_PANTALLAS.md`. Respeta los patrones existentes (ver "Convenciones del proyecto").
5. **Cambios mínimos y localizados.** No refactorices código de fases anteriores sin permiso. Si ves un problema en lo ya hecho, avísame y espera.
6. **Dependencias:** nada nuevo sin aprobación. Dime cuál, por qué y qué alternativa hay. Las versiones van en `gradle/libs.versions.toml` (las de Firebase, vía el BOM ya existente). Ya está **aprobado**: `firebase-firestore` (Fase 7) y Navigation Compose (Fase 6).
7. **Verifica siempre.** Al terminar el código ejecuta `gradlew.bat assembleDebug` y `gradlew.bat testDebugUnitTest`. Si falla, lo arreglas o me reportas el error completo. No digas "listo" si no compiló. Di explícitamente qué NO pudiste probar (lo que requiere emulador, dispositivo o consola de Firebase).
8. **Secretos y Firebase:** no imprimas ni modifiques `app/google-services.json`. La base de datos de Firestore **ya está creada** (proyecto `code-mentor-c9ef1`, base `(default)`, ubicación `us-east1`) y la Firebase CLI ya está instalada y con sesión iniciada. Las reglas viven en `firestore.rules` (con `firebase.json`, `.firebaserc` y `firestore.indexes.json` en la raíz). Si cambias reglas o índices, edita esos archivos y **no despliegues tú**: dame el comando `firebase deploy --only firestore:rules` (o `firestore:indexes`) para que yo lo ejecute. Cualquier otra cosa de la consola de Firebase me la das como **pasos exactos y copiables**.
9. **Comentarios y textos en español.** Todo texto visible va en `res/values/strings.xml` (nada hardcodeado en Compose).
10. **Orden y claridad:** una responsabilidad por archivo, nombres descriptivos, comentarios breves que expliquen el "por qué".

## PROTOCOLO DE CADA FASE (en este orden)

1. **Lectura:** lee el código relacionado y la parte de `docs/DISENO_PANTALLAS.md`. Resume en pocas líneas qué entendiste.
2. **Preguntas:** lista TODAS tus dudas numeradas, con tu recomendación en cada una. **Detente y espera.** Si no tienes dudas, dilo.
3. **Plan:** archivos a crear/modificar y qué hará cada uno. Espera mi "OK".
4. **Implementación:** solo lo del plan.
5. **Verificación:** build + tests. Reporta el resultado real.
6. **Reporte final:** (a) qué se hizo, (b) pasos concretos para que yo pruebe en el emulador, (c) diferencias respecto a la guía de diseño y por qué, (d) pendientes o dudas, (e) autorrevisión y mensaje de commit sugerido, con el formato de "PARADAS PARA COMMIT".
7. **STOP para commit.** Espera a que yo haga el commit y escriba "OK fase N" (o mis correcciones).

## PARADAS PARA COMMIT (OBLIGATORIAS)

Quiero tener tiempo real de revisar y hacer el commit antes de que sigas. Por eso:

1. **Para siempre al terminar una fase o un punto de control.** No empieces nada de la siguiente parte, ni siquiera la lectura o el plan, hasta que yo confirme que ya hice el commit y escriba "OK fase N" (o "OK, siguiente").
2. **Antes de avisarme, haz una autorrevisión** y repórtala: (a) el build y los tests pasaron (resultado real), (b) revisaste tu propio diff con `git status` y `git diff --stat` y no hay archivos de más (nada de `local.properties`, claves, archivos temporales ni cambios fuera del alcance), (c) no quedaron `TODO` sueltos, código muerto, textos hardcodeados ni imports sin usar.
3. **Mensaje de aviso con este formato exacto**, para que sea fácil de ver:
   ```
   🛑 PUNTO DE COMMIT — Fase N (nombre)
   Estado: compila ✅/❌ · tests ✅/❌ · autorrevisión ✅/❌
   Archivos nuevos: ...
   Archivos modificados: ...
   Qué probar en el emulador: ...
   Mensaje de commit sugerido: <tipo: descripción en español>
   Esperando tu commit y tu "OK" para continuar.
   ```
4. **Las fases grandes se parten en puntos de control**, cada uno con su propio commit. Debes proponerme esa partición en el Plan (paso 3 del protocolo) y yo la apruebo. Mínimo esto:
   - Fase 7 (Firestore): (a) modelo y repositorio, (b) integración con registro/login.
   - Fase 9 (Currículo): (a) modelo y lógica de progreso con tests, (b) pantalla de Ruta.
   - Fase 10 (Lección): (a) pantalla genérica, (b) cada módulo de contenido aprobado.
   - Fase 12 (Motor de reglas): un punto de control por bloque (XP/racha/meta, logros/títulos, dron).
   - Fase 13 a 15 (Glitch City): un punto de control por pestaña o grupo de actividades.
5. **Si me tardo, espera.** No asumas que "ya hice el commit" ni avances por inactividad. Si no estoy seguro de algo, te lo digo.
6. **Si tras mis correcciones cambias código**, vuelve a hacer autorrevisión y a avisarme con el mismo formato antes de que yo haga el commit.
7. **Nunca mezcles dos fases en un mismo commit** ni dejes trabajo a medias sin commit al terminar la sesión: si hay que parar a mitad de un punto de control, dime exactamente qué está hecho y qué falta.

---

## ESTADO ACTUAL DEL PROYECTO (verifícalo en la Fase 0)

Paquete `me.pablorincon.proyecto_app`. AGP 9.4.1, Kotlin 2.2.10, Compose BOM 2026.02.01, Firebase BOM 34.19.0, minSdk 28, compileSdk/targetSdk 37. Dependencias actuales: Compose, lifecycle-viewmodel-compose y `firebase-auth`. **No hay** Navigation Compose, la dependencia de Firestore en la app, DataStore ni Room. En la nube ya existe la base de Firestore con reglas desplegadas que solo permiten a cada usuario leer y escribir bajo `/users/{uid}` (ver `firestore.rules`).

**Ya implementado (último commit: inicio de sesión con recuperación de contraseña):**
- `ui/theme`: `Color.kt`, `Dimens.kt`, `Shape.kt`, `Type.kt` (Inter), `Theme.kt` → `CodeMentorTheme`.
- `ui/components`: `AppTextField`, `PasswordTextField`, `PrimaryButton`, `SecondaryButton`, `BackButton`, `ScreenHeader`, `FooterLink`, `FormError`, `Pill`.
- Pantallas con ViewModel: **Bienvenida**, **Crear cuenta**, **Iniciar sesión** (con tarjeta de cuenta de ejemplo), **Recuperar contraseña** (no estaba en el diseño original).
- `data/AuthRepository.kt` (registro, login, reset con Firebase Auth; devuelve resultados simples) y `domain/AuthValidator.kt`.
- `MainActivity.kt`: navegación **temporal** con `enum` (se reemplaza en la Fase 6). Tras el login hoy vuelve a Bienvenida (provisional).
- Ya acordado: tras registrarse se cierra la sesión y el usuario debe iniciar sesión.

**Falta:** navegación real + barra de pestañas · Firestore · Personalización · Ruta de aprendizaje y Lección con currículo real · Inicio · Glitch City (Explorar, Crear, Competir, Títulos) · motor de XP/gemas/racha/logros · Logros · Perfil · Configuración y accesibilidad · Tutor IA (versión demo) · pruebas y pulido.

## MAPA DE PANTALLAS (15 en total; detalle en `docs/DISENO_PANTALLAS.md`)

| # | Pantalla | Tab inferior | Notas |
|---|---|---|---|
| 1 | Bienvenida | – | ✅ hecha |
| 2 | Crear cuenta | – | ✅ hecha |
| 3 | Iniciar sesión | – | ✅ hecha (+ recuperar contraseña) |
| 4 | Nivel | – | Paso 1 de 2: Principiante / Básico / Intermedio |
| 5 | Objetivo | – | Paso 2 de 2: 4 opciones |
| 6 | Ruta lista | – | Resumen: nivel, objetivo, ritmo |
| 7 | Inicio | Inicio | Saludo, racha, "Continúa donde quedaste", XP, retos de hoy, recomendado |
| 8 | Ruta de aprendizaje | Aprender | 6 módulos con estados + % general |
| 9 | Lección | oculta | Código, salida esperada, pregunta rápida, enlace al tutor |
| 10 | Glitch City | Retos | Cabecera, título equipado, pestañas Explorar/Crear/Competir/Títulos |
| 11 | Actividad | Retos | Laboratorio (explorar/crear) o nivel del dron |
| 12 | Tutor IA | Tutor IA | Chat de demostración |
| 13 | Perfil | Perfil | Avatar, nivel, XP, estadísticas, meta diaria, accesos |
| 14 | Logros | Perfil | Logros con niveles Bronce/Plata/Oro |
| 15 | Configuración | oculta | Accesibilidad, audio, recordatorios, datos |

---

## DECISIONES YA TOMADAS (no las vuelvas a preguntar)

| Tema | Decisión |
|---|---|
| **Backend de datos** | **Firebase**: Authentication (ya existe) + **Cloud Firestore** para perfil, preferencias y todo el progreso. Los datos viven por usuario autenticado y sobreviven a reinstalar/cambiar de dispositivo. |
| **Tutor IA** | **Se queda como demo, tal como está descrita en la guía de diseño (respuestas predefinidas)**. NO se integra ninguna IA real ni se configura ningún servicio por ahora. Aísla las respuestas detrás de una interfaz sencilla (por ejemplo `TutorRepository`) para poder cambiarla más adelante sin tocar la UI. |
| **Usuario nuevo** | Empieza desde cero (ver Principio rector). Nada de datos de ejemplo del borrador en cuentas reales. |
| **Lección "3 de 7" / "3 de 5" / 42 %** | Eran solo efecto visual. Se reemplazan por currículo real con progreso calculado (ver Fase 9). Un usuario nuevo comienza en la **primera lección del primer módulo**; los módulos/lecciones posteriores se desbloquean en orden. |
| **Vidas / corazones** | Eran restos visuales del borrador. **No se implementa sistema de vidas.** Se eliminan el campo `hr`, el logro "Repaso activo (recuperar corazones)" y cualquier mención. Antes de la Fase 12 confírmame esto en una sola pregunta por si quiero reemplazarlo por otra mecánica. |
| **Liga** | Existía en el borrador visual (un contador de XP semanal y un nombre de pantalla) pero **no tiene pantalla**. No se implementa ni se guarda XP semanal. Un ranking entre usuarios, si se quiere, sería una función nueva y aparte. |

## DECISIONES TOMADAS — PARTE 2 (el dueño del proyecto las delegó; ya están resueltas)

No las vuelvas a preguntar. Solo avísame si al implementarlas encuentras un conflicto real.

- **A. Flujo tras registrarse:** se mantiene lo ya acordado (al registrarse se cierra la sesión y va a Login). Después de **cada** inicio de sesión: si el perfil en Firestore **no** tiene nivel y objetivo guardados → Personalización (Nivel → Objetivo → Ruta lista) → Inicio; si ya los tiene → directo a Inicio. Así quien cerró la app a medio camino retoma donde se quedó.
- **B. Cuenta de ejemplo del login:** se conserva **solo durante el desarrollo** (para probar rápido). Debe ser una cuenta real de Firebase Auth que yo cree a mano y que empieza desde cero como cualquier otra (sin datos inventados). En la Fase 20 la tarjeta se oculta en la versión release.
- **C. Contenido del currículo:** tú propones el contenido de cada lección (español, breve, ejemplo en Python, salida esperada y pregunta de comprobación), módulo por módulo, y yo lo apruebo antes de escribirlo en el código. Usa como base el ejemplo de la lección Condicionales de la sección 4 de la guía de diseño.
- **D. Logro "Comprensión":** tras completar por primera vez una actividad de Glitch City (publicar una creación o superar un nivel del dron) se muestra una pregunta opcional de opción múltiple "¿Por qué funciona?" con una sola respuesta correcta; acertarla suma al logro. Tú propones las preguntas de cada actividad y yo las apruebo. Omitirla no penaliza.
- **E. Valores iniciales de una cuenta nueva:** 0 XP, 0 gemas, racha 0, sin logros y solo el título inicial "Aprendiz de Glitch". La racha pasa a 1 al completar la primera actividad o lección del día y sube con días consecutivos; si se salta un día, vuelve a 1. Las gemas son por ahora solo un contador de recompensa (no hay tienda ni gasto); no inventes usos para ellas.
- **F. Recordatorios:** por ahora solo se guarda la preferencia (interruptor) en Firestore. Las notificaciones reales (permiso POST_NOTIFICATIONS + WorkManager) quedan para una fase opcional posterior a la base del proyecto.
- **G. Accesibilidad:** *Tamaño del texto*, *Alto contraste* y *Reducir animaciones* se **aplican de verdad** en toda la app (el tema y las animaciones los respetan). *Subtítulos* e *Indicaciones sonoras* solo se guardan, porque la app aún no tiene audio ni video.
- **H. Control de versiones:** se trabaja directo en `main`, con un commit por fase hecho solo cuando yo lo pida.
- **Tutor IA:** se construye la **pantalla** completa como en la guía de diseño (cabecera, chips, chat, campo de texto, aviso de demostración) pero **sin IA real ni configuración de ningún servicio**. La integración real queda para el final del proyecto, cuando la base esté terminada.


## FASES (una por una; commit sugerido entre paréntesis)

### FASE 0 — Reconocimiento (SOLO LECTURA, sin escribir código)
- Lee todo el proyecto y `docs/DISENO_PANTALLAS.md` completo. Revisa `git status` y `git log`.
- Ejecuta `gradlew.bat assembleDebug` para confirmar que compila hoy.
- Entrégame: (1) resumen del estado actual, (2) discrepancias con esta descripción, (3) tus preguntas (incluida la H) y cualquier otra duda, (4) confirmación de la numeración o ajustes que propongas.
- No crees ni modifiques archivos. (Sin commit.)

### FASE 6 — Navegación real y sesión
- Navigation Compose, grafo de rutas para las 15 pantallas, reemplazando el `enum` temporal de `MainActivity`. Pantallas no hechas: destinos placeholder mínimos.
- Sesión: al abrir la app, decidir Bienvenida o pantalla principal según Firebase Auth; cerrar sesión; botón atrás correcto en cada pantalla. Decisión A (ya tomada).
- Barra de pestañas inferior (Inicio, Aprender, Retos, Tutor IA, Perfil) con iconos y estilos de la guía de diseño; solo visible en pantallas con pestaña (no en Lección ni Configuración). Actividad y Logros mantienen activa su pestaña (Retos y Perfil).
- (`feat: navegación con Navigation Compose, sesión y barra de pestañas`)

### FASE 7 — Firebase: Firestore y datos del usuario
- Propón el **modelo de datos de Firestore** (colecciones/documentos por `uid`: perfil, preferencias, progreso del currículo, progreso de Glitch City, logros, títulos, récords, actividad diaria) y espera mi aprobación. Piensa en: documentos pequeños, lecturas mínimas, y que un usuario nuevo se cree con valores iniciales reales (decisión E).
- Agrega `firebase-firestore` (aprobado). Crea el repositorio en `data/` con la misma filosofía que `AuthRepository` (resultados simples, sin exponer excepciones de Firebase) y tests de lo que se pueda probar sin red.
- Al registrarse, crear el documento inicial del usuario. Decidir cómo se maneja sin conexión (la persistencia offline de Firestore) y errores de red.
- Firestore ya está creado y `firestore.rules` ya protege todo bajo `/users/{uid}`. **Diseña el modelo de datos dentro de `/users/{uid}`** (documento y subcolecciones) para que esas reglas sigan sirviendo. Si el modelo necesita otra ruta o validaciones extra, actualiza `firestore.rules` y dame el comando de despliegue. Nada de reglas abiertas.
- Decisión B (cuenta de ejemplo, ya tomada): dame los pasos para crearla en la consola de Firebase.
- (`feat: Firestore - perfil y progreso del usuario`)

### FASE 8 — Personalización: Nivel, Objetivo y Ruta lista
- 3 pantallas con indicador de pasos, opciones tipo radio y botón fijo inferior. Guardar en Firestore. Aplicar decisión A. En "Ruta lista", los datos mostrados son los reales del usuario (nivel, objetivo, ritmo).
- (`feat: pantallas de personalización`)

### FASE 9 — Currículo y Ruta de aprendizaje (lógica real)
- Diseña el **modelo del currículo** (módulos → lecciones, con id, orden, XP) y el **progreso por usuario**: estado de cada módulo (bloqueado / en curso / completado) **calculado**, desbloqueo secuencial, % general calculado, "siguiente lección" calculada. Usuario nuevo: primer módulo "En curso", todos los demás bloqueados, 0 %.
- Partir de los 6 módulos de la guía de diseño (Fundamentos, Variables y tipos de datos, Condicionales, Bucles, Funciones, Mini proyecto) y de sus cantidades de lecciones como **punto de partida**, no como verdad: propónme el número real de lecciones por módulo y espera mi aprobación. Decisión C para el contenido (ya tomada).
- Construir la pantalla Ruta con el diseño de la guía y datos reales.
- (`feat: currículo, progreso y ruta de aprendizaje`)

### FASE 10 — Lección interactiva
- Pantalla genérica alimentada por el modelo del currículo (no una lección escrita a mano): título, explicación, bloque de código con resaltado, salida esperada, pregunta de comprobación con retroalimentación, XP **una sola vez** por lección, marcar como completada, avanzar a la siguiente, indicador "Lección X de N" real, enlaces al tutor y a practicar.
- Escribir las lecciones aprobadas en la decisión C. No se puede abrir una lección bloqueada.
- (`feat: lección interactiva con progreso real`)

### FASE 11 — Inicio
- Saludo con iniciales y racha; "Continúa donde quedaste" (siguiente lección real y avance real; si el usuario nuevo no ha empezado, mostrar un texto de "Empieza tu primera lección" — propón el texto); XP y retos de hoy según meta diaria; recomendado (propón qué criterio lo elige). Todo con datos reales.
- (`feat: pantalla de inicio con datos reales`)

### FASE 12 — Motor de reglas (dominio puro, sin UI) con tests unitarios
- Portar a Kotlin puro y testeado: XP y niveles, gemas, racha diaria (zona horaria definida), meta diaria, títulos, logros Bronce/Plata/Oro, estrellas y récords del dron, simulación del dron (muros, salida del mapa, bloques ×n, máximo de bloques).
- **Antes de codificar:** muéstrame cómo conviertes en reglas de Kotlin cada punto de la sección 10 de la guía (recompensas, racha, logros, títulos, simulación del dron, estrellas y récords), qué corriges de ellas (sin vidas, decisión D del logro Comprensión, cuándo cuenta un día para la racha) y espera mi OK.
- (`feat: motor de reglas de Glitch City con pruebas`)

### FASE 13 — Glitch City: mapa y pestañas
- Cabecera (gemas, racha, XP), título equipado, 4 pestañas, tarjetas, Títulos con "Equipar", reinicio de progreso con hoja de confirmación. Todo guardado en Firestore.
- (`feat: Glitch City - mapa y pestañas`)

### FASE 14 — Actividades: Explorar y Crear
- Pantalla de actividad modo laboratorio: 4 de Explorar (Semáforo, Cajero, Ascensor, Robot) y 2 de Crear (Tienda de ofertas, Reglas NPC), con controles, código generado en vivo, resultado, descubrimientos/objetivos y "Publicar creación". Quitar el pill "Sin vidas".
- (`feat: Glitch City - laboratorios de explorar y crear`)

### FASE 15 — Actividades: Competir (dron)
- 5 niveles, cuadrícula 5×5, movimientos, repetición ×n, programa, ejecutar/borrar/limpiar, idea, estrellas, récord personal, desbloqueo secuencial.
- (`feat: Glitch City - niveles del dron`)

### FASE 16 — Logros
- Progreso total, categorías, barras, niveles de dominio, recompensas, estado vacío. Sin el logro de corazones.
- (`feat: pantalla de logros`)

### FASE 17 — Perfil
- Avatar (iniciales), nivel y título reales, XP con barra, estadísticas, meta diaria (1/2/3/5, guardada en Firestore), accesos a Logros, Configuración y Cerrar sesión.
- (`feat: pantalla de perfil`)

### FASE 18 — Configuración y accesibilidad (decisiones F y G, ya tomadas)
- Tamaño de texto, alto contraste, reducir animaciones, subtítulos, sonido, recordatorios, datos y privacidad ("restablecer datos" con confirmación y su efecto real en Firestore), cerrar sesión.
- (`feat: configuración y accesibilidad`)

### FASE 19 — Pantalla del Tutor IA (solo interfaz, sin IA real)
- Exactamente como la guía de diseño: chips de preguntas, burbujas, campo de texto, aviso de demostración, respuestas predefinidas por palabra clave, detrás de la interfaz `TutorRepository`. **Sin IA real, sin claves, sin servicios externos.**
- (`feat: tutor IA en modo demostración`)

### FASE 20 — Revisión final y pulido
- Comparar cada pantalla con la guía de diseño (visual y textos), TalkBack/etiquetas, rotación y tamaños de pantalla, estados de error y sin conexión, revisión de las reglas de Firestore, limpieza de código y strings sin usar, tests pendientes, configuración de release.
- (`chore: revisión final y pulido`)

---

## CONVENCIONES DEL PROYECTO (respétalas)

- Estructura: `data/` (acceso a Firebase, solo resultados simples), `domain/` (reglas puras y testeables), `ui/components/`, `ui/screens/<pantalla>/` (Screen + ViewModel), `ui/theme/`.
- Cada pantalla: `XScreen` + `XViewModel` que expone `StateFlow<XUiState>`; las excepciones de Firebase nunca llegan a la UI.
- Colores en `Color.kt`, medidas en `Dimens.kt` (1 px de la guía = 1 dp) con comentario del equivalente CSS. Nada suelto en las pantallas.
- Reutiliza los componentes existentes antes de crear otros.
- Si la guía de diseño no define algo (como pasó con Recuperar contraseña), pregúntame y márcalo con el comentario "no está en el mockup".

## PRIMERA ACCIÓN

Empieza ahora **solo con la FASE 0**. No escribas código. Termina con tus preguntas numeradas y espera mi respuesta.
