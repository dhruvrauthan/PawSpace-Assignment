package com.example.pawspaceassignment.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.pawspaceassignment.R
import com.example.pawspaceassignment.models.Pet
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_edit_pet.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class EditPetActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var mID: String
    private lateinit var mPet: Pet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_pet)

        mID= intent.getStringExtra("id").toString()
        mDatabase= Firebase.firestore
        mPet= Pet()

        getData()
    }

    private fun getData(){
        mDatabase.collection("pets")
            .document(mID)
            .get()
            .addOnSuccessListener {
                mPet= it.toObject(Pet::class.java)!!

                initView()
            }
            .addOnFailureListener {error->
                Log.i("EDITPET", error.message.toString())
                return@addOnFailureListener
            }
    }

    private fun initView(){
        initSpinner()

        edittext_edit_pet_weight.setText(mPet.weight.toString())
        textview_edit_pet_ownerid.text= mPet.ownerID.toString()
        progress_bar_edit_pets.visibility= View.GONE

        button_edit_pets.setOnClickListener {
            val petType= spinner_edit_pet_type.selectedItem.toString()
            val petWeight= Integer.parseInt(edittext_edit_pet_weight.text.toString())
            val ownerId= FirebaseAuth.getInstance().currentUser!!.uid

            val pet= Pet(mPet.id, ownerId, petType, petWeight, mPet.timestamp)

            mDatabase.collection("pets")
                .document(mPet.id!!)
                .set(pet)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(this, "Pet updated successfully!", Toast.LENGTH_SHORT)
                        .show()

                    val intent= Intent(this, PetsActivity::class.java)
                    startActivity(intent)

                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error occurred. Please try again!",
                        Toast.LENGTH_SHORT)
                        .show()
                    Log.i("EDITPET", e.message.toString())
                }
        }

        button_delete_pets.setOnClickListener {
            mDatabase.collection("pets")
                .document(mID)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Pet deleted successfully!",
                        Toast.LENGTH_SHORT)
                        .show()

                    val intent= Intent(this, PetsActivity::class.java)
                    startActivity(intent)

                    finish()
                }
                .addOnFailureListener {e->
                    Toast.makeText(this, "Error occurred. Please try again!",
                        Toast.LENGTH_SHORT)
                        .show()
                    Log.i("EDITPET", e.message.toString())
                }
        }
    }

    private fun initSpinner(){
        ArrayAdapter.createFromResource(
            this, R.array.pets, android.R.layout.simple_spinner_item
        ).also { adapter->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_edit_pet_type.adapter= adapter

            val compareValue= mPet.type.toString()
            val spinnerPosition= adapter.getPosition(compareValue)
            Log.i("EDITPET", spinnerPosition.toString())
            spinner_edit_pet_type.setSelection(spinnerPosition)
        }

        spinner_edit_pet_type.onItemSelectedListener= this
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}