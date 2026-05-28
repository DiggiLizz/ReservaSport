# ReservaSport 
**Gestión de Espacios Comunitarios y Complejos Deportivos**

Aplicación móvil nativa desarrollada en **Android** utilizando **Jetpack Compose** y **Material Design 3**. El sistema permite la visualización, filtrado y gestión de reservas de canchas deportivas de forma local, garantizando un flujo de usabilidad óptimo y accesible.

---

## 🛠️ Decisiones de Arquitectura y Persistencia Local

El proyecto aplica una separación estricta de responsabilidades (Clean Architecture Principles) dividida en módulos lógicos:
* **`data`**: Contiene los modelos de datos (`Cancha`) y el gestor de persistencia física local (`PreferenciasReserva`).
* **`ui.components`**: Componentes visuales reutilizables de alta cohesión (`CardCancha`).
* **`ui.screens`**: Pantalla contenedora principal y lógicas de estado mutables (`ReservaSportScreen`).

### 💾 Motor de Almacenamiento (SharedPreferences)
Para dar cumplimiento estricto a los requisitos de persistencia local sin depender de una conexión a internet:
1.  **Persistencia Física**: Se implementa `SharedPreferences` para escribir y recuperar de forma persistente los estados de reserva directamente en el almacenamiento interno del dispositivo.
2.  **Ciclo Bidireccional Completo**: La aplicación no solo almacena la reserva de forma reactiva, sino que permite la **modificación y cancelación** del registro (sobrescribiendo la clave local y devolviendo el complejo a su horario original) mediante diálogos contextuales adaptativos.

---

## 🎨 Criterios de Accesibilidad Digital y UX (Material 3)

Se integraron las pautas de diseño y contraste exigidas por las directrices de accesibilidad móvil:
* **Contraste Legible**: Ajuste de parejas cromáticas basadas en contenedores (`primaryContainer` para fondos claros y `onPrimaryContainer` en tonalidades oscuras), asegurando que el estado *"RESERVADA por ti"* mantenga una legibilidad perfecta.
* **Carga Cognitiva Reducida**: Implementación de filtros rápidos ergonómicos (`FilterChip`) para segmentar los datos de manera inmediata sin saturar la pantalla.
* **Entorno de Desarrollo Sano**: Inclusión de previsualizaciones estáticas completas mediante la anotación `@Preview` con soporte experimental (`@OptIn`), facilitando la documentación visual directa en el modo *Split* de Android Studio.

---

## 📦 Descarga del Entregable Funcional (APK)

El instalador compilado independiente listo para pruebas en dispositivos reales o emuladores externos se encuentra disponible directamente en el árbol de archivos de este repositorio:
* 📁 **Ruta del Instalador:** `app-debug.apk` (o puedes descargarlo desde la sección de archivos adjuntos).
