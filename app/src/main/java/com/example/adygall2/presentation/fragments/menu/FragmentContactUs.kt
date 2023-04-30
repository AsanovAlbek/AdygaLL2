package com.example.adygall2.presentation.fragments.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.adygall2.R
import com.example.adygall2.databinding.FragmentContactUsBinding
import com.example.adygall2.presentation.adapters.ContactsAdapter
import com.example.adygall2.presentation.adapters.groupieitems.model.ContactItem

class FragmentContactUs: Fragment() {
    private var _binding: FragmentContactUsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactUsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val contacts = mutableListOf<ContactItem>(
            ContactItem(
                company = "Адыгейский институт",
                companyImageId = R.drawable.logo,
                email = "adyga@gmail.com",
                telNumber = "8-900-900-00-00"
            ),
            ContactItem(
                company = "КБНЦ РАН",
                companyImageId = R.drawable.logo,
                email = "kbnc@gmail.com",
                telNumber = "8-900-900-07-07"
            )
        )
        val adapter = ContactsAdapter(contacts)
        binding.contactItems.adapter = adapter
    }
}