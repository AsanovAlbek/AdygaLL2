package com.example.adygall2.presentation.fragments

import android.app.AlertDialog
import android.graphics.Color
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.adygall2.presentation.fragments.menu.FragmentGamePage
import com.google.android.material.snackbar.Snackbar
import com.xwray.groupie.GroupieAdapter

fun Fragment.snackBar(message : String) =
    Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
        .setTextColor(Color.GREEN).show()

fun Fragment.dialog(message : String) =
    AlertDialog.Builder(requireContext())
        .setMessage(message).create().show()

fun Fragment.log(message: String) {
    Log.i(tag, message)
}