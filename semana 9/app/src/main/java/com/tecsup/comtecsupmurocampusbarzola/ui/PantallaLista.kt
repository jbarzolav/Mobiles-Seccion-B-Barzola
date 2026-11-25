package com.tecsup.comtecsupmurocampusbarzola.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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

    Column(modifier = modifier.fillMaxSize()) {
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
                if (state.datos.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Sin publicaciones", style = MaterialTheme.typography.bodyLarge)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 8.dp)
                    ) {
                        items(state.datos, key = { it.id }) { post ->
                            TarjetaPost(
                                userId = post.userId,
                                title = post.title,
                                body = post.body,
                                onClick = { onPostClick(post) }
                            )
                        }
                    }
                }
            }
        }
    }
}
