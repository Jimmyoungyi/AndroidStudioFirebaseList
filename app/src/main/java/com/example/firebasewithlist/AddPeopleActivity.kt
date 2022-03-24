package com.example.firebasewithlist

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import com.example.firebasedatatesting.People
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_people.*
import java.io.ByteArrayOutputStream

class AddPeopleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_people)

        val database =
            FirebaseDatabase.getInstance("https://fir-datatesting-85dcc-default-rtdb.firebaseio.com/").reference

        button.setOnClickListener {

            var fnT = fn.text.toString()
            var phoneNumberT = phoneNumber.text.toString()

            fn.text.clear()
            phoneNumber.text.clear()

            val text = "{\"name\": \"$fnT\", \"phone number\": \"$phoneNumberT\"}"
            val qrgEncoder = QRGEncoder(text, null, QRGContents.Type.TEXT, 100)

            uploadImage(qrgEncoder.bitmap, phoneNumberT, database, fnT, phoneNumberT)
        }

    }

    private fun uploadImage(
        qrgEncoder: Bitmap,
        name: String,
        database: DatabaseReference,
        fnT: String,
        phoneNumberT: String
    ) {
        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://fir-datatesting-85dcc.appspot.com/")

        val mountainsRef = storageRef.child("$name.jpg")

        val bitmap = qrgEncoder
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data: ByteArray = baos.toByteArray()

        val uploadTask = mountainsRef.putBytes(data)
        Log.d("img", "prepare upload")
        uploadTask
            .addOnFailureListener {
                Log.d("img", "upload image error" + it.message)
            }
            .addOnSuccessListener { taskSnapshot ->
                Log.d("img", "upload image success")
                mountainsRef.downloadUrl.addOnCompleteListener() { taskSnapshot ->
                    var url = taskSnapshot.result.toString()
                    Log.d("img", "upload image complete")
                    database.child(phoneNumberT)
                        .setValue(People(fnT, "no replied", phoneNumberT.toLong(), url))

                    finish()
                }
            }
    }
}