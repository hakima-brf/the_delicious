package com.example.atyabtabkha

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.atyabtabkha.Model.User
import com.example.atyabtabkha.fragments.ProfileFragment
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class EditProfileActivity : AppCompatActivity() {


    private lateinit var firebaseUser: FirebaseUser
    private var checker = ""
    private var myUrl= ""
    private var imageUri: Uri? = null
    private var storageProfilePicRef: StorageReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        storageProfilePicRef = FirebaseStorage.getInstance().reference.child("Profile Pictures")


        logout_user_account.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@EditProfileActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
        close_edit_profile_btn.setOnClickListener {
            val intent = Intent(this@EditProfileActivity, ProfileFragment::class.java)
            startActivity(intent)

        }

        change_img_profile.setOnClickListener {

            checker = "clicked"
            CropImage.activity()
                .setAspectRatio(1, 1)
                .start(this@EditProfileActivity)
        }

        save_info_profile_btn.setOnClickListener {

            if(checker == "clicked")
            {
                uploadImageAndUpdateInfo()

            }
            else{
                updateUserInfoOnly()
            }
        }

        userInfo()
    }




    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data!=null)
        {
            val result = CropImage.getActivityResult(data)
            imageUri = result.uri
            profile_img.setImageURI(imageUri)
        }
    }

    private fun updateUserInfoOnly() {
        when {
            edit_profile_full_name.text.toString() == "" -> {
                Toast.makeText(this, "full name can't be empty", Toast.LENGTH_SHORT).show()
            }
            edit_profile_username.text.toString() == "" -> {
                Toast.makeText(this, "username can't be empty", Toast.LENGTH_SHORT).show()
            }
            edit_profile_bio.text.toString() == "" -> {
                Toast.makeText(this, "bio can't be empty", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val userRef = FirebaseDatabase.getInstance().reference.child("users")
                val userMap = HashMap<String, Any>()

                userMap["fullname"] = edit_profile_full_name.text.toString().toLowerCase()
                userMap["username"] = edit_profile_username.text.toString().toLowerCase()
                userMap["bio"] = edit_profile_bio.text.toString().toLowerCase()

                userRef.child(firebaseUser.uid).updateChildren(userMap)
                Toast.makeText(this, "account info has been updated successfully.",Toast.LENGTH_LONG).show()
                val intent = Intent(this@EditProfileActivity, PostActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()

            }
        }




    }


    private  fun userInfo()
    {
        val userRef = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.uid)

        userRef.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot) {

                if(p0.exists())
                {

                    val user = p0.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(profile_img)
                    edit_profile_username.setText(user!!.getUserName())
                    edit_profile_full_name.setText(user!!.getFullName())
                    edit_profile_bio.setText(user!!.getBio())
                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    @Suppress("DEPRECATION")
    private fun uploadImageAndUpdateInfo() {


        when
        {
            imageUri == null -> Toast.makeText(this, "Please select image first.", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(edit_profile_full_name.text.toString()) -> Toast.makeText(this, "Please write full name first.", Toast.LENGTH_LONG).show()
            edit_profile_username.text.toString() == "" -> Toast.makeText(this, "Please write user name first.", Toast.LENGTH_LONG).show()
            edit_profile_bio.text.toString() == "" -> Toast.makeText(this, "Please write your bio first.", Toast.LENGTH_LONG).show()

            else -> {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Account Settings")
                progressDialog.setMessage("Please wait, we are updating your profile...")
                progressDialog.show()

                val fileRef = storageProfilePicRef!!.child(firebaseUser!!.uid + ".jpg")

                var uploadTask: StorageTask<*>
                uploadTask = fileRef.putFile(imageUri!!)

                uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{ task ->
                    if (!task.isSuccessful)
                    {
                        task.exception?.let {
                            throw it
                            progressDialog.dismiss()
                        }
                    }
                    return@Continuation fileRef.downloadUrl
                }).addOnCompleteListener (OnCompleteListener<Uri> {task ->
                    if (task.isSuccessful)
                    {
                        val downloadUrl = task.result
                        myUrl = downloadUrl.toString()

                        val ref = FirebaseDatabase.getInstance().reference.child("users")

                        val userMap = HashMap<String, Any>()
                        userMap["fullname"] = edit_profile_full_name.text.toString().toLowerCase()
                        userMap["username"] = edit_profile_username.text.toString().toLowerCase()
                        userMap["bio"] = edit_profile_bio.text.toString().toLowerCase()
                        userMap["image"] = myUrl

                        ref.child(firebaseUser.uid).updateChildren(userMap)

                        Toast.makeText(this, "Account Information has been updated successfully.", Toast.LENGTH_LONG).show()

                        val intent = Intent(this@EditProfileActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        progressDialog.dismiss()
                    }
                    else
                    {
                        progressDialog.dismiss()
                    }
                } )
            }
        }
    }

}





