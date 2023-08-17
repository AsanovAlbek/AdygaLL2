package com.example.adygall2.presentation.fragments

import android.app.AlertDialog
import android.graphics.Color
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.snackBar(message : String) =
    Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
        .setTextColor(Color.GREEN).show()

fun Fragment.dialog(message : String) =
    AlertDialog.Builder(requireContext())
        .setMessage(message).create().show()

fun Fragment.log(message: String) {
    Log.i(tag, message)
}

fun KeyboardView.onKeyActionDefault(editText: EditText, isCaps: Boolean) {

    var caps = isCaps

    fun simpleClick(code: Int, connection: InputConnection) {
        var letter = code.toChar()
        if (caps) {
            letter = letter.uppercaseChar()
        } else {
            if (letter != 'I') {
                letter = letter.lowercaseChar()
            }
        }
        connection.commitText(letter.toString(), 1)
    }

    fun shiftClick() {
        caps = !caps
        keyboard.isShifted = caps
        invalidateAllKeys()
    }

    fun deleteClick(connection: InputConnection) = connection.deleteSurroundingText(1, 0)

    setOnKeyboardActionListener(object : OnKeyboardActionListener {
        override fun onKey(keyCode: Int, codes: IntArray) {
            val connection = editText.onCreateInputConnection(EditorInfo())
            when (keyCode) {
                Keyboard.KEYCODE_DELETE -> deleteClick(connection = connection!!)
                Keyboard.KEYCODE_SHIFT -> shiftClick()
                else -> simpleClick(code = keyCode, connection = connection!!)
            }
            connection.closeConnection()
        }
        override fun onPress(p0: Int) {}
        override fun onRelease(p0: Int) {}
        override fun onText(p0: CharSequence?) {}
        override fun swipeLeft() {}
        override fun swipeRight() {}
        override fun swipeDown() {}
        override fun swipeUp() {}
    })
}