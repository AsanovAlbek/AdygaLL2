package com.example.adygall2.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.adygall2.OnContinueBtnListener
import com.example.adygall2.R

class Authorize(
    private val listener : OnContinueBtnListener
) : Fragment() {


    lateinit var loginEt : EditText
    lateinit var passwordEt : EditText
    lateinit var singInButton : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_authorize, container, false)

        loginEt = view.findViewById(R.id.auth_login_et)
        passwordEt = view.findViewById(R.id.auth_password_et)
        singInButton = view.findViewById(R.id.auth_sing_in_button)

        singInButton.setOnClickListener{listener.onClick()}

        return view
    }


}