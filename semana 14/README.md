# CentroTareas

Aplicación Android desarrollada con Kotlin, Jetpack Compose y Material 3. Utiliza MVVM + Repository para ejecutar tareas largas sin bloquear la interfaz.

## Observación del Paso 1

La primera versión utilizaba `Thread.sleep(4000)` en el hilo principal. Durante esos cuatro segundos, la pantalla quedaba bloqueada y el contador **Tócame** no podía actualizarse visualmente.

El mensaje **Trabajando...** no llegaba a dibujarse porque el mismo hilo encargado de actualizar la pantalla estaba ocupado con la espera. Cuando terminaba el bloqueo, el mensaje cambiaba a **Terminó** y la interfaz volvía a responder.

Si la espera fuera de diez segundos, Android podría mostrar un aviso ANR (*Application Not Responding*, «La aplicación no responde»), según las condiciones y los tiempos de espera del sistema.


