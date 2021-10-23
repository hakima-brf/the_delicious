package com.example.atyabtabkha.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.example.atyabtabkha.EditProfileActivity
import com.example.atyabtabkha.Model.User
import com.example.atyabtabkha.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_profile.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.profile_img


class ProfileFragment : Fragment() {

    private lateinit var profileId: String
    private lateinit var firebaseUser: FirebaseUser





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)



        //go to saved imgs

        @Suppress("DEPRECATION")
        val btsaved= view.findViewById<ImageButton>(R.id.imgs_saved_view_btn_pro)
        btsaved.setOnClickListener {



            requireFragmentManager().beginTransaction()
                .replace(R.id.fl_wrapper, Saved_Fragment())
                .commit()
        }


        //go to posts


        @Suppress("DEPRECATION")
      val btnposts= view.findViewById<ImageButton>(R.id.imgs_grid_view_btn)
        btnposts.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("data", profileId)
            val fragment = UserPostsFragment()
            fragment.arguments = bundle

            requireFragmentManager().beginTransaction()
                .replace(R.id.fl_wrapper, UserPostsFragment())
                .commit()

        }







        //go to edit profile activity
        view.edit_account.setOnClickListener {



            val getButtonText  =view.edit_account.text.toString()
            when
            {
                getButtonText == "edit profile" -> startActivity(Intent(context, EditProfileActivity::class.java ))

                getButtonText == "Follow" ->{
                    firebaseUser?.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Follow").child(it1.toString())
                            .child("Following").child(profileId).setValue(true)
                    }

                    firebaseUser?.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Follow").child(profileId)
                            .child("Followers").child(it1.toString()).setValue(true)
                    }
                }
                getButtonText == "Following" ->{
                    firebaseUser?.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Follow").child(it1.toString())
                            .child("Following").child(profileId).removeValue()
                    }

                    firebaseUser?.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Follow").child(profileId)
                            .child("Followers").child(it1.toString()).removeValue()
                    }
                }



            }



        }




        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        if(pref != null)
        {
            this.profileId = pref.getString("profileId", "none").toString()




        }
        if(profileId == firebaseUser.uid)
        {
            view.edit_account.text = "edit profile"


        }
        else if(profileId != firebaseUser.uid)
        {

            val bar = view.findViewById<LinearLayout>(R.id.bar_posts_saved)
            bar.setVisibility(View.GONE)



            checkFollowAndFollowingButtonStatus()

        }

        getFollowers()
        getFollowing()
        userInfo()
        getPostsNbr()

        return view
        }

    private fun checkFollowAndFollowingButtonStatus() {
        val followingRef = firebaseUser?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Follow").child(it1.toString())
                .child("Following")
        }
        if(followingRef != null)
        {
            followingRef.addValueEventListener(object  : ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.child(profileId).exists())
                    {

                        view?.edit_account?.text = "Following"
                    }
                    else
                    {
                        view?.edit_account?.text = "Follow"
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }


    private fun getFollowers()
    {
        val followersRef = FirebaseDatabase.getInstance().reference
                .child("Follow").child(profileId)
                .child("Followers")

        followersRef.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
               if(p0.exists())
               {
                   view?.total_followers?.text = p0.childrenCount.toString()
               }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    private fun getFollowing()
    {
        val followersRef = FirebaseDatabase.getInstance().reference
                .child("Follow").child(profileId)
                .child("Following")

        followersRef.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists())
                {
                    view?.total_following?.text = p0.childrenCount.toString()
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun getPostsNbr(){
        val postsNbrRef = FirebaseDatabase.getInstance().reference
            .child("UserPosts").child(profileId)

        postsNbrRef.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists())
                {
                    view?.total_posts?.text = p0.childrenCount.toString()
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    private  fun userInfo()
    {
        val userRef = FirebaseDatabase.getInstance().reference.child("users").child(profileId)

        userRef.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot) {

                if(p0.exists())
                {

                    val user = p0.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(view?.profile_img)
                    view?.profile_username?.text = user!!.getUserName()
                    view?.full_name_profile?.text = user!!.getFullName()
                    view?.bio_profile?.text = user!!.getBio()
                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    override fun onStop() {
        super.onStop()
        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUser.uid)
        pref?.apply()
    }

    override fun onPause() {
        super.onPause()
        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUser.uid)
        pref?.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUser.uid)
        pref?.apply()
    }



}





