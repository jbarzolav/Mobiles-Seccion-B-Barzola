package com.tecsup.comtecsupmurocampusbarzola.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.comtecsupmurocampusbarzola.data.PostRepository

// [E] Factory de ViewModel entregado
class PostsViewModelFactory(private val repository: PostRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PostsViewModel() as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}
