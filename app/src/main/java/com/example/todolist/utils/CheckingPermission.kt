package com.example.todolist.utils

import android.Manifest.permission.POST_NOTIFICATIONS
import android.Manifest.permission.SCHEDULE_EXACT_ALARM
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.example.todolist.R
import javax.inject.Inject

/**
 * CheckingPermission - класс, который отвечает за проверку и запрос разрешений.
 */
class CheckingPermission @Inject constructor(private val context: Context) {

    fun checkPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val postNotificationsPermission = context.checkSelfPermission(POST_NOTIFICATIONS)
            val scheduleExactAlarmPermission = context.checkSelfPermission(SCHEDULE_EXACT_ALARM)

            postNotificationsPermission == PackageManager.PERMISSION_GRANTED &&
                    scheduleExactAlarmPermission == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun requestPermissions() {
        if (!checkPermissions() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissions = arrayOf(
                POST_NOTIFICATIONS,
                SCHEDULE_EXACT_ALARM,
            )

            val builder = AlertDialog.Builder(context)
            builder.setTitle(context.getString(R.string.permission_request))
            builder.setMessage(context.getString(R.string.permission_information))
            builder.setPositiveButton(context.getString(R.string.ok)) { dialog, _ ->
                dialog.dismiss()
                (context as? Activity)?.requestPermissions(permissions, PERMISSION_REQUEST_CODE)
            }
            builder.setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }
}