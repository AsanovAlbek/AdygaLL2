package com.example.adygall2.presentation.model

import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.EditText

class KeyboardManager(private val keyboard: KeyboardView) {
    private var inputConnection: InputConnection? = null
    private var isCaps = false

    init {
        keyboard.setOnKeyListener { _, keyCode, _ ->
            when(keyCode) {
                Keyboard.KEYCODE_DELETE -> delete()
                Keyboard.KEYCODE_SHIFT -> caps()
                else -> simpleClick(code = keyCode)
            }
            true
        }
    }

    fun connect(editText: EditText, isCaps: Boolean) {
        if (inputConnection == null) {
            inputConnection = editText.onCreateInputConnection(EditorInfo())
        }
    }

    fun closeConnect() {
        inputConnection?.closeConnection()
        inputConnection = null
    }

    private fun delete() {
        inputConnection?.deleteSurroundingText(1, 0)
    }

    private fun caps() {
        isCaps = !isCaps
    }

    private fun simpleClick(code: Int) {
        var letter = code.toChar()
        if (isCaps) {
            letter = letter.uppercaseChar()
        } else {
            if (letter != 'I') {
                letter = letter.lowercaseChar()
            }
        }
        inputConnection?.commitText(letter.toString(), 1)
    }
}