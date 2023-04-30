package com.example.adygall2.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adygall2.R
import com.example.adygall2.databinding.ContactItemBinding
import com.example.adygall2.presentation.adapters.groupieitems.model.ContactItem

class ContactsAdapter(
    private val contacts: List<ContactItem>
): RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {
    inner class ContactsViewHolder(
        private val contactItem: ContactItemBinding
    ): RecyclerView.ViewHolder(contactItem.root) {
        fun bind(contact: ContactItem) {
            contactItem.apply {
                contactImage.setImageResource(contact.companyImageId)
                companyEmail.text = contact.email
                companyNumber.text = contact.telNumber
                companyName.text = contact.company
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder =
        ContactsViewHolder(
            ContactItemBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.contact_item, parent, false
                )
            )
        )

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    override fun getItemCount(): Int = contacts.size
}