package com.tecsup.comtecsupmurocampusbarzola.data

import com.tecsup.comtecsupmurocampusbarzola.data.remote.PostApi
import com.tecsup.comtecsupmurocampusbarzola.model.Comentario
import com.tecsup.comtecsupmurocampusbarzola.model.NuevoPost
import com.tecsup.comtecsupmurocampusbarzola.model.Post
import kotlin.coroutines.cancellation.CancellationException
import retrofit2.HttpException

interface PostRepository {
    suspend fun listar(inicio: Int, limite: Int, userId: Int?): Result<List<Post>>
    suspend fun detalle(id: Int): Result<Post>
    suspend fun comentarios(id: Int): Result<List<Comentario>>
    suspend fun crear(nuevo: NuevoPost): Result<Post>
    suspend fun eliminar(id: Int): Result<Unit>
}

class PostRepositoryRemote(private val api: PostApi) : PostRepository {

    private suspend fun <T> llamar(bloque: suspend () -> T): Result<T> {
        return try {
            Result.success(bloque())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun listar(inicio: Int, limite: Int, userId: Int?): Result<List<Post>> =
        llamar { api.listar(inicio, limite, userId) }

    override suspend fun detalle(id: Int): Result<Post> = llamar { api.detalle(id) }

    override suspend fun comentarios(id: Int): Result<List<Comentario>> = llamar { api.comentarios(id) }


    override suspend fun crear(nuevo: NuevoPost): Result<Post> = llamar { api.crear(nuevo) }

    override suspend fun eliminar(id: Int): Result<Unit> = llamar {
        val respuesta = api.eliminar(id)
        if (!respuesta.isSuccessful) throw HttpException(respuesta)
    }
}
