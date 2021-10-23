package com.example.atyabtabkha.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.atyabtabkha.Adapter.PostAdapter
import com.example.atyabtabkha.Adapter.UserAdapter
import com.example.atyabtabkha.Model.Post
import com.example.atyabtabkha.Model.Saved
import com.example.atyabtabkha.Model.User
import com.example.atyabtabkha.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class UserPostsFragment : Fragment() {
    private  var postAdapter: PostAdapter? = null
    private var UserPostsList: MutableList<Post>? = null
    private lateinit var proid: String






    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_user_posts, container, false)






        //val args = this.arguments
        //val pro = args?.get("data")
        //proid=pro.toString()
        //Log.i("proid", proid)





        var recyclerview: RecyclerView? = null
        recyclerview = view. findViewById((R.id.recyclerview_user_posts))
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerview.layoutManager = linearLayoutManager


        UserPostsList = ArrayList()
        postAdapter = context?.let { PostAdapter(it, UserPostsList as ArrayList<Post> ) }



        recyclerview.adapter = postAdapter




        retreiveUserPosts()

        return view
    }

    private fun retreiveUserPosts() {
        val userPostsRef = FirebaseDatabase.getInstance().reference.child("UserPosts").child(FirebaseAuth.getInstance().currentUser!!.uid)

        userPostsRef.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    UserPostsList?.clear()

                    for(snapshot in p0.children) {
                        val user = snapshot.getValue(Post::class.java)
                        if (user != null) {
                            UserPostsList?.add(user)
                        }


                    }
                    postAdapter!!.notifyDataSetChanged()

                }
            }


            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


}