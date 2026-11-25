package com.tecsup.comtecsupavisoscampusbarzola

import android.app.Application
import com.tecsup.comtecsupavisoscampusbarzola.notificacion.Notificador

class AvisosApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Notificador.crearCanal(this)
    }
}
