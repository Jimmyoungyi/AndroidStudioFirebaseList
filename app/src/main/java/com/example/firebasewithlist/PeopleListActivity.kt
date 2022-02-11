package com.example.firebasewithlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebasedatatesting.People
import com.example.firebasewithlist.databinding.ActivityPeopleListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PeopleListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPeopleListBinding
    private lateinit var userArrayList: ArrayList<People>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeopleListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = FirebaseDatabase.getInstance("https://fir-datatesting-85dcc-default-rtdb.firebaseio.com/").reference
        userArrayList = ArrayList()
        val getData = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                userArrayList.clear()
                for (i in p0.children){
                    val people = People(
                        i.child("firstname").getValue().toString(),
                        i.child("lastname").getValue().toString(),
                        Integer.parseInt(i.child("age").getValue().toString()),
                        Integer.parseInt(i.child("id").getValue().toString()))
                    userArrayList.add(people)
                }

                binding.lvPeopleList.isClickable = true
                binding.lvPeopleList.adapter = UserListAdapter(this@PeopleListActivity, userArrayList)
                binding.lvPeopleList.setOnItemClickListener { _, _, position, _->
                    val firstname = userArrayList[position].firstname
                    val lastname = userArrayList[position].lastname
                    val age = userArrayList[position].age.toString()
                    val id = userArrayList[position].id.toString()

                    val i = Intent(this@PeopleListActivity, PeopleActivity::class.java)
                    i.putExtra("firstname", firstname)
                    i.putExtra("lastname", lastname)
                    i.putExtra("age",age)
                    i.putExtra("id",id)
                    startActivity(i)
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        }
        database.addValueEventListener(getData)
        database.addListenerForSingleValueEvent(getData)

    }
}