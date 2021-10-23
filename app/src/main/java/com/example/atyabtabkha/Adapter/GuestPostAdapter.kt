package com.example.atyabtabkha.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.atyabtabkha.Model.Post
import com.example.atyabtabkha.Model.User
import com.example.atyabtabkha.R
import com.example.atyabtabkha.fragments.GuestPostDetailsFragment
import com.example.atyabtabkha.fragments.PostDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class GuestPostAdapter (private val mContext: Context,
                        private val mPost: List<Post>) : RecyclerView.Adapter<GuestPostAdapter.ViewHolder>()



{



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.guest_post_layout, parent, false )
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return mPost.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val post = mPost[position]

        Picasso.get().load(post.getImage()).into(holder.postImage)
        holder.title.text = post.getTitle()
        holder.description.text = post.getDescription()



        holder.itemView.setOnClickListener(View.OnClickListener {
            val pref = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            pref.putString("publisher", post.getPublisher())

            pref.putString("postid", post.getPostId())
            pref.apply()

            (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fl_wrapper1, GuestPostDetailsFragment()).commit()
        })






    }




    inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView)
    {

        var postImage : ImageView

        var description : TextView
        //var time : TextView
        //var methode : TextView
        //var ingredients : TextView
        //var country : TextView
        var title : TextView
        init {

            postImage = itemView.findViewById(R.id.post_image_guest_post)



            description = itemView.findViewById(R.id.description_guest_post)
            // time = itemView.findViewById(R.id.time_needed)
            //methode = itemView.findViewById(R.id.methode)
            //ingredients = itemView.findViewById(R.id.ingredients)
            //country = itemView.findViewById(R.id.country)
            title = itemView.findViewById(R.id.title_guest_post)

        }
    }















}