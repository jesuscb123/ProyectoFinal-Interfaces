## ManoVecina

## Descripción del proyecto
Una aplicación nativa de Android para la gestión de eventos y tareas comunitarias. Está diseñada para que un grupo de personas (como vecinos de una comunidad o miembros de una organización) pueda reportar incidencias, ofrecerse como voluntarios para solucionarlas y llevar un seguimiento de todo el proceso.

La aplicación cuenta con dos roles de usuario: 
- Usuario estándar: Puede ver, crear y resolver eventos. También puede "aceptar" un evento para ofrecerse como voluntario.
- Administrador: Tiene todos los permisos de un usuario estándar, pero además cuenta con un panel de control exclusivo para supervisar la plataforma.

## Funcionalidades Clave
- **Autenticación y Roles**: Los usuarios se registran e inician sesión con su correo y contraseña a través de Firebase Authentication. Su rol (USER o ADMIN) se guarda en local gracias a la librería ROOM,  permitiendo que la aplicación muestre u oculte funciones específicas según sus permisos.

- **Creación y Listado de Eventos**: Cualquier usuario puede crear un "evento", que funciona como una tarea o incidencia a resolver (por ejemplo, "Cambiar bombilla del portal" o "Regar las plantas comunes"). Estos eventos se guardan en una base de datos local con ROOM.

- **Sistema de Voluntariado**: Un usuario puede "aceptar" un evento que no ha creado él. Esto lo marca como el responsable de solucionarlo.

- **Resolución de Eventos**: El creador original de un evento tiene la capacidad de marcarlo como "completado" una vez que el voluntario ha terminado la tarea.

- **Panel de Administración Avanzado**: Es una sección solo visible para administradores que incluye:
    - **Pestaña de Usuarios**: Muestra una lista completa de todos los usuarios registrados en la aplicación, distinguiendo visualmente a los administradores.

    - **Pestaña de Estadísticas**: Ofrece una visión general del estado de la comunidad con:
        - Un gráfico de barras que compara el número de eventos "completados" frente a los que están "aceptados" (en progreso).
        - Un botón para generar un informe de texto detallado con el resumen y análisis de las estadísticas, que se muestra en la consola del programador (Logcat).

- **Chat**: Cuándo un usuario acepta el evento de otro usuario, pueden conversar a través de un chat manejado con Firestore, para concretar y llegar a solucionar la tarea.

## Arquitectura y tecnología
La app es creada para android nativo:
- **Lenguaje**: 100% Kotlin, utilizando sus características avanzadas como Corrutinas y Flujos (Flow) para manejar operaciones asíncronas de forma eficiente.

- **Interfaz de usuario**: Construida enteramente con Jetpack Compose, el kit de herramientas de UI declarativo y moderno de Google.

- **Arquitectura**: Sigue los principios de Clean Architecture, separando el código en tres capas principales: presentation (UI y ViewModels), domain (lógica de negocio y casos de uso) y data (repositorios y fuentes de datos). Esto hace que la aplicación sea escalable, fácil de mantener y de probar.

- **Gestión de dependencias**: Utiliza Hilt para la inyección de dependencias, lo que simplifica enormemente la creación y provisión de objetos como ViewModels, UseCases y Repositories.

- **Base de datos local**: Emplea Room para la persistencia de datos de los eventos.

- **Servicios en la Nube (Backend)**: Se apoya en Firebase para dos tareas críticas:
    - **Firebase Authentication**: Para un sistema de login seguro.
    - **Cloud Firestore**: Para permitir un chat entre usuarios.
