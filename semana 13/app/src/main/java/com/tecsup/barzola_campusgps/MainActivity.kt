package com.tecsup.barzola_campusgps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.barzola_campusgps.data.LugaresRepositoryLocal
import com.tecsup.barzola_campusgps.data.PreferenciasRepository
import com.tecsup.barzola_campusgps.data.UbicacionRepositoryGoogle
import com.tecsup.barzola_campusgps.ui.PantallaMapa
import com.tecsup.barzola_campusgps.ui.theme.Barzola_campusgpsTheme
import com.tecsup.barzola_campusgps.viewmodel.CampusViewModelFactory
import com.tecsup.barzola_campusgps.viewmodel.MapaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val factory = CampusViewModelFactory(
            ubicacionRepo = UbicacionRepositoryGoogle(applicationContext),
            lugaresRepo = LugaresRepositoryLocal(),
            preferenciasRepo = PreferenciasRepository(applicationContext)
        )

        setContent {
            Barzola_campusgpsTheme {
                val vm: MapaViewModel = viewModel(factory = factory)
                PantallaMapa(vm = vm)
            }
        }
    }
}
