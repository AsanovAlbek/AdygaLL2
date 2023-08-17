package com.example.adygall2.presentation.keyboard

import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener
import android.view.View
import android.view.inputmethod.InputConnection
import com.example.adygall2.R

class AdygeKeyboardIme: InputMethodService(), OnKeyboardActionListener {

    private lateinit var keyboardView: KeyboardView
    private lateinit var keyboard: Keyboard
    private var caps = false

    override fun onCreateInputView(): View {
        keyboardView = layoutInflater.inflate(R.layout.keyboard_view_adyge, null) as KeyboardView
        keyboard = Keyboard(this, R.xml.keyboard)
        keyboardView.keyboard = keyboard
        keyboardView.setOnKeyboardActionListener(this)

        return keyboardView
    }

    override fun onKey(primaryCode: Int, codes: IntArray?) {
        val connection = currentInputConnection
        when(primaryCode) {
            Keyboard.KEYCODE_DELETE -> deleteClick(connection = connection)
            Keyboard.KEYCODE_SHIFT -> shiftClick()
            else -> simpleClick(code = primaryCode, connection = connection)
        }
    }

    private fun simpleClick(code: Int, connection: InputConnection) {
        var letter = code.toChar()
        letter = if (caps) {
            letter.uppercaseChar()
        } else {
            letter.lowercaseChar()
        }
        connection.commitText(letter.toString(), 1)
    }

    private fun shiftClick() {
        caps = !caps
        keyboard.isShifted = caps
        keyboardView.invalidateAllKeys()
    }

    private fun deleteClick(connection: InputConnection) = connection.deleteSurroundingText(1, 0)

    override fun onPress(p0: Int) {

    }

    override fun onRelease(p0: Int) {

    }

    override fun onText(p0: CharSequence?) {

    }

    override fun swipeLeft() {

    }

    override fun swipeRight() {

    }

    override fun swipeDown() {

    }

    override fun swipeUp() {

    }
}