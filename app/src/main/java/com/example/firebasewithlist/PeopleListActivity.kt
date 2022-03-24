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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.firebasedatatesting.People
import com.example.firebasewithlist.databinding.ActivityPeopleListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_people_list.*

class PeopleListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPeopleListBinding
    private lateinit var userArrayList: ArrayList<People>
    private val database = FirebaseDatabase.getInstance("https://fir-datatesting-85dcc-default-rtdb.firebaseio.com/").reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeopleListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.RECEIVE_SMS,android.Manifest.permission.SEND_SMS),
                111)
        }else{
            receiveMsg()
        }

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

                binding.lvPeopleList.isClickable = true
                binding.lvPeopleList.adapter = UserListAdapter(this@PeopleListActivity, userArrayList)
                binding.lvPeopleList.setOnItemClickListener { _, _, position, _->
                    val name = userArrayList[position].name
                    val replied = userArrayList[position].replied.toString()
                    val phoneNumber = userArrayList[position].phoneNumber.toString()
                    val url = userArrayList[position].url.toString()

                    val i = Intent(this@PeopleListActivity, PeopleActivity::class.java)
                    i.putExtra("name", name)
                    i.putExtra("replied",replied)
                    i.putExtra("phoneNumber",phoneNumber)
                    i.putExtra("url",url)

                    startActivity(i)
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        }
        database.addValueEventListener(getData)
        database.addListenerForSingleValueEvent(getData)

        buttonSentMessage.setOnClickListener {
            val intent = Intent(this, MessageActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            receiveMsg()
        }
    }

    private fun receiveMsg() {
        val br = object: BroadcastReceiver(){
            override fun onReceive(p0: Context?, p1: Intent?) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                    for(sms in Telephony.Sms.Intents.getMessagesFromIntent(p1)){
                        if( userArrayList.any { it.phoneNumber.toString() == sms.originatingAddress } ){
                            val update = userArrayList.find { it.phoneNumber.toString() == sms.originatingAddress }
                            update!!.replied = sms.displayMessageBody.toString()
                            database.child(sms.originatingAddress.toString()).setValue(update!!)
                        }
                    }
                }
            }
        }
        registerReceiver(br, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
    }
}