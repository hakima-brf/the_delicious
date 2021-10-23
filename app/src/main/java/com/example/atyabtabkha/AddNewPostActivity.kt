package com.example.atyabtabkha

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_add_new_post.*
import kotlinx.android.synthetic.main.activity_edit_profile.*

class AddNewPostActivity : AppCompatActivity() {

    private var myUrl= ""
    private var imageUri: Uri? = null
    private var storagePostPicRef: StorageReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_post)

        close_new_post_btn.setOnClickListener {
            startActivity(Intent(this, PostActivity::class.java))
        }

        storagePostPicRef = FirebaseStorage.getInstance().reference.child("Posts Pictures")
        save_new_post_btn.setOnClickListener { uploadImage() }


        CropImage.activity()
            .setAspectRatio(2, 1)
            .start(this@AddNewPostActivity)
    }



    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data!=null)
        {
            val result = CropImage.getActivityResult(data)
            imageUri = result.uri
            image_post.setImageURI(imageUri)
        }
    }

    @Suppress("DEPRECATION")
    private fun uploadImage() {
        when{
            imageUri == null -> Toast.makeText(this, "Please select image first.", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(description_post.text.toString()) -> Toast.makeText(this, "Please add a description.", Toast.LENGTH_LONG).show()
            title_post.text.toString() == "" -> Toast.makeText(this, "Please add a title.", Toast.LENGTH_LONG).show()
            country_plate_post.text.toString() == "" -> Toast.makeText(this, "Please add a country.", Toast.LENGTH_LONG).show()
            methode_cooking_post.text.toString() == "" -> Toast.makeText(this, "Please add the the methode to cook this plate.", Toast.LENGTH_LONG).show()
            ingredients_cooking_post.text.toString() == "" -> Toast.makeText(this, "Please add the ingredients needed to cook this plate.", Toast.LENGTH_LONG).show()
            time_cooking_post.text.toString() == "" -> Toast.makeText(this, "Please add the time needed to cook this plate.", Toast.LENGTH_LONG).show()
            nbr_people_post.text.toString() == "" -> Toast.makeText(this, "Please add the number of people who can eat this plate.", Toast.LENGTH_LONG).show()
            else ->{

                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Adding new post")
                progressDialog.setMessage("Please wait, we are adding  your  new post to your profile...")
                progressDialog.show()

                val fileRef = storagePostPicRef!!.child(System.currentTimeMillis().toString() + ".jpg")

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
                })
                    .addOnCompleteListener (OnCompleteListener<Uri> { task ->
                    if (task.isSuccessful)
                    {
                        val downloadUrl = task.result
                        myUrl = downloadUrl.toString()

                        val ref = FirebaseDatabase.getInstance().reference.child("Posts")
                        val ref1 = FirebaseDatabase.getInstance().reference.child("UserPosts").child(FirebaseAuth.getInstance().currentUser!!.uid)


                        val postId = ref.push().key

                        val postMap = HashMap<String, Any>()
                        postMap["postid"] = postId!!
                        postMap["description"] = description_post.text.toString().toLowerCase()
                        postMap["title"] = title_post.text.toString().toLowerCase()
                        postMap["time"] = time_cooking_post.text.toString().toLowerCase()
                        postMap["country"] = country_plate_post.text.toString().toLowerCase()
                        postMap["ingredients"] = ingredients_cooking_post.text.toString().toLowerCase()
                        postMap["methode"] = methode_cooking_post.text.toString().toLowerCase()
                        postMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid
                        postMap["image"] = myUrl
                        postMap["nbrPeople"] = nbr_people_post.text.toString().toLowerCase()


                        ref.child(postId).updateChildren(postMap)
                        ref1.child(postId).updateChildren(postMap)


                        Toast.makeText(this, "post uoloaded successfully.", Toast.LENGTH_LONG).show()

                        val intent = Intent(this@AddNewPostActivity, PostActivity::class.java)
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