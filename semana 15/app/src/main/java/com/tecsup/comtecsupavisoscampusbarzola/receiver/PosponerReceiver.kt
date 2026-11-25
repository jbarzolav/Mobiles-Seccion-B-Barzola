package com.tecsup.comtecsupavisoscampusbarzola.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.AlarmManager
import android.app.PendingIntent
import android.os.SystemClock
import androidx.core.app.NotificationManagerCompat
import com.tecsup.comtecsupavisoscampusbarzola.model.Aviso
import com.tecsup.comtecsupavisoscampusbarzola.model.Origen
import com.tecsup.comtecsupavisoscampusbarzola.notificacion.Notificador

class PosponerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getIntExtra(EXTRA_ID, -1)
        val titulo = intent.getStringExtra(EXTRA_TITULO)
        val texto = intent.getStringExtra(EXTRA_TEXTO)
        if (id < 0 || titulo == null || texto == null) return

        when (intent.action) {
            ACTION_POSPONER -> {
                NotificationManagerCompat.from(context).cancel(id)
                val mostrarIntent = Intent(context, PosponerReceiver::class.java).apply {
                    action = ACTION_MOSTRAR
                    putExtra(EXTRA_ID, id)
                    putExtra(EXTRA_TITULO, titulo)
                    putExtra(EXTRA_TEXTO, texto)
                }
                val mostrarPendiente = PendingIntent.getBroadcast(
                    context,
                    id,
                    mostrarIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                val alarma = context.getSystemService(AlarmManager::class.java)
                alarma.setAndAllowWhileIdle(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + 10_000,
                    mostrarPendiente
                )
            }

            ACTION_MOSTRAR -> Notificador.mostrar(
                context,
                Aviso(
                    id = id,
                    titulo = titulo,
                    texto = texto,
                    origen = Origen.RECORDATORIO,
                    hora = ""
                )
            )
        }
    }

    companion object {
        const val ACTION_POSPONER = "com.tecsup.avisoscampus.action.POSPONER"
        const val ACTION_MOSTRAR = "com.tecsup.avisoscampus.action.MOSTRAR_POSPUESTO"
        const val EXTRA_ID = "com.tecsup.avisoscampus.extra.POSPONER_ID"
        const val EXTRA_TITULO = "com.tecsup.avisoscampus.extra.POSPONER_TITULO"
        const val EXTRA_TEXTO = "com.tecsup.avisoscampus.extra.POSPONER_TEXTO"
    }
}
