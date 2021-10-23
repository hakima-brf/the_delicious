package com.example.atyabtabkha.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.atyabtabkha.Model.Post
import com.example.atyabtabkha.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_post_details.view.*


class GuestPostDetailsFragment : Fragment() {

    private lateinit var postid: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_guest_post_details, container, false)

        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        if(pref != null)
        {
            this.postid = pref.getString("postid", "none").toString()


        }

        postInfo()

        return view
    }
    private  fun postInfo()
    {
        val categorie = postid.dropLast(1)

        val postRef = FirebaseDatabase.getInstance().reference.child("gests").child(categorie).child(postid)

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


}