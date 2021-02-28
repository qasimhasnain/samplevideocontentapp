package com.challenge.starzplay.Utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.challenge.starzplay.Interface.AlertDialogInterface


class Utils {
    companion object {
        @Volatile private var instance: Utils? = null
        fun getInstance() =
                instance ?: synchronized(this) {
                    instance ?: Utils().also { instance = it }
                }
    }

    fun showSingleButtonAlert(context : Context, title : String, btText : String, message: String, alertDialogInterface: AlertDialogInterface){
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage(message)
                .setCancelable(true)
                .setPositiveButton(btText, DialogInterface.OnClickListener { dialog, _ ->
                    alertDialogInterface.onOK()
                    dialog.dismiss()
                })
        val alert = dialogBuilder.create()
        alert.setTitle(title)
        alert.show()
    }
    fun showTwoButtonAlert(context : Context, title:String, bt1Text : String, bt2Text : String, message: String, alertDialogInterface: AlertDialogInterface){
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(bt1Text, DialogInterface.OnClickListener { _, _ ->
                    alertDialogInterface.onRetry()
                })
                .setNegativeButton(bt2Text, DialogInterface.OnClickListener { _, _ ->
                    alertDialogInterface.onCancel()
                })

        val alert = dialogBuilder.create()
        alert.setTitle(title)
        alert.show()
    }
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}