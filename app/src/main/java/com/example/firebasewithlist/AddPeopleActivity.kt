package com.example.firebasewithlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebasedatatesting.People
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_people.*

class AddPeopleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_people)

        val database = FirebaseDatabase.getInstance("https://fir-datatesting-85dcc-default-rtdb.firebaseio.com/").reference
//        database.setValue("test 1")

        button.setOnClickListener {

            var fnT = fn.text.toString()
            var lnT = ln.text.toString()
            var ageN = age.text.toString().toInt()

            var id = System.currentTimeMillis().toInt().toString()

            fn.text.clear()
            ln.text.clear()
            age.text.clear()

            database.child(id).setValue(People(fnT, lnT, ageN, id.toInt()))
            val intent = Intent(this, PeopleListActivity::class.java)
            startActivity(intent)
        }

    }
}