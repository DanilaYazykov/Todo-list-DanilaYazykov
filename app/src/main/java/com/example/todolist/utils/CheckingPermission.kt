package com.example.todolist.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.example.todolist.R

class CheckingPermission(private val context: Context) {
    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }

    fun checkPermissions(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val postNotificationsPermission = context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
            val scheduleExactAlarmPermission = context.checkSelfPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)

            return postNotificationsPermission == PackageManager.PERMISSION_GRANTED &&
                    scheduleExactAlarmPermission == PackageManager.PERMISSION_GRANTED
        }

        return true
    }

    fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkPermissions()) {
                val permissions = arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS,
                    Manifest.permission.SCHEDULE_EXACT_ALARM
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
    }
}