package com.example.adygall2.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.adygall2.OnContinueBtnListener
import com.example.adygall2.R

class HomePage(
    private val listener : OnContinueBtnListener
) : Fragment() {

    private lateinit var flag : LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view : View = inflater.inflate(R.layout.fragment_home_page, container, false)

        flag = view.findViewById(R.id.test_flag)
        flag.setOnClickListener {
            listener.onClick()
        }

        return view
    }
}