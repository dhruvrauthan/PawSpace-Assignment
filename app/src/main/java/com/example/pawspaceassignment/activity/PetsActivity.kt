package com.example.pawspaceassignment.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pawspaceassignment.models.Pet
import com.example.pawspaceassignment.PetsAdapter
import com.example.pawspaceassignment.R
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_pets.*

class PetsActivity : AppCompatActivity() {

    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var mPetsAdapter: PetsAdapter
    private lateinit var mPets: MutableLiveData<ArrayList<Pet>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pets)

        mDatabase= Firebase.firestore
        mPets= MutableLiveData()

        getData()
    }

    private fun initRecyclerView(){
        val layoutManager= LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)
        recyclerview_pets.layoutManager= layoutManager

        mPetsAdapter= PetsAdapter(mPets.value!!)
        recyclerview_pets.adapter= mPetsAdapter

        progress_bar_pets_recyclerview.visibility= View.GONE

        mPets.observe(this, object: Observer<ArrayList<Pet>>{
            override fun onChanged(t: ArrayList<Pet>?) {
                mPetsAdapter= PetsAdapter(mPets.value!!)
                recyclerview_pets.adapter= mPetsAdapter
            }
        })
    }

    private fun getData() {
        mDatabase.collection("pets")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if(error!=null){
                    Log.i("GETPETS", error.message.toString())
                    return@addSnapshotListener
                }

                val petsArray= arrayListOf<Pet>()

                for(doc in value!!){
                    petsArray.add(doc.toObject(Pet::class.java))
                }

                mPets.value= petsArray

                initRecyclerView()
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent= Intent(this, MainActivity::class.java)
        startActivity(intent)

        finish()
    }
}