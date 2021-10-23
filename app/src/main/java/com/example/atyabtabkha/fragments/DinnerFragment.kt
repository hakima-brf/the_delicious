package com.example.atyabtabkha.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.atyabtabkha.Adapter.GuestPostAdapter
import com.example.atyabtabkha.Model.Post
import com.example.atyabtabkha.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class DinnerFragment : Fragment() {

    private  var dinnerPostAdapter: GuestPostAdapter? = null
    private var dinnerPostList: MutableList<Post>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dinner, container, false)

        var recyclerview: RecyclerView? = null
        recyclerview = view. findViewById((R.id.recyclerview_jus))
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerview.layoutManager = linearLayoutManager

        dinnerPostList = ArrayList()
        dinnerPostAdapter = context?.let { GuestPostAdapter(it, dinnerPostList as ArrayList<Post> ) }

        recyclerview.adapter = dinnerPostAdapter
        retreiveJusPosts()

        return view
    }

    private fun retreiveJusPosts() {
        val userPostsRef = FirebaseDatabase.getInstance().reference.child("gests").child("dinner")

        userPostsRef.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    dinnerPostList?.clear()

                    for(snapshot in p0.children) {
                        val user = snapshot.getValue(Post::class.java)
                        if (user != null) {
                            dinnerPostList?.add(user)
                        }


                    }
                    dinnerPostAdapter!!.notifyDataSetChanged()

                }
            }


            override fun onCancelled(error: DatabaseError) {

            }
        })
    }




}