package com.tecsup.comtecsupavisoscampusbarzola.notificacion

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.tecsup.comtecsupavisoscampusbarzola.model.Aviso
import com.tecsup.comtecsupavisoscampusbarzola.model.Origen
import com.tecsup.comtecsupavisoscampusbarzola.R
import com.tecsup.comtecsupavisoscampusbarzola.MainActivity
import com.tecsup.comtecsupavisoscampusbarzola.receiver.PosponerReceiver

object Notificador {
    const val CANAL = "avisos"
    const val CANAL_URGENTES = "urgentes"

    fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                CANAL,
                "Avisos",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val canalUrgente = NotificationChannel(
                CANAL_URGENTES,
                "Urgentes",
                NotificationManager.IMPORTANCE_HIGH
            )
            val gestor = context.getSystemService(NotificationManager::class.java)
            gestor.createNotificationChannel(canal)
            gestor.createNotificationChannel(canalUrgente)
        }
    }

    fun tienePermiso(context: Context): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
    }

    fun mostrar(context: Context, aviso: Aviso): Boolean {
        if (!tienePermiso(context)) return false

        val intent = Intent(context, MainActivity::class.java)
        val pendiente = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val posponerIntent = Intent(context, PosponerReceiver::class.java).apply {
            action = PosponerReceiver.ACTION_POSPONER
            putExtra(PosponerReceiver.EXTRA_ID, aviso.id)
            putExtra(PosponerReceiver.EXTRA_TITULO, aviso.titulo)
            putExtra(PosponerReceiver.EXTRA_TEXTO, aviso.texto)
        }
        val posponerPendiente = PendingIntent.getBroadcast(
            context,
            aviso.id,
            posponerIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val canal = if (aviso.origen == Origen.RECORDATORIO) {
            CANAL_URGENTES
        } else {
            CANAL
        }
        val prioridad = if (aviso.origen == Origen.RECORDATORIO) {
            NotificationCompat.PRIORITY_HIGH
        } else {
            NotificationCompat.PRIORITY_DEFAULT
        }

        val notificacion = NotificationCompat.Builder(context, canal)
            .setSmallIcon(R.drawable.ic_notificacion)
            .setContentTitle(aviso.titulo)
            .setContentText(aviso.texto)
            .setStyle(NotificationCompat.BigTextStyle().bigText(aviso.texto))
            .setPriority(prioridad)
            .setContentIntent(pendiente)
            .addAction(
                R.drawable.ic_notificacion,
                "Posponer 10 s",
                posponerPendiente
            )
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(aviso.id, notificacion)
        return true
    }
}
