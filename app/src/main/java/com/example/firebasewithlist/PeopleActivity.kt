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

        val firstname = intent.getStringExtra("firstname")
        val lastname = intent.getStringExtra("lastname")
        val age = intent.getStringExtra("age")
        val id = intent.getStringExtra("id")

        binding.tvFirstname.text = firstname
        binding.tvLastname.text = lastname
        binding.tvage.text = age

        btnDelete.setOnClickListener {
            database.child(id.toString()).removeValue()
            val intent = Intent(this, PeopleListActivity::class.java)
            startActivity(intent)
        }
    }
}