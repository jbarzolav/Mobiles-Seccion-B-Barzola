package com.tecsup.comtecsupmurocampusbarzola.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.comtecsupmurocampusbarzola.model.Post
import com.tecsup.comtecsupmurocampusbarzola.viewmodel.PostsViewModel
import com.tecsup.comtecsupmurocampusbarzola.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLista(
    viewModel: PostsViewModel,
    onPostClick: (Post) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val estado by viewModel.lista.collectAsStateWithLifecycle()
    val filtroActual by viewModel.usuarioFiltro.collectAsStateWithLifecycle()

    val busqueda by viewModel.busqueda.collectAsStateWithLifecycle()
    val paginacion by viewModel.paginacion.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        OutlinedTextField(
            value = busqueda,
            onValueChange = viewModel::buscar,
            label = { Text("Buscar por título") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )
        // Fila de chips desplazable para filtro por usuario
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = filtroActual == null,
                    onClick = { viewModel.filtrar(null) },
                    label = { Text("Todos") }
                )
            }
            items((1..10).toList()) { userId ->
                FilterChip(
                    selected = filtroActual == userId,
                    onClick = { viewModel.filtrar(userId) },
                    label = { Text("Usuario $userId") }
                )
            }
        }

        when (val state = estado) {
            is UiState.Cargando -> {
                Cargando(mensaje = "Cargando publicaciones...", modifier = Modifier.weight(1f))
            }
            is UiState.Error -> {
                ErrorConReintento(
                    mensaje = state.mensaje,
                    onReintentar = { viewModel.cargar() },
                    modifier = Modifier.weight(1f)
                )
            }
            is UiState.Exito -> {
                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    if (state.datos.isEmpty()) {
                        item {
                            Text(
                                text = if (busqueda.isBlank()) "Sin publicaciones" else "Sin coincidencias en lo cargado",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    items(state.datos, key = { it.id }) { post ->
                        TarjetaPost(
                            userId = post.userId,
                            title = post.title,
                            body = post.body,
                            onClick = { onPostClick(post) }
                        )
                    }

                    if (paginacion.hayMas) {
                        item {
                            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                                paginacion.error?.let { mensaje ->
                                    Text(mensaje, color = MaterialTheme.colorScheme.error)
                                }
                                Button(
                                    onClick = viewModel::cargarMas,
                                    enabled = !paginacion.cargando
                                ) {
                                    Text(when {
                                        paginacion.cargando -> "Cargando…"
                                        paginacion.error != null -> "Reintentar"
                                        else -> "Cargar más"
                                    })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
