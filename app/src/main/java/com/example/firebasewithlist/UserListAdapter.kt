package com.example.firebasewithlist

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.firebasedatatesting.People


class UserListAdapter(private val context: Activity, private val arrayList: ArrayList<People>) : ArrayAdapter<People>(context, R.layout.people_item, arrayList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.people_item, null)

        val name = view.findViewById<TextView>(R.id.tvName)
        val phoneNumber = view.findViewById<TextView>(R.id.tvPhoneNumber)
        val replied = view.findViewById<TextView>(R.id.tvReplied)

        name.text = arrayList[position].name
        phoneNumber.text = arrayList[position].phoneNumber.toString()
        replied.text = arrayList[position].replied

        return view
    }
}