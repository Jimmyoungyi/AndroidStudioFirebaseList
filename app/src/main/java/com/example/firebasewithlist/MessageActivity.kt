package com.example.firebasewithlist

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.firebasedatatesting.People
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_message.*

class MessageActivity : AppCompatActivity() {

    private lateinit var userArrayList: ArrayList<People>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.RECEIVE_SMS,android.Manifest.permission.SEND_SMS),
            111)
        }else{
//            receiveMsg()
        }

        val database = FirebaseDatabase.getInstance("https://fir-datatesting-85dcc-default-rtdb.firebaseio.com/").reference
        userArrayList = ArrayList()
        val getData = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                userArrayList.clear()
                for (i in p0.children){
                    val people = People(
                        i.child("name").getValue().toString(),
                        i.child("replied").getValue().toString(),
                        i.child("phoneNumber").getValue().toString().toLong(),
                        i.child("url").getValue().toString()
                    )
                    userArrayList.add(people)
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        }
        database.addValueEventListener(getData)
        database.addListenerForSingleValueEvent(getData)

        btnSubmitSMS.setOnClickListener {
            for (i in userArrayList) {
                val sms = SmsManager.getDefault()
                sms.sendTextMessage(i.phoneNumber.toString(),"ME",etMessage.text.toString().replace("_name",i.name),null,null)
                i.replied = "no replied"
                database.child(i.phoneNumber.toString()).setValue(i)
            }
            finish()
        }
    }
}