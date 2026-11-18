package com.tecsup.barzola_campusgps.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.barzola_campusgps.model.LugarConDistancia
import com.tecsup.barzola_campusgps.util.formatearDistancia

@Composable
fun ListaLugares(
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    onSeleccionar: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(lugares) { item ->
            val esSeleccionado = item.lugar.id == seleccionado
            Card(
                onClick = { onSeleccionar(item.lugar.id) },
                colors = CardDefaults.cardColors(
                    containerColor = if (esSeleccionado)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.lugar.nombre,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = item.lugar.categoria,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    val textoDistancia = if (item.metros != null) {
                        formatearDistancia(item.metros)
                    } else {
                        "Sin calcular"
                    }
                    Text(
                        text = textoDistancia,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}