# CentroTareas

Aplicación Android desarrollada con Kotlin, Jetpack Compose y Material 3. Utiliza MVVM + Repository para ejecutar tareas largas sin bloquear la interfaz.

## Observación del Paso 1

La primera versión utilizaba `Thread.sleep(4000)` en el hilo principal. Durante esos cuatro segundos, la pantalla quedaba bloqueada y el contador **Tócame** no podía actualizarse visualmente.

El mensaje **Trabajando...** no llegaba a dibujarse porque el mismo hilo encargado de actualizar la pantalla estaba ocupado con la espera. Cuando terminaba el bloqueo, el mensaje cambiaba a **Terminó** y la interfaz volvía a responder.

Si la espera fuera de diez segundos, Android podría mostrar un aviso ANR (*Application Not Responding*, «La aplicación no responde»), según las condiciones y los tiempos de espera del sistema.

## Paso 2: tarea con corrutina

Se reemplazó `tareaLargaMala()` por `tareaLarga()`, utilizando `viewModelScope.launch` y `withContext(Dispatchers.Default)`. La espera de cuatro segundos se realiza con `delay(4000)`, sin bloquear el hilo principal.

Durante la tarea aparece **Trabajando...**, el contador **Tócame** sigue sumando y, al finalizar, se muestra **Terminó**. Se comprobó que la interfaz continúa respondiendo mientras se ejecuta la tarea.

Los registros con la etiqueta **HILO** permiten identificar el cambio de contexto:

| Momento | Hilo |
| --- | --- |
| Antes de `withContext` | `main` |
| Dentro de `withContext` | `DefaultDispatcher-worker-…` |
| Después de `withContext` | `main` |

`Thread.sleep` ya no forma parte del código de la aplicación.
