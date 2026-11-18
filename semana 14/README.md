# CentroTareas

Aplicación de laboratorio con Kotlin, Jetpack Compose y MVVM + Repository para estudiar tareas largas y corrutinas en Android.

## Observación del Paso 1

### Comportamiento esperado por el código

Al pulsar **Tarea larga (MAL)**, `Thread.sleep(4000)` bloquea el hilo principal durante unos cuatro segundos. Durante ese intervalo, el contador **Tócame** no se actualiza visualmente y la pantalla no responde con normalidad. Los eventos de entrada pueden procesarse después de que termine el bloqueo.

Aunque el ViewModel asigna **Trabajando...**, el hilo principal está ocupado y no puede dibujar ese cambio. Al terminar la espera, el mensaje cambia a **Terminó**, y la interfaz vuelve a poder actualizarse.

Si el bloqueo durara diez segundos, Android podría mostrar un aviso ANR (Application Not Responding, «La aplicación no responde»), dependiendo del contexto y de los tiempos de espera del sistema.

### Verificación manual pendiente

- [ ] Comprobar que Tócame aumenta el contador antes de iniciar la tarea.
- [ ] Pulsar Tarea larga (MAL) e intentar tocar el contador durante la espera.
- [ ] Confirmar que Trabajando... no llega a dibujarse y aparece Terminó al finalizar.
- [ ] Registrar aquí el resultado observado y adjuntar una captura.

`Thread.sleep` se usa únicamente para esta demostración y debe eliminarse en el paso 2.
