package com.tecsup.barzola.centro_de_tareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.barzola.centro_de_tareas.data.TareasRepositoryLocal
import com.tecsup.barzola.centro_de_tareas.ui.PantallaTareas
import com.tecsup.barzola.centro_de_tareas.ui.theme.BarzolaTheme
import com.tecsup.barzola.centro_de_tareas.viewmodel.TareasViewModel
import com.tecsup.barzola.centro_de_tareas.viewmodel.TareasViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val factory = TareasViewModelFactory(TareasRepositoryLocal())
        setContent {
            BarzolaTheme(dynamicColor = false) {
                val vm: TareasViewModel = viewModel(factory = factory)
                PantallaTareas(vm = vm)
            }
        }
    }
}
