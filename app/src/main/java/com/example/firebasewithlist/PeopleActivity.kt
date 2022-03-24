package com.example.firebasewithlist

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasewithlist.databinding.ActivityPeopleBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_people.*
import java.io.ByteArrayOutputStream


class PeopleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPeopleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeopleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database =
            FirebaseDatabase.getInstance("https://fir-datatesting-85dcc-default-rtdb.firebaseio.com/").reference

        val name = intent.getStringExtra("name")
        val replied = intent.getStringExtra("replied")
        val phoneNumber = intent.getStringExtra("phoneNumber")
        val url = intent.getStringExtra("url")

        binding.tvFirstname.text = name
        binding.tvLastname.text = phoneNumber
        binding.tvage.text = replied

        Picasso.get().load(url).into(img_qr);

        btnDelete.setOnClickListener {
            database.child(phoneNumber.toString()).removeValue()
            finish()
        }
    }




}