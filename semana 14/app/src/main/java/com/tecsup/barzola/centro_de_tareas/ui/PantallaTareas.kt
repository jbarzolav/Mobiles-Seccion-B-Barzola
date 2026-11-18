package com.tecsup.barzola.centro_de_tareas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.barzola.centro_de_tareas.model.CargaUi
import com.tecsup.barzola.centro_de_tareas.model.PrimosUi
import com.tecsup.barzola.centro_de_tareas.viewmodel.TareasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(vm: TareasViewModel, modifier: Modifier = Modifier) {
    val mensaje by vm.mensaje.collectAsState()
    val primos by vm.primos.collectAsState()
    val carga by vm.carga.collectAsState()
    var limite by rememberSaveable { mutableStateOf("500000") }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("CentroTareas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // TODO Paso 1: contador de toques y evento para incrementarlo.
            OutlinedButton(onClick = { /* TODO Paso 1 */ }, enabled = false) {
                Text("Tócame: 0")
            }

            TarjetaTarea("Tarea con progreso") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // TODO Paso 1: conectar tareaLargaMala().
                    // TODO Paso 2: cambiar a tareaLarga() y renombrar el botón.
                    Button(onClick = { /* TODO Paso 1 */ }, enabled = false) {
                        Text("Tarea larga (MAL)")
                    }
                    // TODO Paso 4: conectar cancelar() y habilitar mientras corre.
                    OutlinedButton(onClick = { /* TODO Paso 4 */ }, enabled = false) {
                        Text("Cancelar")
                    }
                }
                Text(mensaje)
                // TODO Paso 3: LinearProgressIndicator y porcentaje desde vm.progreso.
                // TODO Paso 4: estados de los botones desde vm.corriendo.
            }

            TarjetaTarea("Contar números primos") {
                OutlinedTextField(
                    value = limite,
                    onValueChange = { limite = it },
                    label = { Text("Hasta qué número") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // TODO Paso 5: validar con toIntOrNull(), mínimo 2; vm.contar(hasta).
                    Button(onClick = { /* TODO Paso 5 */ }, enabled = false) {
                        Text("Contar")
                    }
                    OutlinedButton(onClick = { /* TODO Paso 5 */ }, enabled = false) {
                        Text("Cancelar")
                    }
                }
                if (primos is PrimosUi.Inicial) {
                    Text("Ingresa un límite para contar los números primos")
                }
                // TODO Paso 5: dibujar Calculando, Listo y Cancelado según primos.
            }

            TarjetaTarea("Dos cargas") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { /* TODO Paso 6 */ }, enabled = false) {
                        Text("Una por una")
                    }
                    Button(onClick = { /* TODO Paso 6 */ }, enabled = false) {
                        Text("A la vez")
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // TODO Paso 7: observar vm.simularError y conectar su evento.
                    Switch(checked = false, onCheckedChange = null, enabled = false)
                    Text("Simular error")
                }
                if (carga is CargaUi.Inicial) {
                    Text("Compara los tiempos de ambas cargas")
                }
                // TODO Paso 6: Cargando y Exito (modo, perfil, pedidos y tiempo).
                // TODO Paso 7: Error con botón Reintentar.
            }
        }
    }
}

@Composable
private fun TarjetaTarea(
    titulo: String,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                titulo,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            contenido()
        }
    }
}
