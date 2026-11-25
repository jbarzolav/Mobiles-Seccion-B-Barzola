package com.tecsup.comtecsupavisoscampusbarzola

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.tecsup.comtecsupavisoscampusbarzola.data.AvisosRepositoryMemoria
import com.tecsup.comtecsupavisoscampusbarzola.ui.PantallaAvisos
import com.tecsup.comtecsupavisoscampusbarzola.ui.theme.ComtecsupavisoscampusBarzolaTheme
import com.tecsup.comtecsupavisoscampusbarzola.viewmodel.AvisosViewModel
import com.tecsup.comtecsupavisoscampusbarzola.viewmodel.AvisosViewModelFactory

class MainActivity : ComponentActivity() {
    private val repository = AvisosRepositoryMemoria()
    private val viewModel: AvisosViewModel by viewModels {
        AvisosViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComtecsupavisoscampusBarzolaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PantallaAvisos(vm = viewModel)
                }
            }
        }
    }
}
