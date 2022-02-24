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
            var phoneNumberT = phoneNumber.text.toString()

            fn.text.clear()
            phoneNumber.text.clear()

            try{
                database.child(phoneNumberT).setValue(People( fnT, "no replied", phoneNumberT.toLong() ) )
            }catch (error: Exception){
                print("error #1")
                print(fnT)
                print(phoneNumberT)
                println(error)
            }

            finish()
        }

    }
}