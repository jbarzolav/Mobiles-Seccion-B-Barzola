package com.tecsup.comtecsupmurocampusbarzola.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.comtecsupmurocampusbarzola.model.Post
import com.tecsup.comtecsupmurocampusbarzola.viewmodel.PostsViewModel
import com.tecsup.comtecsupmurocampusbarzola.viewmodel.UiState

@Composable
fun PantallaLista(
    viewModel: PostsViewModel,
    onPostClick: (Post) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val estado by viewModel.lista.collectAsStateWithLifecycle()

    when (val state = estado) {
        is UiState.Cargando -> {
            Cargando(mensaje = "Cargando publicaciones...", modifier = modifier)
        }
        is UiState.Error -> {
            ErrorConReintento(
                mensaje = state.mensaje,
                onReintentar = { viewModel.cargar() },
                modifier = modifier
            )
        }
        is UiState.Exito -> {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
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
