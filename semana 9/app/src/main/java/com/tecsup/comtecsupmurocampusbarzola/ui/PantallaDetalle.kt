package com.tecsup.comtecsupmurocampusbarzola.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.comtecsupmurocampusbarzola.viewmodel.PostsViewModel
import com.tecsup.comtecsupmurocampusbarzola.viewmodel.UiState

@Composable
fun PantallaDetalle(
    postId: Int,
    viewModel: PostsViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val eliminacion by viewModel.eliminacion.collectAsStateWithLifecycle()
    val eliminando = eliminacion == UiState.Cargando
    var confirmar by rememberSaveable(postId) { mutableStateOf(false) }

    BackHandler { if (!eliminando) onVolver() }

    LaunchedEffect(eliminacion, postId) {
        if ((eliminacion as? UiState.Exito)?.datos == postId) onVolver()
    }

    if (confirmar) {
        AlertDialog(
            onDismissRequest = { confirmar = false },
            title = { Text("¿Eliminar publicación?") },
            text = { Text("La publicación se ocultará de la lista durante esta sesión.") },
            confirmButton = {
                TextButton(onClick = {
                    confirmar = false
                    viewModel.eliminar(postId)
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { confirmar = false }) { Text("Cancelar") }
            }
        )
    }

    LaunchedEffect(postId) {
        viewModel.abrir(postId)
    }

    val estado by viewModel.detalle.collectAsStateWithLifecycle()

    when (val state = estado) {
        is UiState.Cargando -> {
            Cargando(mensaje = "Cargando detalle...", modifier = modifier)
        }
        is UiState.Error -> {
            ErrorConReintento(
                mensaje = state.mensaje,
                onReintentar = { viewModel.abrir(postId) },
                modifier = modifier
            )
        }
        is UiState.Exito -> {
            val post = state.datos.post
            val comentarios = state.datos.comentarios

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = post.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = post.body,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    TextButton(onClick = { confirmar = true }, enabled = !eliminando) {
                        Text("Eliminar", color = MaterialTheme.colorScheme.error)
                    }
                    when (val resultado = eliminacion) {
                        UiState.Cargando -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                        is UiState.Error -> Text(resultado.mensaje, color = MaterialTheme.colorScheme.error)
                        else -> Unit
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Comentarios (${comentarios.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(comentarios, key = { it.id }) { comentario ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = comentario.email,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = comentario.body,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}
