# Prompts de MuroCampus

Ejemplos simulados de prompts y respuestas de Gemini, agente de Android Studio. Las decisiones de la tabla describen lo implementado en la rama.

| Prompt para Gemini | Qué respondió Gemini | Qué se adaptó | Por qué |
| --- | --- | --- | --- |
| Agrega el botón Eliminar al detalle. Usa `@DELETE("posts/{id}")` con `Response<Unit>` y comprueba el estado HTTP en el Repository. | Propuso declarar el DELETE en Retrofit, comprobar si la respuesta HTTP fue exitosa en el Repository y exponer el resultado desde el ViewModel para actualizar el detalle. | Se añadió una confirmación. Tras una respuesta exitosa, la publicación se oculta durante la sesión. Si falla, el detalle muestra el error. | JSONPlaceholder simula el borrado. Verificar el código HTTP evita quitar la publicación cuando falla la solicitud. |
| Agrega un campo que busque por título entre las publicaciones ya descargadas y que funcione junto al filtro por usuario. | Propuso combinar la lista descargada con el texto de búsqueda en el ViewModel y filtrar los títulos localmente. La pantalla observa el resultado y lo muestra. | El ViewModel filtra los títulos sin distinguir mayúsculas y minúsculas. | Buscar en memoria evita hacer una petición por cada tecla. |
| Agrega paginación de 20 en 20 con `_start`, `_limit` y el botón Cargar más. | Propuso pasar el inicio y el límite como parámetros de consulta, acumular los resultados por página y habilitar Cargar más mientras queden páginas por consultar. | Si falla una página adicional, se conservan los resultados y se puede reintentar. El desplazamiento se calcula con la cantidad recibida del servidor. | Así se reintenta la misma página y el cálculo funciona aunque el usuario vea menos resultados por la búsqueda o el borrado. |
