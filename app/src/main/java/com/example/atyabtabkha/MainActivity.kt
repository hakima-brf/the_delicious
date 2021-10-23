
package com.example.atyabtabkha

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
private  const val TAG = "loginActivity"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            goPostActivity()
        }


        sign_in_btn_main_view.setOnClickListener {

            sign_in_btn_main_view.isEnabled = false
            val email1 = email.text.toString()
            val password1 = password.text.toString()
            if (email1.isBlank() || password1.isBlank()) {
                Toast.makeText(this, "Email/password cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //firebase auth
            auth.signInWithEmailAndPassword(email1, password1).addOnCompleteListener { task ->
                sign_in_btn_main_view.isEnabled = true
                if (task.isSuccessful) {
                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
                    goPostActivity()
                } else {
                    Log.i(TAG, "signInWithEmail failed", task.exception)
                    Toast.makeText(this, "Authentification failed", Toast.LENGTH_SHORT).show()
                }

            }

        }

        sighup_btn_main_view.setOnClickListener {

            sighup_btn_main_view.isEnabled = false
            goSignUpActivity()

        }

        guest_btn.setOnClickListener {

            sighup_btn_main_view.isEnabled = false

            val intent = Intent(this, GuestActivity::class.java)
            startActivity(intent)
        }

    }
    private fun goPostActivity(){
        Log.i(TAG, "goPostActivity")
        val intent = Intent(this, PostActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun goSignUpActivity(){
        Log.i(TAG, "goPostActivity")
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)

    }




}