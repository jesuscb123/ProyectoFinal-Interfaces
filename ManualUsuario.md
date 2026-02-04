# 📘 Manual de Usuario — ManoVecina

## 1. Introducción

ManoVecina es una aplicación Android orientada a la **gestión de eventos colaborativos**, donde los usuarios pueden crear eventos, aceptar tareas, resolverlas y obtener puntos como recompensa. La aplicación ha sido diseñada siguiendo criterios de usabilidad, claridad y estabilidad, con una interfaz moderna basada en Material Design 3.

Este manual describe cómo **instalar, utilizar y comprender** el funcionamiento general de la aplicación.

---

## 2. Requisitos del sistema

Para utilizar la aplicación es necesario disponer de:

- Dispositivo Android con **Android 8.0 (API 26)** o superior.
- Conexión a Internet para autenticación y sincronización de datos.
- Cuenta de usuario registrada en la aplicación.

---

## 3. Instalación de la aplicación

### 3.1 Instalación mediante APK

1. Descargar el archivo `ProyectoFinal-v1.0.0.apk`.
2. Copiar el archivo al dispositivo Android.
3. Activar la opción **“Permitir instalación de aplicaciones de orígenes desconocidos”**.
4. Abrir el archivo APK y seguir las instrucciones del instalador.
5. Una vez completada la instalación, abrir la aplicación desde el menú principal.

La aplicación se distribuye como **APK firmado en modo release**, garantizando su integridad y correcto funcionamiento.

---

## 4. Primer inicio de la aplicación

Al iniciar la aplicación por primera vez, el usuario accede a la pantalla de **autenticación**, donde puede:

- Iniciar sesión con una cuenta existente.
- Registrarse creando una nueva cuenta.

Tras completar el proceso de autenticación, se accede automáticamente a la pantalla principal.

---

## 5. Uso de la aplicación

### 5.1 Pantalla principal (Inicio)

En la pantalla de inicio se muestran los eventos disponibles. Desde aquí el usuario puede:

- Visualizar eventos creados por otros usuarios.
- Aceptar eventos disponibles.
- Crear un nuevo evento mediante el botón flotante de acción (+).

---

### 5.2 Crear un evento

Para crear un evento:

1. Pulsar el botón **“Crear evento”**.
2. Introducir el título del evento.
3. Añadir una descripción opcional.
4. Seleccionar una categoría (Comunidad o Personal).
5. Confirmar la creación.

El evento quedará disponible para que otros usuarios puedan aceptarlo.

---

### 5.3 Eventos aceptados

En la sección **“Eventos aceptados”**, el usuario puede:

- Consultar los eventos que ha aceptado.
- Marcar un evento como resuelto cuando se complete.
- Acceder a las funcionalidades asociadas al evento.

---

### 5.4 Eventos creados

Desde la sección **“Mis eventos”**, el usuario puede:

- Ver los eventos que ha creado.
- Consultar su estado (pendiente, aceptado o resuelto).
- Eliminar eventos si es necesario.

---

### 5.5 Sistema de puntos

Cada vez que un usuario completa un evento aceptado, recibe una **recompensa en puntos**.  
Los puntos acumulados pueden consultarse desde la pantalla **“Mis puntos”**.

---

### 5.6 Funcionalidades de administrador

Los usuarios con rol de **Administrador** disponen de una sección adicional desde la que pueden:

- Visualizar estadísticas globales.
- Consultar el número de eventos aceptados y resueltos.
- Gestionar la información general de la aplicación.

---

## 6. Cierre de sesión

El usuario puede cerrar sesión en cualquier momento desde la pantalla principal.  
Al cerrar sesión, la aplicación vuelve automáticamente a la pantalla de autenticación.

---

# ⚙️ Manual de Instalación Técnica

## 7. Instalación para desarrollo

### Requisitos técnicos

- Android Studio actualizado.
- JDK 17 o superior.
- Android SDK configurado.
- Proyecto Firebase configurado (Authentication).

### Pasos de instalación

1. Clonar el repositorio del proyecto.
2. Abrir el proyecto en Android Studio.
3. Sincronizar las dependencias de Gradle.
4. Ejecutar la aplicación en un emulador o dispositivo físico.
5. Generar el APK o AAB desde el menú:
   Build > Generate Signed Bundle / APK

---

## 8. Configuración adicional

- La aplicación utiliza **Firebase Authentication** para la gestión de usuarios.
- La persistencia local se realiza mediante **Room**.
- La inyección de dependencias se gestiona con **Hilt**.

---

# 🛠️ Manual Técnico Básico

## 9. Arquitectura del sistema

La aplicación sigue una arquitectura **MVVM + Clean Architecture**, separando claramente:

- Capa de presentación (UI + ViewModel).
- Capa de dominio (Casos de uso).
- Capa de datos (Repositorios, Room, Firebase).

Esta separación facilita el mantenimiento, la escalabilidad y la realización de pruebas unitarias.

---

## 10. Pruebas

El proyecto incluye **pruebas unitarias** centradas en:

- Casos de uso.
- ViewModels.

Se utilizan herramientas como:
- JUnit
- MockK
- Coroutines Test
- Turbine

---

## 11. Mantenimiento y ampliación

La estructura modular del proyecto permite:

- Añadir nuevas funcionalidades de forma sencilla.
- Integrar servicios externos.
- Escalar la aplicación manteniendo la estabilidad.

---

## 12. Conclusión

Este manual proporciona una guía completa para la instalación, uso y comprensión de la aplicación.  
El proyecto ha sido desarrollado siguiendo criterios profesionales, con una base sólida para futuras mejoras.

---

## Relación con la rúbrica

- RA6.e — Manual de usuario y guía de referencia
- RA6.f — Manual técnico de instalación y configuración
- RA6.g — Tutoriales
- RA7.a — Empaquetado de la aplicación
- RA7.c — Paquete generado desde el entorno
