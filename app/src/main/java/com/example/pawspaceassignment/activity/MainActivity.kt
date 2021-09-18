package com.example.pawspaceassignment.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.pawspaceassignment.models.Pet
import com.example.pawspaceassignment.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener{

    private lateinit var mDatabase: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDatabase = Firebase.firestore

        initSpinner()

        button_save.setOnClickListener() {
            saveDetails()
        }

        button_go_to_activity_pets.setOnClickListener {
            val intent= Intent(this, PetsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initSpinner(){
        ArrayAdapter.createFromResource(
            this, R.array.number_of_pets, android.R.layout.simple_spinner_item
        ).also { adapter->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_number_of_pets.adapter= adapter
        }

        spinner_number_of_pets.onItemSelectedListener= this

        ArrayAdapter.createFromResource(
            this, R.array.pets, android.R.layout.simple_spinner_item
        ).also { adapter->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_type_of_pet.adapter= adapter
        }

        spinner_type_of_pet.onItemSelectedListener= this
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//        val pets= resources.getStringArray(R.array.number_of_pets)
//        Toast.makeText(this, pets[p2], Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    private fun saveDetails(){

        if(edittext_weight.text.toString()==""){
            Toast.makeText(this, "Please enter a weight!", Toast.LENGTH_SHORT).show()
            return
        }

        val petType= spinner_type_of_pet.selectedItem.toString()
        val petWeight= Integer.parseInt(edittext_weight.text.toString())
        val ownerId= FirebaseAuth.getInstance().currentUser!!.uid
        val id= UUID.randomUUID().toString()

        val pet= Pet(id,ownerId, petType, petWeight, Timestamp.now())

        mDatabase.collection("pets")
            .document(id)
            .set(pet)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Pet added successfully!", Toast.LENGTH_SHORT)
                    .show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error occurred. Please try again!",
                    Toast.LENGTH_SHORT)
                    .show()
                Log.i("SAVE", e.message.toString())
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        return
    }
}