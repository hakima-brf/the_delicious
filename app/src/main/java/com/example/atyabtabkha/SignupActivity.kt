package com.example.atyabtabkha

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        if (FirebaseAuth.getInstance().currentUser != null) {
            val intent = Intent(this, PostActivity::class.java)
            startActivity(intent)
        }

        sign_up_btn_sign_up_view.setOnClickListener {
            createAccount()
        }

        sigh_in_btn_sign_up_view.setOnClickListener {
            gosigninactivity()
        }





    }
    private fun gosigninactivity(){

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    @Suppress("DEPRECATION")
    private fun createAccount(){

        val fullName = fulname_signup.text.toString()
        val userName= username_signup.text.toString()
        val emailSignUp = email_signup.text.toString()
        val passwordSignUp =password_signup.text.toString()

        when{
            TextUtils.isEmpty(fullName) -> Toast.makeText(this, "full name is required.",Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(userName) -> Toast.makeText(this, "username is required.",Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(emailSignUp) -> Toast.makeText(this, "email is required.",Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(passwordSignUp) -> Toast.makeText(this, "password is required.",Toast.LENGTH_LONG).show()
        else -> {

            val progressDialog = ProgressDialog(this@SignupActivity)
            progressDialog.setTitle("SignUp")
            progressDialog.setMessage("please wait, this may take a while ...")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

            val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
            mAuth.createUserWithEmailAndPassword(emailSignUp,passwordSignUp).addOnCompleteListener { task ->

                    if(task.isSuccessful){
                        
                        saveUserInfo(fullName, userName, emailSignUp,progressDialog)
                        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()

                    }
                    else{

                        Toast.makeText(this, "Signing up failed", Toast.LENGTH_SHORT).show()
                        mAuth.signOut()
                        progressDialog.dismiss()
                    }

                }
        }
        }
        }
    @Suppress("DEPRECATION")
    private fun saveUserInfo(fullName: String, userName: String, emailSignUp: String, progressDialog: ProgressDialog) {
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("users")
        val userMap = HashMap<String, Any>()
        userMap["uid"] = currentUserID
        userMap["fullname"] = fullName.toLowerCase()
        userMap["username"] = userName.toLowerCase()
        userMap["email"] = emailSignUp
        userMap["bio"] = "hey i'm using atyab tabkha."
        userMap["image"]="https://firebasestorage.googleapis.com/v0/b/salty-9f4fd.appspot.com/o/default%20images%2Ft%C3%A9l%C3%A9chargement%20(1).png?alt=media&token=e131c62b-804c-499d-a22a-1bdc604c7554"

        usersRef.child(currentUserID).setValue(userMap).addOnCompleteListener { task ->
            if(task.isSuccessful){
                progressDialog.dismiss()
                Toast.makeText(this, "account has been created successfully.",Toast.LENGTH_LONG).show()



                FirebaseDatabase.getInstance().reference
                    .child("Follow").child(currentUserID)
                    .child("Following").child(currentUserID)
                    .setValue(true)


                val intent = Intent(this@SignupActivity, PostActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()


            }
            else{

                Toast.makeText(this, "creating account failed", Toast.LENGTH_SHORT).show()
                FirebaseAuth.getInstance().signOut()
                progressDialog.dismiss()

            }
        }

    }

}
