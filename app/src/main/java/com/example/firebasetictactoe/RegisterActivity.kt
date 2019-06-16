package com.example.firebasetictactoe

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.letsbuildthatapp.kotlinmessenger.models.User
import kotlinx.android.synthetic.main.activity_main.*


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Tic Tac Toe Game"

        findViewById<TextView>(R.id.signIn).setOnClickListener { signInOnClick() }
        findViewById<Button>(R.id.signUp).setOnClickListener { signUpOnClick() }

    }
    private fun signUpOnClick(){
        val email = email.text.toString()
        val password = password.text.toString()
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please enter text in email/pw", Toast.LENGTH_SHORT).show()
            return
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("2019", "createUserWithEmail:success")
                    saveUserToFirebaseDatabase()
                    Toast.makeText(this, "Registration successful, you can login now!",
                        Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, SignIn::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("2019", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(this, "Couldn't create account! Try again!",
                        Toast.LENGTH_SHORT).show()
                    return@addOnCompleteListener
                }
            }
            .addOnFailureListener{
                Toast.makeText(this, "Failed to create user: ${it.message}",
                    Toast.LENGTH_SHORT).show()
            }
    }

    private fun signInOnClick() {
        val intent = Intent(this@RegisterActivity, SignIn::class.java)
        startActivityForResult(intent, 1)
    }
    private fun saveUserToFirebaseDatabase() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, name.text.toString())

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("database", "Finally we saved the user to Firebase Database")
            }
            .addOnFailureListener {
                Log.d("database", "Failed to set value to database: ${it.message}")
            }
    }
}
