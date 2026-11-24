package com.tecsup.comtecsupmurocampusbarzola

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.tecsup.comtecsupmurocampusbarzola.data.PostRepositoryRemote
import com.tecsup.comtecsupmurocampusbarzola.data.remote.RetrofitClient
import com.tecsup.comtecsupmurocampusbarzola.ui.PantallaLista
import com.tecsup.comtecsupmurocampusbarzola.ui.theme.ComtecsupmurocampusbarzolaTheme
import com.tecsup.comtecsupmurocampusbarzola.viewmodel.PostsViewModel
import com.tecsup.comtecsupmurocampusbarzola.viewmodel.PostsViewModelFactory

class MainActivity : ComponentActivity() {

    private val repository by lazy { PostRepositoryRemote(RetrofitClient.api) }
    private val viewModel: PostsViewModel by viewModels { PostsViewModelFactory(repository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComtecsupmurocampusbarzolaTheme {
                MuroCampusApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuroCampusApp(viewModel: PostsViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MuroCampus", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            PantallaLista(viewModel = viewModel)
        }
    }
}
