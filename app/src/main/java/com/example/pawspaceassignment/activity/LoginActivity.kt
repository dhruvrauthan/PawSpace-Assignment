package com.example.pawspaceassignment.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.pawspaceassignment.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()

        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if(user==null){
            mAuth.signInAnonymously()
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {
                        val currentUser = mAuth.currentUser
                        updateUI(currentUser)
                    } else {
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        Log.i("LOGIN", task.exception.toString())
                        updateUI(null)
                    }

                }
        } else{
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}