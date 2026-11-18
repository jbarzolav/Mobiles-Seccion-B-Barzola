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
    val progreso by vm.progreso.collectAsState()
    val corriendo by vm.corriendo.collectAsState()
    val primos by vm.primos.collectAsState()
    val carga by vm.carga.collectAsState()
    val simularError by vm.simularError.collectAsState()
    var limite by rememberSaveable { mutableStateOf("500000") }
    var toques by rememberSaveable { mutableStateOf(0) }

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
            OutlinedButton(onClick = { toques++ }) {
                Text("Tócame: $toques")
            }

            TarjetaTarea("Tarea con progreso") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { vm.tareaLarga() }, enabled = !corriendo) {
                        Text("Iniciar")
                    }
                    OutlinedButton(onClick = { vm.cancelar() }, enabled = corriendo) {
                        Text("Cancelar")
                    }
                }
                Text(mensaje)
                LinearProgressIndicator(
                    progress = { progreso },
                    modifier = Modifier.fillMaxWidth()
                )
                Text("${(progreso * 100).toInt()} %")
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
                val hastaInt = limite.toIntOrNull() ?: 0
                val esValido = hastaInt >= 2
                val calculandoPrimos = primos is PrimosUi.Calculando

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { vm.contar(hastaInt) },
                        enabled = esValido && !calculandoPrimos
                    ) {
                        Text("Contar")
                    }
                    OutlinedButton(
                        onClick = { vm.cancelarPrimos() },
                        enabled = calculandoPrimos
                    ) {
                        Text("Cancelar")
                    }
                }
                when (val p = primos) {
                    is PrimosUi.Inicial -> Text("Ingresa un límite para contar los números primos")
                    is PrimosUi.Calculando -> {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                        Text("Calculando primos...")
                    }
                    is PrimosUi.Listo -> {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Primos hasta $limite: ${p.cantidad}")
                                Text("Tiempo: ${p.milisegundos} ms")
                            }
                        }
                    }
                    is PrimosUi.Cancelado -> Text("Cálculo cancelado")
                }
            }

            TarjetaTarea("Dos cargas") {
                val cargando = carga is CargaUi.Cargando
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { vm.cargarUnaPorUna() },
                        enabled = !cargando
                    ) {
                        Text("Una por una")
                    }
                    Button(
                        onClick = { vm.cargarALaVez() },
                        enabled = !cargando
                    ) {
                        Text("A la vez")
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Switch(
                        checked = simularError,
                        onCheckedChange = { vm.cambiarSimularError(it) },
                        enabled = !cargando
                    )
                    Text("Simular error")
                }
                when (val c = carga) {
                    is CargaUi.Inicial -> Text("Compara los tiempos de ambas cargas")
                    is CargaUi.Cargando -> {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is CargaUi.Exito -> {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    "Modo: ${c.modo}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text("Perfil: ${c.perfil}")
                                Text("Pedidos: ${c.pedidos}")
                                Text("Tiempo: ${c.milisegundos} ms")
                            }
                        }
                    }
                    is CargaUi.Error -> {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(c.mensaje)
                                Button(onClick = { vm.cargarALaVez() }) {
                                    Text("Reintentar")
                                }
                            }
                        }
                    }
                }
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
