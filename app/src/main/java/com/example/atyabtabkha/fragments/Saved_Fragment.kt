package com.example.atyabtabkha.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.atyabtabkha.Adapter.PostAdapter
import com.example.atyabtabkha.Adapter.SavedAdapter
import com.example.atyabtabkha.Model.Post
import com.example.atyabtabkha.Model.Saved
import com.example.atyabtabkha.Model.User
import com.example.atyabtabkha.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class Saved_Fragment : Fragment() {

    private  var savedAdapter: SavedAdapter? = null
    private var savedList: MutableList<Saved>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_saved_, container, false)

        var recyclerview: RecyclerView? = null
        recyclerview = view. findViewById((R.id.recyclerview_saved))
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerview.layoutManager = linearLayoutManager


        savedList = ArrayList()
        savedAdapter = context?.let { SavedAdapter(it, savedList as ArrayList<Saved> ) }

        recyclerview.adapter = savedAdapter

        retreivePosts()

        return view
    }

    private fun retreivePosts() {

        val savedRef = FirebaseDatabase.getInstance().reference.child("Saved").child(FirebaseAuth.getInstance().currentUser!!.uid)

        savedRef.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    savedList?.clear()

                    for(snapshot in p0.children) {
                        val user = snapshot.getValue(Saved::class.java)
                        if (user != null) {
                            savedList?.add(user)
                        }


                    }
                        savedAdapter!!.notifyDataSetChanged()

                }
                }


            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

}