package com.tecsup.comtecsupavisoscampusbarzola.ui

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.tecsup.comtecsupavisoscampusbarzola.model.PermisoUi
import com.tecsup.comtecsupavisoscampusbarzola.notificacion.Notificador
import com.tecsup.comtecsupavisoscampusbarzola.receiver.ModoAvionReceiver
import com.tecsup.comtecsupavisoscampusbarzola.viewmodel.AvisosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAvisos(vm: AvisosViewModel) {
    val contexto = LocalContext.current
    val permiso by vm.permiso.collectAsState()
    val avisos by vm.avisos.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val faltan by vm.faltan.collectAsState()
    val escuchandoAvion by vm.escuchandoAvion.collectAsState()
    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        vm.permisoRespondido(concedido)
    }

    LaunchedEffect(Unit) {
        if (Notificador.tienePermiso(contexto)) {
            vm.permisoRespondido(true)
        }
        vm.eventos.collect { aviso ->
            Notificador.mostrar(contexto, aviso)
        }
    }

    DisposableEffect(escuchandoAvion) {
        val receptor = if (escuchandoAvion) {
            ModoAvionReceiver { activo -> vm.alCambiarModoAvion(activo) }
        } else {
            null
        }

        receptor?.let {
            ContextCompat.registerReceiver(
                contexto,
                it,
                IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }

        onDispose {
            receptor?.let(contexto::unregisterReceiver)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AvisosCampus") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tarjeta 1: Permiso de notificaciones
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Permiso de notificaciones",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    val textoEstado = when (permiso) {
                        PermisoUi.SinPedir -> "Estado: Sin pedir"
                        PermisoUi.Concedido -> "Estado: Permitido"
                        PermisoUi.Denegado -> "Estado: Denegado"
                    }
                    Text(text = textoEstado)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                lanzador.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                vm.permisoRespondido(true)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Permitir notificaciones")
                    }
                }
            }

            // Tarjeta 2: Avisos
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Avisos",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            vm.avisarAhora()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Mostrar aviso")
                    }
                }
            }

            // Tarjeta 3: Recordatorio
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Recordatorio",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                vm.recordarEn(10)
                            },
                            modifier = Modifier.weight(1f),
                            enabled = faltan == null
                        ) {
                            Text("Recordar en 10 s")
                        }
                        OutlinedButton(
                            onClick = {
                                vm.cancelarRecordatorio()
                            },
                            enabled = faltan != null
                        ) {
                            Text("Cancelar")
                        }
                    }
                    faltan?.let { segundos ->
                        Text("Faltan $segundos s")
                    }
                }
            }

            // Tarjeta 4: Receptor de anuncios
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Receptor de anuncios",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Escuchar el modo avión")
                        Switch(
                            checked = escuchandoAvion,
                            onCheckedChange = vm::cambiarEscucha
                        )
                    }
                    Text(
                        text = "Activa o desactiva el modo avión del celular",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Tarjeta 5: Historial
            HistorialAvisos(avisos = avisos)
        }
    }
}
