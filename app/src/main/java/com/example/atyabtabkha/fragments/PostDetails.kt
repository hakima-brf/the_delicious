package com.example.atyabtabkha.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.atyabtabkha.Model.Post
import com.example.atyabtabkha.Model.User
import com.example.atyabtabkha.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_post_details.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class PostDetails : Fragment() {
    private lateinit var postid: String
    private lateinit var publisher: String

    private lateinit var firebaseUser: FirebaseUser



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post_details, container, false)


        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        if(pref != null)
        {
            this.postid = pref.getString("postid", "none").toString()
            this.publisher = pref.getString("publisher", "none").toString()

        }

        postInfo()
        publisherInfo()


        return view
    }


    private  fun postInfo()
    {
        val postRef = FirebaseDatabase.getInstance().reference.child("Posts").child(postid)

        postRef.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot) {

                if(p0.exists())
                {

                    val post = p0.getValue<Post>(Post::class.java)
                    Picasso.get().load(post!!.getImage()).placeholder(R.drawable.logo).into(view?.post_image_home)
                    view?.title?.text = post!!.getTitle()
                    view?.description?.text = post!!.getDescription()
                    view?.country?.text = post!!.getCountry()
                    view?.methode?.text = post!!.getMethode()
                    view?.ingredients?.text = post!!.getIngredients()
                    view?.time_needed?.text = post!!.getTime()
                    view?.nbr_people?.text = post!!.getNbrPeople()


                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private  fun publisherInfo()
    {
        val userRef = FirebaseDatabase.getInstance().reference.child("users").child(publisher)

        userRef.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot) {

                if(p0.exists())
                {

                    val user = p0.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(view?.user_profile_image_post)
                    view?.user_name_post?.text = user!!.getUserName()
                    //view?.publisher?.text = user!!.getFullName()

                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

}