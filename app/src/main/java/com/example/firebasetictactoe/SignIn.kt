package com.example.firebasetictactoe

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class SignIn: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.title = "Sign In"

        login_button_login.setOnClickListener {
            signInOnClick()
        }

        back_to_register_textview.setOnClickListener{
            finish()
        }
    }

    private fun signInOnClick(){
        val email = email_edittext_login.text.toString()
        val password = password_edittext_login.text.toString()
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please fill out email/pw.", Toast.LENGTH_SHORT).show()
            return
    }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
    .addOnCompleteListener(this) { task ->
        if (task.isSuccessful) {
            // Sign in success, update UI with the signed-in user's information
            Log.d("singIn", "signInWithEmail:success")
            val intent = Intent(this, PlayersList::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        } else {
            return@addOnCompleteListener
            // If sign in fails, display a message to the user.
            Log.w("singIn", "signInWithEmail:failure", task.exception)
            Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
        }
    }.addOnFailureListener{
        Toast.makeText(this, "Failed to create user: ${it.message}",
            Toast.LENGTH_SHORT).show()
    }
}

}