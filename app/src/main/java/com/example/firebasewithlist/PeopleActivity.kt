package com.example.firebasewithlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebasewithlist.databinding.ActivityPeopleBinding
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_people.*

class PeopleActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPeopleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeopleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = FirebaseDatabase.getInstance("https://fir-datatesting-85dcc-default-rtdb.firebaseio.com/").reference

        val name = intent.getStringExtra("name")
        val replied = intent.getStringExtra("replied")
        val phoneNumber = intent.getStringExtra("phoneNumber")

        binding.tvFirstname.text = name
        binding.tvLastname.text = phoneNumber
        binding.tvage.text = replied

        btnDelete.setOnClickListener {
            database.child(phoneNumber.toString()).removeValue()
            finish()
        }
    }
}