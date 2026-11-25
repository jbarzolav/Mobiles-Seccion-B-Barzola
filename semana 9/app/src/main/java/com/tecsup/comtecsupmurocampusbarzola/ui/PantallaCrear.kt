package com.tecsup.comtecsupmurocampusbarzola.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.comtecsupmurocampusbarzola.viewmodel.PostsViewModel
import com.tecsup.comtecsupmurocampusbarzola.viewmodel.UiState

@Composable
fun PantallaCrear(viewModel: PostsViewModel, onVolver: () -> Unit) {
    var titulo by rememberSaveable { mutableStateOf("") }
    var contenido by rememberSaveable { mutableStateOf("") }
    val estado by viewModel.publicacion.collectAsStateWithLifecycle()
    val publicando = estado == UiState.Cargando

    BackHandler(onBack = onVolver)
    LaunchedEffect(estado) {
        if (estado is UiState.Exito) {
            titulo = ""
            contenido = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextButton(onClick = onVolver) { Text("Volver") }
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            singleLine = true,
            enabled = !publicando,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = contenido,
            onValueChange = { contenido = it },
            label = { Text("Contenido") },
            minLines = 4,
            enabled = !publicando,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { viewModel.publicar(titulo, contenido) },
            enabled = !publicando && titulo.isNotBlank() && contenido.isNotBlank()
        ) {
            Text("Publicar")
        }

        when (val resultado = estado) {
            null -> Unit
            UiState.Cargando -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            is UiState.Exito -> Text("Publicado con id ${resultado.datos.id}")
            is UiState.Error -> Text(resultado.mensaje, color = MaterialTheme.colorScheme.error)
        }
    }
}
