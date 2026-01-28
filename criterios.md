# Criterios de evaluación
# RA1 — Interfaz, estructura y comportamiento de la aplicación

En este apartado se describe cómo se han aplicado los criterios del RA1 en el desarrollo de la aplicación, justificando las decisiones técnicas, de diseño y de arquitectura, y señalando las partes concretas del código donde se implementan.

---

## RA1.a — Análisis de herramientas y librerías

### Descripción y justificación

Para el desarrollo de la aplicación he realizado una selección consciente de herramientas y librerías modernas del ecosistema Android, priorizando la mantenibilidad, la escalabilidad y una arquitectura limpia.

He utilizado **Jetpack Compose** como sistema principal de creación de interfaces. La elección de Compose frente a XML tradicional se debe a su enfoque declarativo, que permite describir la UI como una función del estado. Esto facilita la reutilización de componentes, la reducción de código repetitivo y una mejor integración con flujos reactivos como `StateFlow`.

Para garantizar coherencia visual y accesibilidad, la interfaz se apoya en **Material Design 3**, utilizando `MaterialTheme.colorScheme`, tipografías y componentes estándar. Esto permite mantener una identidad visual consistente en toda la aplicación y adaptar fácilmente el diseño a distintos modos (claro/oscuro).

La navegación entre pantallas se gestiona mediante **Navigation Compose**, centralizando todas las rutas en un único `NavHost`. Este enfoque evita dependencias entre pantallas, permite pasar argumentos tipados (por ejemplo, en el chat) y simplifica el control del back stack.

Para la gestión de dependencias se ha utilizado **Hilt**, lo que permite desacoplar la creación de objetos de su uso. Gracias a Hilt, los ViewModels, repositorios, DAOs y servicios de Firebase se inyectan automáticamente, mejorando la legibilidad del código y facilitando futuras pruebas.

La persistencia local se implementa con **Room**, estructurando los datos mediante entidades, DAOs y convertidores de tipos. Esto permite almacenar eventos y usuarios de forma eficiente, realizar consultas complejas (por ejemplo, estadísticas) y mantener la app funcional incluso sin conexión.

La asincronía y la gestión de estado se realizan con **Kotlin Coroutines y Flow**, usando `StateFlow` para exponer el estado de la UI y `callbackFlow` para integrar flujos de datos en tiempo real (chat). Este enfoque garantiza que la UI siempre esté sincronizada con los datos.

Por último, se ha integrado **Firebase Authentication** para la gestión de usuarios y **Firebase Firestore** para el sistema de chat en tiempo real, evitando la necesidad de un backend propio y proporcionando escalabilidad inmediata.

### Dónde ocurre en el código
- `MainActivity.kt` → inicialización de Compose y navegación
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/MainActivity.kt#L330-L390
- `ProyectoFinalApp.kt` → `@HiltAndroidApp`
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/ProyectoFinalApp.kt#L6-L9
- Módulos DI: `di/*Module.kt`
  - Chat Module 
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/di/ChatModule.kt#L11-L37
  - Database Module
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/di/DataBaseModule.kt#L14-L58
  - Firebase Module
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/di/FirebaseModule.kt#L11-L39
  - Repository Module
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/di/RepositoryModule.kt#L15-L71
- Room: `AppDatabase.kt`, `UserDao.kt`, `EventDao.kt`, converters
  - AppDatabase   
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/di/AppDataBase.kt#L14-L38
  - UseDao
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/user/data/local/dao/UserDao.kt#L11-L60
  - EventDao
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/events/data/local/dao/EventDao.kt#L10-L84
  - RolConverter
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/user/data/local/converter/RolConverter.kt#L6-L32
  - CategoryConverter
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/events/data/local/Converter/CategoryConverter.kt#L6-L30
  - DateConverter
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/events/data/local/Converter/DateConverter.kt#L6-L40
- Chat realtime: `ChatRepositoryImpl.kt`
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/chat/data/repository/ChatRepositoryImpl.kt#L15-L119

### Evidencias
- Captura del `build.gradle` con dependencias
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/build.gradle.kts#L47-L101
- Captura del árbol de paquetes
  
  <img width="333" height="925" alt="image" src="https://github.com/user-attachments/assets/713c1cc8-d77a-433c-96a1-1c6a660f306c" />
  <img width="323" height="326" alt="image" src="https://github.com/user-attachments/assets/c9d2e172-007e-404e-b789-d1a182765363" />

---

## RA1.b — Creación de la interfaz gráfica

### Descripción y justificación

La aplicación cuenta con una **interfaz gráfica completa, funcional y coherente**, diseñada íntegramente con Jetpack Compose. La UI está organizada alrededor de un `Scaffold`, que actúa como estructura base común para todas las pantallas, integrando barra superior, barra inferior y botón de acción flotante.

La barra inferior permite la navegación entre las secciones principales de la aplicación, mientras que la barra superior se adapta dinámicamente según la pantalla activa, mostrando información contextual (saludo, título, chat, navegación hacia atrás o cierre de sesión).

Cada funcionalidad principal dispone de su propia pantalla: inicio, creación de eventos, eventos aceptados, eventos creados, puntos del usuario, panel de administración y chat. Todas las pantallas están conectadas mediante el sistema de navegación y comparten criterios visuales comunes.

La interfaz incluye también diálogos modales para acciones críticas (aceptar/cancelar eventos, marcar como resuelto, eliminar eventos), mejorando la experiencia de usuario y evitando acciones accidentales.

### Dónde ocurre en el código
- Estructura global: `MainActivity.kt` → `IniciarApp()` + `Scaffold`
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/MainActivity.kt#L68-L84
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/MainActivity.kt#L149-L390
- Navegación: `NavHost` en `MainActivity.kt`
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/MainActivity.kt#L330-L390
- Pantallas:
  - `HomeScreen.kt`
    https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/HomeScreen.kt#L45-L549
  - `CreateEvent.kt`
    https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/events/presentation/screen/CreateEventScreen.kt#L20-L126
  - `EventsUserScreen.kt`
    https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/EventsUserScreen.kt#L37-L122
  - `EventsUserCreateScreen.kt`
    https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/EventsUserCreateScreen.kt#L50-L357
  - `ViewPointsUserScreen.kt`
    https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/ViewPointsUser.kt#L45-L204
  - `AdminScreen.kt`
    https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/AdminScreen.kt#L35-L314
  - `ChatScreen.kt`
    https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/chat/presentation/screen/ChatScreen.kt#L52-L250

### Evidencias
- Capturas de las principales pantallas
- Captura del `Scaffold` y navegación
---

## RA1.c — Uso de layouts y posicionamiento

### Descripción y justificación

El posicionamiento de los elementos de la interfaz se ha realizado de forma clara y jerárquica, utilizando los layouts proporcionados por Compose: `Column`, `Row`, `Box`, `LazyColumn` y `Spacer`.

Las listas de contenido (eventos, mensajes, usuarios) se implementan mediante `LazyColumn`, lo que garantiza eficiencia y buen rendimiento incluso con grandes volúmenes de datos. Se utilizan claves (`key`) para asegurar una correcta recomposición.

La jerarquía visual está bien definida: las tarjetas de eventos presentan una cabecera destacada, seguida de contenido secundario y acciones, facilitando la lectura y comprensión de la información.

En el chat, el layout está diseñado para mantener la lista de mensajes desplazable mientras el campo de entrada permanece fijo en la parte inferior, mejorando la usabilidad.

### Dónde ocurre en el código
- `HomeScreen.kt` → `LazyColumn`, `AnimatedVisibility`
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/HomeScreen.kt#L45-L159
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/HomeScreen.kt#L108-L135
- `EventsUserScreen.kt` y `EventsUserCreateScreen.kt`
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/EventsUserScreen.kt#L37-L122
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/EventsUserCreateScreen.kt#L50-L357
- `ChatScreen.kt` → `Column` con `weight(1f)` para la lista
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/chat/presentation/screen/ChatScreen.kt#L52-L250
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/chat/presentation/screen/ChatScreen.kt#L117-L134
- `EventItem.kt` → composición de cards
https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/core/components/navigation/EventItem.kt#L43-L201
### Evidencias
- Capturas de listas y jerarquía visual
- Captura del chat mostrando layout fijo del input

---

## RA1.d — Personalización de componentes

### Descripción y justificación

Los componentes visuales han sido personalizados para ofrecer una experiencia cuidada y profesional. Se utilizan colores, formas y elevaciones definidas por el tema Material 3, evitando estilos por defecto sin adaptar.

Se han creado componentes propios como chips de estado (pendiente, aceptado, resuelto, evento propio), píldoras informativas (fecha, categoría) y burbujas de mensaje en el chat, todos ellos reutilizables y coherentes visualmente.

Las tarjetas de eventos cuentan con fondos diferenciados, iconos circulares, tipografías jerarquizadas y acciones contextuales, mejorando la claridad visual.

### Dónde ocurre en el código
- `EventItem.kt`
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/core/components/navigation/EventItem.kt#L43-L201
- Chips: `CreatorChipV2`, `PendingChipV2`, `AcceptedChip`
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/HomeScreen.kt#L481-L502
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/HomeScreen.kt#L504-L525
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/HomeScreen.kt#L527-L549
- `InfoPill`
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/HomeScreen.kt#L450-L479
- `ChatScreen.kt` → `MessageBubble`
https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/chat/presentation/screen/ChatScreen.kt#L152-L193
### Evidencias
- Capturas de cards con chips y colores
- Captura del chat con burbujas personalizadas

---

## RA1.e — Análisis del código

### Descripción y justificación

El código sigue una arquitectura por capas claramente diferenciadas: presentación, dominio y datos. Cada capa tiene una responsabilidad concreta, lo que mejora la mantenibilidad y la comprensión del proyecto.

La capa de presentación contiene los Composables y ViewModels, responsables únicamente de la UI y del estado. La lógica de negocio se encapsula en casos de uso dentro de la capa de dominio, que dependen de interfaces de repositorio. Finalmente, la capa de datos implementa estas interfaces utilizando Room o Firebase.

Este enfoque permite cambiar la fuente de datos sin afectar al resto del sistema y facilita la escalabilidad.

### Dónde ocurre en el código
- `presentation/*`
  
  <img width="232" height="612" alt="image" src="https://github.com/user-attachments/assets/c4e6f215-f4a3-4952-a61d-12da9f31cb66" />

- `domain/usecase/*`
  - User
    - GetAllUsersUseCase 
    https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/user/domain/usecase/GetAllUsersUseCase.kt#L8-L32
    - GetUserEmailUseCase
      https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/98d5dd248751aba00763fff4fcbb3ce26ce93e90/app/src/main/java/dam2/jetpack/proyectofinal/user/domain/usecase/GetUserByEmailUseCase.kt#L7-L35
    - GetUserByFirebaseUidUseCase
      https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/user/domain/usecase/GetUserByFirebaseUidUseCase.kt#L7-L35
    - SaveUserUseCase
      https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/user/domain/usecase/SaveUserUseCase.kt#L7-L29
  - Event
    - AcceptEventUseCase
      https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/events/domain/usecase/AcceptEventUseCase.kt#L8-L30
    - CreateEventUseCase
      https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/events/domain/usecase/CreateEventUseCase.kt#L7-L29
    - DeleteEventUseCase
      https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/events/domain/usecase/DeleteEventUseCase.kt#L6-L38
    - GetAllEventsUseCase
      https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/events/domain/usecase/GetAllEventsUseCase.kt#L8-L31
    - GetEventByIdUseCase
      https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/events/domain/usecase/GetEventByIdUseCase.kt#L7-L34
    - GetEventStatsUseCase
      https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/events/domain/usecase/GetEventStatsUseCase.kt#L5-L30
    - GetEventsUserCreateUseCase
      https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/events/domain/usecase/GetEventsUserCreateUseCase.kt#L8-L32
    - GetEventsUserUseCase
      https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/events/domain/usecase/GetEventsUserUseCase.kt#L8-L31

  - Chat
    - ListenMessageUseCase
      https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/chat/domain/usecase/ListenMessagesUseCase.kt#L8-L30
    - SendMessageUseCase
      https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/chat/domain/usecase/SendMessageUseCase.kt#L6-L28

  - Auth
    - LogOutUseCase
      https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/auth/domain/useCase/LogOutUseCase.kt#L6-L37
    - LoginUseCase
      https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/auth/domain/useCase/LoginUseCase.kt#L8-L51
    - RegisterUseDCase
      https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/auth/domain/useCase/RegisterUseCase.kt#L10-L53
- `domain/repository/*`
  - User:
  - UserRepository (interfaz)
    https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/user/domain/repository/UserRepository.kt#L7-L45
  - EventRepository (interfaz)
    https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/events/domain/repository/EventRepository.kt#L6-L75
  - ChatRepository (interfaz)
    https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/chat/domain/repository/ChatRepository.kt#L6-L30
  - AuthRepository (interfaz)
    https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/auth/domain/repository/AuthRepository.kt#L6-L50
- `data/repository/*`
  - UserRepositoryImpl (implementación)
    https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/user/data/repository/UserRepositoryImpl.kt#L12-L73
  - EventRepositoryImpl (implementación)
    https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/events/data/repository/EventRepositoryImpl.kt#L14-L118
  - ChatRepositoryImpl (implementación)
    https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/chat/data/repository/ChatRepositoryImpl.kt#L15-L119
  - AuthRepository (implementación)
    https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/auth/data/repository/AuthRepositoryImpl.kt#L10-L94
### Evidencias
- Diagrama de arquitectura
- Captura del árbol de paquetes

---

## RA1.f — Modificación del código

### Descripción y justificación

Durante el desarrollo se han realizado modificaciones significativas sobre una base inicial, incorporando nuevas funcionalidades y mejoras tanto a nivel funcional como de experiencia de usuario.

Entre las modificaciones destacan la implementación del chat en tiempo real, el reconocimiento de voz para dictado de mensajes, el sistema de swipe con confirmación para administradores y la generación de estadísticas visuales.

Todas las modificaciones están justificadas por necesidades funcionales reales y se integran de forma coherente en la arquitectura existente.

### Dónde ocurre en el código
- Voz: `ChatScreen.kt`
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/chat/presentation/screen/ChatScreen.kt#L52-L250
- Swipe admin: `HomeScreen.kt` → `SwipeableEventItem`
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/HomeScreen.kt#L117-L131
- Estadísticas: `EventDao.kt`, `EventRepositoryImpl.kt`, `AdminScreen.kt`
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/events/data/local/dao/EventDao.kt#L74-L84
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/events/data/repository/EventRepositoryImpl.kt#L104-L118
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/AdminScreen.kt#L123-L181
### Evidencias
- Capturas de las funcionalidades añadidas
- Fragmentos de código modificados

---

## RA1.g — Asociación de eventos

### Descripción y justificación

La aplicación asocia de forma coherente las acciones del usuario con los eventos del sistema. Los usuarios pueden aceptar eventos, cancelarlos, resolverlos y comunicarse mediante un chat asociado directamente a cada evento.

Las acciones tienen consecuencias directas en los datos persistidos (estado del evento, usuario aceptador, puntos del usuario), garantizando coherencia entre UI y lógica de negocio.

### Dónde ocurre en el código
- `EventViewModel.kt` → `acceptEvent`, `cancelAcceptance`, `markEventAsResolved`
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/events/presentation/viewModel/EventViewModel.kt#L228-L248
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/events/presentation/viewModel/EventViewModel.kt#L250-L262
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/events/presentation/viewModel/EventViewModel.kt#L305-L354
- `ChatRepositoryImpl.kt` → generación de `chatId`
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/chat/data/repository/ChatRepositoryImpl.kt#L27-L40
- Navegación al chat desde `EventItem.kt`
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fe29e69311953807ae7157c688e73fa8d847cfc4/app/src/main/java/dam2/jetpack/proyectofinal/core/components/navigation/EventItem.kt#L161-L196
### Evidencias
- Capturas de diálogos y chat asociado a eventos

---

## RA1.h — Aplicación integrada

### Descripción y justificación

La aplicación funciona como un sistema integrado y estable, donde todas las funcionalidades están conectadas entre sí. El flujo completo incluye autenticación, carga de usuario, navegación, gestión de eventos, comunicación en tiempo real y sistema de puntos.

El rol de administrador introduce funcionalidades adicionales sin romper la experiencia del usuario estándar, demostrando una integración correcta de permisos y vistas.

El resultado es una aplicación completa, coherente y funcional, sin secciones aisladas ni flujos inconexos.

### Dónde ocurre en el código
- Integración global: `MainActivity.kt`
https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/beec41f0f1ad826334d815d18635dde1d0b0554a/app/src/main/java/dam2/jetpack/proyectofinal/MainActivity.kt#L68-L390
- Control de rol admin: `bottomItems(isAdmin)` y `adminScreen`
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/beec41f0f1ad826334d815d18635dde1d0b0554a/app/src/main/java/dam2/jetpack/proyectofinal/MainActivity.kt#L95-L103
  
- Flujo eventos-puntos: `markEventAsResolved` + `ViewPointsUserScreen.kt`
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/beec41f0f1ad826334d815d18635dde1d0b0554a/app/src/main/java/dam2/jetpack/proyectofinal/events/presentation/viewModel/EventViewModel.kt#L305-L355
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/beec41f0f1ad826334d815d18635dde1d0b0554a/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/ViewPointsUser.kt#L45-L204
  
### Evidencias
- Capturas de navegación completa
- Captura de puntos actualizados
- Captura del panel admin

# RA2 — Interfaces Naturales de Usuario (NUI)

En este apartado se analizan las interfaces naturales de usuario (NUI) utilizadas y planteadas en la aplicación. El objetivo ha sido mejorar la experiencia de usuario incorporando formas de interacción más intuitivas y cercanas, combinando funcionalidades implementadas con propuestas realistas y coherentes con el contexto del proyecto.

La aplicación está orientada a la gestión de eventos colaborativos, por lo que se ha tenido especialmente en cuenta la accesibilidad, la rapidez de interacción y la reducción de fricción en el uso diario.

---

## RA2.a — Herramientas NUI

### Análisis y justificación

Durante el desarrollo del proyecto se ha realizado un análisis consciente de las herramientas NUI disponibles en el ecosistema Android, seleccionando aquellas que aportan valor real a la aplicación.

La principal herramienta NUI **implementada directamente** es el **reconocimiento de voz nativo de Android**, a través de la API `SpeechRecognizer`. Esta herramienta permite convertir la voz del usuario en texto sin necesidad de librerías externas, ofreciendo una solución estable, mantenida por Google y perfectamente integrada con el sistema operativo.

Además, se han utilizado **gestos táctiles avanzados** proporcionados por **Jetpack Compose**, como el deslizamiento (swipe), que forma parte de las interacciones naturales más comunes en dispositivos móviles.

De forma complementaria, se han analizado otras herramientas NUI relevantes aunque no se hayan implementado en esta versión del proyecto, como:
- **ML Kit**, para detección facial y corporal.
- **CameraX**, como base para captura de imagen.
- **ARCore**, para posibles experiencias de realidad aumentada.
- Gestos avanzados de Compose (`Drag`, `LongPress`, `Swipe`).

La selección y análisis de estas herramientas se ha realizado teniendo en cuenta el alcance del proyecto, la complejidad técnica y la coherencia con un entorno profesional real.

### Dónde ocurre en el proyecto
- Reconocimiento de voz: `ChatScreen.kt`
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/beec41f0f1ad826334d815d18635dde1d0b0554a/app/src/main/java/dam2/jetpack/proyectofinal/chat/presentation/screen/ChatScreen.kt#L79-L97
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/beec41f0f1ad826334d815d18635dde1d0b0554a/app/src/main/java/dam2/jetpack/proyectofinal/chat/presentation/screen/ChatScreen.kt#L136-L150
- Gestos táctiles: `HomeScreen.kt` (Swipe para eliminar eventos)
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/beec41f0f1ad826334d815d18635dde1d0b0554a/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/HomeScreen.kt#L117-L136
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/beec41f0f1ad826334d815d18635dde1d0b0554a/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/HomeScreen.kt#L160-L263
- Análisis teórico: documentación del proyecto

---

## RA2.b — Diseño conceptual NUI

### Diseño y enfoque conceptual

El diseño conceptual de las interfaces NUI de la aplicación se basa en ofrecer **formas alternativas y naturales de interacción**, sin sustituir completamente los controles tradicionales.

El objetivo principal es que el usuario pueda:
- Interactuar hablando cuando escribir no sea cómodo.
- Realizar acciones rápidas mediante gestos.
- Recibir feedback visual inmediato tras cada acción.

Este enfoque resulta especialmente útil en el contexto de la aplicación, donde los usuarios pueden encontrarse en situaciones de movilidad o realizando tareas prácticas.

El diseño NUI no es obligatorio ni intrusivo: todas las acciones NUI tienen su equivalente tradicional, garantizando accesibilidad y evitando dependencias exclusivas de una sola forma de interacción.

El diseño conceptual se ha aplicado de forma transversal en distintas pantallas, manteniendo coherencia visual y funcional.

### Dónde ocurre en el proyecto
- Chat por voz: `ChatScreen.kt`
   https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/beec41f0f1ad826334d815d18635dde1d0b0554a/app/src/main/java/dam2/jetpack/proyectofinal/chat/presentation/screen/ChatScreen.kt#L79-L97
- Gestos administrativos: `HomeScreen.kt`
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/beec41f0f1ad826334d815d18635dde1d0b0554a/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/HomeScreen.kt#L117-L136
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/beec41f0f1ad826334d815d18635dde1d0b0554a/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/HomeScreen.kt#L160-L263

---

## RA2.c — Interacción por voz

### Implementación y funcionamiento

La aplicación incluye **interacción por voz real y funcional**, integrada directamente en el sistema de chat asociado a los eventos.

Mediante el uso de la API `SpeechRecognizer`, el usuario puede dictar mensajes en lugar de escribirlos. Esta funcionalidad se activa de forma explícita mediante un botón con icono de micrófono, evitando activaciones accidentales.

El flujo de uso es el siguiente:
1. El usuario pulsa el botón de micrófono.
2. El sistema inicia el reconocimiento de voz.
3. El texto reconocido se inserta automáticamente en el campo de mensaje.
4. El usuario puede revisar, editar o enviar el mensaje.

Esta implementación mejora la accesibilidad, reduce el tiempo de interacción y se adapta a situaciones reales de uso.

### Dónde ocurre en el proyecto
- `ChatScreen.kt`
  - Inicialización del reconocimiento de voz
    https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/beec41f0f1ad826334d815d18635dde1d0b0554a/app/src/main/java/dam2/jetpack/proyectofinal/chat/presentation/screen/ChatScreen.kt#L79-L97
  - Gestión de resultados
    https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/beec41f0f1ad826334d815d18635dde1d0b0554a/app/src/main/java/dam2/jetpack/proyectofinal/chat/presentation/screen/ChatScreen.kt#L85-L87
  - Inserción del texto en el campo de entrada
    https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/beec41f0f1ad826334d815d18635dde1d0b0554a/app/src/main/java/dam2/jetpack/proyectofinal/chat/presentation/screen/ChatScreen.kt#L137-L150
---

## RA2.d — Interacción por gesto

### Implementación y experiencia de usuario

La interacción por gestos se ha implementado mediante **gestos táctiles naturales**, concretamente el gesto de **deslizamiento horizontal (swipe)**.

En la pantalla principal, los usuarios con rol de administrador pueden eliminar eventos deslizando una tarjeta. Este gesto:
- Es intuitivo y ampliamente reconocido por los usuarios.
- Reduce la saturación visual de botones.
- Acelera la realización de acciones administrativas.

Para evitar errores, el gesto está acompañado de un **diálogo de confirmación**, garantizando seguridad y control. Además, la funcionalidad está restringida por rol, lo que refuerza el control de permisos.

### Dónde ocurre en el proyecto
- `HomeScreen.kt`
  - `SwipeableEventItem`
    https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/beec41f0f1ad826334d815d18635dde1d0b0554a/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/HomeScreen.kt#L117-L132
    https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/beec41f0f1ad826334d815d18635dde1d0b0554a/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/HomeScreen.kt#L160-L263
  - `SwipeToDismissBox`
    https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/beec41f0f1ad826334d815d18635dde1d0b0554a/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/HomeScreen.kt#L238-L263
    https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/beec41f0f1ad826334d815d18635dde1d0b0554a/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/HomeScreen.kt#L267-L298
  - Diálogo de confirmación
    https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/beec41f0f1ad826334d815d18635dde1d0b0554a/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/HomeScreen.kt#L198-L236
---

## RA2.e — Detección facial / corporal

### Análisis y propuesta razonada

La detección facial o corporal no se ha implementado directamente en esta versión del proyecto, pero se ha realizado una **reflexión técnica razonada** sobre su posible uso futuro.

Algunas aplicaciones viables dentro del contexto del proyecto serían:
- Verificación visual en eventos presenciales.
- Mejora de accesibilidad mediante reconocimiento de expresiones.
- Validación de presencia en eventos físicos.

Estas funcionalidades podrían implementarse utilizando **ML Kit** junto con **CameraX**, aunque no se han incluido para evitar aumentar la complejidad técnica y los requisitos de permisos en esta fase.

La decisión de no implementarlas está justificada por criterios de alcance, tiempo y adecuación al proyecto académico.

### Dónde se documenta
- README del proyecto
- Apartado de mejoras futuras

---

## RA2.f — Realidad aumentada

### Propuesta y justificación

La realidad aumentada no se ha implementado, pero se ha planteado una **propuesta clara, útil y realista** basada en **ARCore**.

Una posible evolución de la aplicación permitiría:
- Visualizar eventos cercanos superpuestos en el entorno real.
- Guiar al usuario hasta ubicaciones físicas mediante marcadores AR.
- Mostrar información contextual (categoría, estado, distancia) directamente sobre la imagen de la cámara.

Esta propuesta encaja especialmente bien en eventos comunitarios o presenciales y representa una evolución natural del proyecto.

La funcionalidad se ha descartado en esta fase por motivos de alcance, pero está correctamente fundamentada desde un punto de vista técnico y de experiencia de usuario.

### Dónde se documenta
- README del proyecto
- Propuestas de mejora y ampliación

# RA3 — Componentes reutilizables y diseño modular

En este apartado se describe el uso de componentes reutilizables dentro de la aplicación, así como las herramientas empleadas para su creación, configuración e integración. El objetivo principal ha sido construir una interfaz modular, mantenible y escalable, siguiendo buenas prácticas de desarrollo con Jetpack Compose.

---

## RA3.a — Herramientas de componentes

### Identificación y justificación

Para el desarrollo de la interfaz se ha utilizado **Jetpack Compose** como herramienta principal para la creación de componentes visuales. Compose permite definir componentes reutilizables mediante funciones `@Composable`, facilitando la separación de responsabilidades y la reutilización de código.

Además, se han utilizado herramientas complementarias del ecosistema Android:
- **Material 3**: para garantizar coherencia visual, accesibilidad y adaptación al sistema.
- **Navigation Compose**: para integrar componentes dentro del flujo de navegación.
- **StateFlow y ViewModel**: para desacoplar la lógica de estado de los componentes visuales.
- **Hilt**: para inyección de dependencias y desacoplamiento entre capas.

La elección de estas herramientas está justificada por su uso profesional en proyectos Android modernos, su mantenimiento activo y su perfecta integración entre sí.

### Dónde ocurre en el proyecto
- Componentes visuales: paquete `core.components.navigation`
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/d557f51459572924605c642819c527b9bf96966a/app/src/main/java/dam2/jetpack/proyectofinal/core/components/navigation/BottomNavItem.kt#L5-L19
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/d557f51459572924605c642819c527b9bf96966a/app/src/main/java/dam2/jetpack/proyectofinal/core/components/navigation/EventItem.kt#L43-L201
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/d557f51459572924605c642819c527b9bf96966a/app/src/main/java/dam2/jetpack/proyectofinal/core/components/navigation/EventsStatsChart.kt#L18-L77
- Uso de Material 3: en todas las pantallas (`MaterialTheme`)
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/d557f51459572924605c642819c527b9bf96966a/app/src/main/java/dam2/jetpack/proyectofinal/MainActivity.kt#L21-L30
- Arquitectura y estado: `ViewModel`, `StateFlow`
  - AuthViewModel
    https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/d557f51459572924605c642819c527b9bf96966a/app/src/main/java/dam2/jetpack/proyectofinal/auth/presentation/viewmodel/AuthViewModel.kt#L17-L113
    - EventViewModel
      https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/d557f51459572924605c642819c527b9bf96966a/app/src/main/java/dam2/jetpack/proyectofinal/events/presentation/viewModel/EventViewModel.kt#L26-L355
    - UserViewModel
      https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/d557f51459572924605c642819c527b9bf96966a/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/viewmodel/UserViewModel.kt#L19-L117
---

## RA3.b — Componentes reutilizables

### Diseño y reutilización

La aplicación hace un uso intensivo de **componentes reutilizables**, diseñados para ser utilizados en múltiples pantallas sin duplicar código.

Algunos ejemplos claros son:
- `EventItem`: tarjeta reutilizable para mostrar eventos en distintas pantallas (Home, eventos aceptados, swipe de administrador).
- `InfoPill`: componente reutilizable para mostrar información contextual (fecha, categoría).
- `AcceptedChip`, `PendingChipV2`, `CreatorChipV2`: chips de estado reutilizables.
- `EmptyState`: componente genérico para estados vacíos.

Estos componentes están diseñados para ser independientes, legibles y fácilmente ampliables, lo que mejora la mantenibilidad del proyecto.

### Dónde ocurre en el proyecto
- Componentes reutilizables:  
  - `core.components.navigation.EventItem`
    https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/d557f51459572924605c642819c527b9bf96966a/app/src/main/java/dam2/jetpack/proyectofinal/core/components/navigation/EventItem.kt#L43-L201
  - `user.presentation.screen.InfoPill`
    https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/d557f51459572924605c642819c527b9bf96966a/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/HomeScreen.kt#L450-L479
  - `user.presentation.screen.*Chip`
    https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/d557f51459572924605c642819c527b9bf96966a/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/HomeScreen.kt#L377-L407
    https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/d557f51459572924605c642819c527b9bf96966a/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/HomeScreen.kt#L481-L549

---

## RA3.c — Parámetros y valores por defecto

### Diseño de parámetros

Los componentes reutilizables se han diseñado con **parámetros claros y bien definidos**, permitiendo su adaptación a diferentes contextos sin modificar su implementación interna.

Por ejemplo:
- `EventItem` recibe el evento, el usuario actual y callbacks de interacción.
- `InfoPill` recibe el icono y el texto a mostrar.
- Los chips reciben información mínima necesaria para representar su estado.

En los casos necesarios, se han definido **valores por defecto** para evitar configuraciones innecesarias y simplificar el uso de los componentes.

Este enfoque garantiza un uso consistente de los componentes y evita errores derivados de configuraciones incorrectas.

### Dónde ocurre en el proyecto
- Definición de parámetros: archivos de componentes (`EventItem.kt`, `InfoPill.kt`)
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/d557f51459572924605c642819c527b9bf96966a/app/src/main/java/dam2/jetpack/proyectofinal/core/components/navigation/EventItem.kt#L43-L49
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/d557f51459572924605c642819c527b9bf96966a/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/HomeScreen.kt#L451-L454
- Uso consistente en distintas pantallas

---

## RA3.d — Eventos en componentes

### Gestión de eventos e interacción

Los componentes gestionan correctamente los eventos de usuario mediante **lambdas** que se pasan desde los composables padres. Esto permite:
- Mantener los componentes desacoplados de la lógica de negocio.
- Reutilizar los mismos componentes en contextos distintos.
- Centralizar la lógica de acciones en ViewModels o pantallas superiores.

Ejemplos de eventos gestionados:
- Click sobre un evento (`onClick`).
- Swipe para eliminar (`onDelete`).
- Confirmaciones mediante diálogos.
- Acciones de aceptar o cancelar eventos.

La interacción es fluida, coherente y consistente en toda la aplicación.

### Dónde ocurre en el proyecto
- Callbacks en `EventItem`
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/d03ee736cd3e920fe80694b2b7944e9b6ebcb2eb/app/src/main/java/dam2/jetpack/proyectofinal/core/components/navigation/EventItem.kt#L48
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/d03ee736cd3e920fe80694b2b7944e9b6ebcb2eb/app/src/main/java/dam2/jetpack/proyectofinal/core/components/navigation/EventItem.kt#L71-L75
- Eventos en `SwipeableEventItem`
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/d03ee736cd3e920fe80694b2b7944e9b6ebcb2eb/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/HomeScreen.kt#L158-L166
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/d03ee736cd3e920fe80694b2b7944e9b6ebcb2eb/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/HomeScreen.kt#L113-L132
- Diálogos de acción en `EventActionDialog`
https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/d03ee736cd3e920fe80694b2b7944e9b6ebcb2eb/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/HomeScreen.kt#L140-L145

---

## RA3.f — Documentación de componentes

### Documentación y claridad

Los componentes y clases principales del proyecto están **documentados mediante comentarios KDoc**, describiendo su propósito, parámetros y comportamiento.

Además, la estructura de paquetes sigue una organización clara:
- Separación por capas (data, domain, presentation).
- Separación de componentes reutilizables.
- Nombres descriptivos y coherentes.

Esta documentación facilita la comprensión del proyecto, su mantenimiento y su posible ampliación por otros desarrolladores.

### Dónde ocurre en el proyecto
- Comentarios KDoc en:
  - Repositorios
  - Casos de uso
  - ViewModels
  - Componentes reutilizables
---

## RA3.h — Integración de componentes en la app

### Integración global

Los componentes reutilizables no se utilizan de forma aislada, sino que están **totalmente integrados en la aplicación**, apareciendo en múltiples pantallas y flujos de navegación.

La integración es coherente tanto a nivel visual como funcional:
- Mismos componentes para mismos conceptos.
- Comportamiento consistente en toda la app.
- Adaptación automática al estado y rol del usuario.

Este enfoque refuerza la identidad visual de la aplicación y reduce la duplicación de código, cumpliendo principios de diseño modular y escalable.

### Dónde ocurre en el proyecto
- Integración en:
  - `HomeScreen`
  PENDIENTE
  - `EventsUserScreen`
  - `AdminScreen`
  - `ViewPointsUserScreen`
- Navegación centralizada en `MainActivity.kt`

# RA4 — Usabilidad, diseño visual y experiencia de usuario

En este apartado se analiza la aplicación desde el punto de vista de la **usabilidad**, el **diseño visual** y la **experiencia de usuario**, aplicando estándares reconocidos de diseño de interfaces móviles y buenas prácticas propias del ecosistema Android con Jetpack Compose y Material Design 3.

---

## RA4.a — Aplicación de estándares

### Uso de estándares de diseño

La aplicación aplica de forma consistente los **estándares de diseño de Android** a través del uso de **Material Design 3** y Jetpack Compose. Se respetan principios fundamentales como:
- Jerarquía visual clara
- Contraste adecuado de colores
- Tamaños de tipografía accesibles
- Componentes interactivos coherentes

El uso de `MaterialTheme`, `Surface`, `Card`, `Button`, `NavigationBar` y `TopAppBar` garantiza una experiencia alineada con las guías oficiales de Android, ofreciendo una interfaz moderna, consistente y reconocible para el usuario.

### Dónde ocurre en el proyecto
- Uso global de `MaterialTheme` en todas las pantallas
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/7770661ebb48559904219b4939d5320a7272a971/app/src/main/java/dam2/jetpack/proyectofinal/MainActivity.kt#L137-L143
- Componentes Material en `HomeScreen`, `AdminScreen`, `ChatScreen`, etc.
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/7770661ebb48559904219b4939d5320a7272a971/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/HomeScreen.kt#L201-L204
- Tema definido en `ProyectoFinalTheme`
https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/7770661ebb48559904219b4939d5320a7272a971/app/src/main/java/dam2/jetpack/proyectofinal/ui/theme/Theme.kt#L41-L62 
https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/7770661ebb48559904219b4939d5320a7272a971/app/src/main/java/dam2/jetpack/proyectofinal/MainActivity.kt#L205

---

## RA4.b — Valoración de los estándares aplicados

### Reflexión y justificación

No solo se aplican los estándares, sino que se **valoran y adaptan** al contexto de la aplicación. Por ejemplo:
- Se utiliza navegación inferior (`BottomNavigation`) por tratarse de una app con secciones principales claras.
- Se prioriza la visibilidad de acciones frecuentes mediante botones principales y FAB.
- Se adaptan colores y jerarquías según el rol del usuario (administrador o usuario normal).

Estas decisiones están justificadas por criterios de usabilidad, frecuencia de uso y claridad, no por una aplicación mecánica del estándar.

### Dónde ocurre en el proyecto
- Barra inferior dinámica en `MainActivity.kt`
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/d03ee736cd3e920fe80694b2b7944e9b6ebcb2eb/app/src/main/java/dam2/jetpack/proyectofinal/MainActivity.kt#L86-L147
- Pantalla de administración exclusiva para administradores
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/d03ee736cd3e920fe80694b2b7944e9b6ebcb2eb/app/src/main/java/dam2/jetpack/proyectofinal/MainActivity.kt#L102
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/d03ee736cd3e920fe80694b2b7944e9b6ebcb2eb/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/AdminScreen.kt#L35-L314
- FAB solo visible en la pantalla principal
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/d03ee736cd3e920fe80694b2b7944e9b6ebcb2eb/app/src/main/java/dam2/jetpack/proyectofinal/MainActivity.kt#L235-L244

---

## RA4.c — Menús de navegación

### Diseño y coherencia

La aplicación cuenta con un sistema de menús **claro, coherente y bien integrado**, compuesto por:
- Barra de navegación inferior para secciones principales.
- Barra superior (`TopAppBar`) contextual.
- Navegación interna mediante `NavHost`.

Los menús se adaptan al estado de la aplicación (por ejemplo, ocultando la barra inferior en el chat), lo que mejora la concentración del usuario en la tarea actual.

### Dónde ocurre en el proyecto
- `AppBottomBar` en `MainActivity.kt`
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/d03ee736cd3e920fe80694b2b7944e9b6ebcb2eb/app/src/main/java/dam2/jetpack/proyectofinal/MainActivity.kt#L86-L147
- `NavHost` con rutas bien definidas
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/d03ee736cd3e920fe80694b2b7944e9b6ebcb2eb/app/src/main/java/dam2/jetpack/proyectofinal/MainActivity.kt#L329-L390
- Ocultación condicional del menú inferior en pantalla de chat

---

## RA4.d — Distribución de acciones

### Claridad y accesibilidad

Las acciones principales están colocadas de forma **clara y accesible**, siguiendo patrones conocidos:
- Acción principal destacada (FAB para crear eventos).
- Acciones secundarias en botones claramente etiquetados.
- Acciones destructivas (eliminar eventos) diferenciadas visualmente.

La distribución evita sobrecargar la interfaz y guía al usuario de forma natural.

### Dónde ocurre en el proyecto
- FAB en `HomeScreen`
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fd6eb8662e40c99b7f5147011b90163e46bca5b2/app/src/main/java/dam2/jetpack/proyectofinal/MainActivity.kt#L239-L244
- Botones de acción en `EventItem`, `AdminScreen`
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fd6eb8662e40c99b7f5147011b90163e46bca5b2/app/src/main/java/dam2/jetpack/proyectofinal/core/components/navigation/EventItem.kt#L165-L184
https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fd6eb8662e40c99b7f5147011b90163e46bca5b2/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/AdminScreen.kt#L84-L95
- Swipe para acciones administrativas
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fd6eb8662e40c99b7f5147011b90163e46bca5b2/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/HomeScreen.kt#L158-L165
---

## RA4.e — Distribución de controles

### Orden y jerarquía

Los controles están organizados de forma lógica y ordenada:
- Información principal en la parte superior.
- Acciones relacionadas cerca del contenido al que afectan.
- Uso de espaciado consistente y alineación correcta.

Se aplica una jerarquía visual clara que permite escanear la pantalla rápidamente sin confusión.

### Dónde ocurre en el proyecto
- Uso de `Column`, `Row`, `Spacer` en todas las pantallas
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fd6eb8662e40c99b7f5147011b90163e46bca5b2/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/HomeScreen.kt#L76-L96
  https://github.com/jesuscb123/ProyectoFinal-Interfaces/blob/fd6eb8662e40c99b7f5147011b90163e46bca5b2/app/src/main/java/dam2/jetpack/proyectofinal/user/presentation/screen/HomeScreen.kt#L390-L396
- Tarjetas (`Card`) como contenedores principales
  
- Componentes agrupados por función

---

## RA4.f — Elección de controles

### Justificación de los componentes utilizados

Los controles elegidos son adecuados para su función:
- `Button` para acciones claras.
- `IconButton` para acciones secundarias.
- `SwipeToDismiss` para acciones administrativas.
- `OutlinedTextField` para entrada de texto en el chat.

Cada control responde a un patrón de uso reconocido, facilitando la comprensión y reduciendo la curva de aprendizaje.

### Dónde ocurre en el proyecto
- Chat: `OutlinedTextField`, `IconButton`
- Listados: `Card`, `LazyColumn`
- Acciones críticas: `AlertDialog`

---

## RA4.g — Diseño visual

### Atractivo y coherencia visual

El diseño visual es atractivo y coherente gracias a:
- Paleta de colores unificada.
- Tipografías consistentes.
- Uso de iconografía clara y reconocible.
- Animaciones sutiles para mejorar la percepción de fluidez.

La interfaz resulta moderna, legible y agradable para el usuario.

### Dónde ocurre en el proyecto
- Animaciones en `HomeScreen` y `EventsUserScreen`
- Chips de estado con colores semánticos
- Tema global definido en `ProyectoFinalTheme`

---

## RA4.h — Claridad de mensajes

### Comunicación con el usuario

Los mensajes mostrados al usuario son claros, concisos y adaptados al contexto:
- Estados vacíos explicativos.
- Confirmaciones antes de acciones importantes.
- Mensajes contextualizados según el rol y la acción.

Esto reduce errores y mejora la experiencia general.

### Dónde ocurre en el proyecto
- `EmptyState`
- `AlertDialog` de confirmación
- Textos dinámicos en eventos y chat

---

## RA4.i — Pruebas de usabilidad

### Evaluación funcional

Se han realizado pruebas de usabilidad funcionales durante el desarrollo:
- Navegación completa entre pantallas.
- Uso de la app con distintos roles.
- Validación de flujos principales (crear, aceptar, resolver eventos).

Estas pruebas han permitido ajustar la distribución de acciones y mejorar la claridad de la interfaz.

### Dónde se evidencia
- Correcciones de navegación en `MainActivity`
- Ajustes de visibilidad según rol
- Mejora de estados vacíos y feedback visual

---

## RA4.j — Evaluación en distintos dispositivos

### Adaptación y pruebas

La aplicación ha sido evaluada en distintos tamaños de pantalla y orientaciones utilizando:
- Emuladores Android con diferentes resoluciones.
- Dispositivos físicos.
- Uso de layouts flexibles (`fillMaxWidth`, `weight`, `LazyColumn`).

Esto garantiza un comportamiento correcto y consistente independientemente del dispositivo.

### Dónde ocurre en el proyecto
- Uso de layouts responsivos en todas las pantallas
- Componentes adaptables y scrollables
- Diseño fluido sin valores fijos innecesarios

## RA5 – Generación de informes

### RA5.a – Establece la estructura del informe

La aplicación implementa una estructura clara y organizada para la generación de informes, siguiendo una arquitectura en capas.  
La información del informe se obtiene desde la base de datos local, se procesa en la capa de dominio y se presenta en la interfaz de usuario mediante componentes gráficos.

La estructura del informe se divide en:
- Capa de datos: consultas a la base de datos Room.
- Capa de dominio: casos de uso específicos para informes.
- Capa de presentación: ViewModel y componentes visuales.

Esta separación permite mantener el código limpio, escalable y fácilmente ampliable.

Ubicación en el código:
- EventDao.kt
- EventRepository.kt / EventRepositoryImpl.kt
- GetEventStatsUseCase.kt
- EventViewModel.kt
- EventStatsChart.kt

---

### RA5.b – Genera informes a partir de fuentes de datos

El informe se genera a partir de datos reales almacenados en la base de datos local (Room).  
Los valores mostrados no son simulados, sino que se obtienen directamente de la tabla de eventos mediante consultas SQL.

Estas consultas permiten conocer el estado real de la aplicación y garantizan la fiabilidad del informe.

Ubicación en el código:
- EventDao.kt (consultas COUNT sobre la tabla events)
- EventRepositoryImpl.kt (obtención de los datos)
- GetEventStatsUseCase.kt (exposición de los datos al ViewModel)

---

### RA5.c – Establece filtros sobre los valores a presentar

El informe aplica filtros claros sobre los datos presentados, diferenciando los eventos según su estado.

Los filtros utilizados son:
- Eventos completados.
- Eventos aceptados pero no resueltos.

Estos filtros se aplican directamente en la base de datos, asegurando eficiencia y coherencia en la información mostrada.

Ubicación en el código:
- EventDao.kt (condiciones WHERE en las consultas SQL)

---

### RA5.d – Incluye valores calculados, recuentos o totales

El informe incluye recuentos y valores calculados que permiten analizar el estado global de los eventos.

Además de los recuentos obtenidos desde la base de datos, la interfaz calcula totales y porcentajes para mejorar la comprensión de la información.

Ubicación en el código:
- EventRepositoryImpl.kt (recuento de eventos)
- EventStatsChart.kt (cálculo de totales y porcentajes)

---

### RA5.e – Incluye gráficos generados a partir de los datos

La aplicación incluye gráficos generados dinámicamente a partir de los datos del informe.  
Se utiliza un gráfico de barras que representa visualmente el número de eventos completados y aceptados.

El gráfico se actualiza automáticamente cuando cambian los datos, ofreciendo una visualización clara y profesional.

Ubicación en el código:
- EventStatsChart.kt (componente gráfico)
- AdminScreen.kt (integración del gráfico en la interfaz)


## RA6 – Sistemas de ayuda y documentación

### RA6.a – Identifica sistemas de generación de ayudas

En la aplicación se identifican y utilizan distintos sistemas de generación de ayudas orientados tanto al usuario final como al desarrollador.  
Estas ayudas no se limitan a un único formato, sino que se integran de forma natural en la interfaz y en la documentación del proyecto.

A nivel de interfaz, se utilizan mensajes informativos, estados vacíos y diálogos de confirmación que guían al usuario en el uso correcto de la aplicación.  
A nivel técnico, el proyecto incorpora comentarios de código y documentación estructurada que facilitan la comprensión del funcionamiento interno.

Este enfoque permite ofrecer ayudas claras sin sobrecargar la experiencia del usuario.

Ubicación en el código:
- Composables `EmptyState` utilizados en varias pantallas.
- Uso de `AlertDialog` para confirmar acciones críticas.
- Comentarios KDoc en entidades, repositorios, casos de uso y ViewModels.

---

### RA6.b – Genera ayudas en formatos habituales

Las ayudas se generan utilizando formatos habituales y reconocibles dentro del desarrollo de aplicaciones móviles.  
La aplicación ofrece información al usuario mediante:
- Textos descriptivos integrados en la interfaz.
- Diálogos modales con explicaciones claras.
- Mensajes de estado para situaciones sin datos.
- Documentación escrita externa en formato README.

Estos formatos permiten que el usuario comprenda el funcionamiento de la aplicación sin necesidad de formación previa, siguiendo estándares comunes de usabilidad.

Ubicación en el código:
- `AdminScreen.kt` (mensajes informativos y explicación de estadísticas).
- `HomeScreen.kt` (mensajes de bienvenida y estados vacíos).
- `ViewPointsUserScreen.kt` (explicación del sistema de puntos).
- `README.md` (manual de uso).

---

### RA6.c – Genera ayudas sensibles al contexto

La aplicación genera ayudas adaptadas al contexto y al estado actual del usuario.  
Dependiendo de la situación, se muestran mensajes distintos para evitar confusión y mejorar la experiencia de uso.

Ejemplos de ayudas contextuales:
- Mensajes cuando no existen eventos disponibles.
- Avisos cuando el usuario no ha creado o aceptado eventos.
- Diálogos de confirmación antes de realizar acciones irreversibles, como eliminar un evento o marcarlo como resuelto.

Estas ayudas solo aparecen cuando son necesarias, ofreciendo información relevante en el momento adecuado.

Ubicación en el código:
- Condiciones `if` y `when` basadas en el estado de la UI.
- Composable `EmptyState` reutilizado en distintas pantallas.
- `AlertDialog` en acciones críticas.

---

### RA6.d – Documenta la estructura de la información persistente

La estructura de la información persistente está documentada de forma clara y ordenada mediante el uso de entidades Room, DAOs y comentarios explicativos.

Cada entidad define de forma explícita la estructura de la tabla correspondiente, sus campos y su propósito dentro de la aplicación.  
Los DAOs documentan las operaciones de acceso a datos, facilitando la comprensión de las consultas realizadas.

Esta documentación permite mantener y ampliar la base de datos de forma segura y coherente.

Ubicación en el código:
- `UserEntity.kt`
- `EventEntity.kt`
- `UserDao.kt`
- `EventDao.kt`
- Comentarios KDoc en cada clase y método.

---

### RA6.e – Manual de usuario y guía de referencia

El proyecto incluye un manual de usuario en formato README que actúa como guía de referencia para el uso de la aplicación.  
En este manual se explica:
- El objetivo de la aplicación.
- La navegación entre pantallas.
- El funcionamiento del sistema de eventos.
- El sistema de puntos y recompensas.
- Los roles de usuario (usuario y administrador).

El manual está orientado a usuarios finales y utiliza un lenguaje claro y accesible.

Ubicación:
- `README.md` (apartado Manual de Usuario).
- Capturas de pantalla de las principales vistas.

---

### RA6.f – Manual técnico de instalación y configuración

La aplicación dispone de un manual técnico de instalación y configuración orientado a desarrolladores.  
Este manual describe:
- Los requisitos del entorno de desarrollo.
- La configuración del proyecto en Android Studio.
- La integración con Firebase.
- El proceso de compilación y ejecución de la aplicación.

Esta documentación permite reproducir el entorno de trabajo y facilita la continuidad del proyecto.

Ubicación:
- `README.md` (apartado Manual Técnico).
- Archivos de configuración Gradle.

---

### RA6.g – Confecciona tutoriales

Se han confeccionado tutoriales de uso que guían al usuario en las acciones principales de la aplicación.  
Estos tutoriales no se presentan como una pantalla independiente, sino que se integran de forma progresiva en el flujo normal de la app mediante:
- Navegación guiada.
- Mensajes explicativos.
- Diálogos de confirmación.

Las acciones cubiertas incluyen:
- Crear un evento.
- Aceptar un evento.
- Marcar un evento como resuelto.
- Consultar los puntos acumulados.

Ubicación:
- Flujo de navegación definido en `MainActivity.kt`.
- Textos explicativos en las pantallas principales.
- `README.md` (apartado Tutorial de uso).



