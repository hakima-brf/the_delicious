package com.example.atyabtabkha.Adapter

import android.app.ProgressDialog
import android.content.Context
import androidx.annotation.NonNull
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.atyabtabkha.Model.Post
import com.example.atyabtabkha.Model.User
import com.example.atyabtabkha.R
import com.example.atyabtabkha.fragments.HomeFragment
import com.example.atyabtabkha.fragments.PostDetails
import com.example.atyabtabkha.fragments.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_add_new_post.*
import kotlinx.android.synthetic.main.activity_edit_profile.*

class PostAdapter (private val mContext: Context,
                   private val mPost: List<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>()



{

    private var firebaseUser: FirebaseUser? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.post_layout, parent, false )
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return mPost.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        firebaseUser = FirebaseAuth.getInstance().currentUser
        val post = mPost[position]

        holder.title.text = post.getTitle()
        holder.description.text = post.getDescription()

        Picasso.get().load(post.getImage()).into(holder.postImage)

        publisherInfo(holder.profileImage, holder.userName, holder.publisher, post.getPublisher())

        holder.itemView.setOnClickListener(View.OnClickListener {
            val pref = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            pref.putString("publisher", post.getPublisher())

            pref.putString("postid", post.getPostId())
            pref.apply()

            (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fl_wrapper, PostDetails()).commit()
        })


        holder.likeButton.setOnClickListener(View.OnClickListener {
            with(holder) { likeButton.setImageResource(R.drawable.favorite)}
        })

        holder.saveButton.setOnClickListener(View.OnClickListener {
            val pref = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            pref.putString("publisher", post.getPublisher())

            pref.putString("postid", post.getPostId())

            pref.apply()
            var savedPublisherid = post.getPublisher()
            var savedPostId = post.getPostId()
            var savedDescription = post.getDescription()
            var savedTitle = post.getTitle()
            var savedTime = post.getTime()
            var savedCountry = post.getCountry()
            var savedIngredients = post.getIngredients()
            var savedMethode = post.getMethode()
            var savedImage = post.getImage()
            var nbrPeople = post.getNbrPeople()










            val savedRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Saved").child(FirebaseAuth.getInstance().currentUser!!.uid)
            val savedMap = HashMap<String, Any>()

            savedMap["postid"] = savedPostId
            savedMap["description"] = savedDescription
            savedMap["title"] = savedTitle
            savedMap["time"] = savedTime
            savedMap["country"] = savedCountry
            savedMap["ingredients"] = savedIngredients
            savedMap["methode"] = savedMethode
            savedMap["publisher"] = savedPublisherid.toString()
            savedMap["image"] = savedImage
            savedMap["nbrPeople"] = nbrPeople


            savedRef.child(savedPostId).setValue(savedMap).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    with(holder) { saveButton.setImageResource(R.drawable.bookmark_saved)}

                } else {


                    

                }
            }

        })

    }




    inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView)
   {
       var profileImage: CircleImageView
       var postImage : ImageView
       var likeButton : ImageView
       var saveButton : ImageView
       var userName : TextView

       var publisher : TextView
       var description : TextView
       //var time : TextView
       //var methode : TextView
       //var ingredients : TextView
       //var country : TextView
       var title : TextView
       init {
           profileImage = itemView.findViewById(R.id.user_profile_image_post)
           postImage = itemView.findViewById(R.id.post_image_home)
           likeButton = itemView.findViewById(R.id.post_image_like_btn)
           saveButton = itemView.findViewById(R.id.post_save_comment_btn)
           userName = itemView.findViewById(R.id.user_name_post)

           publisher = itemView.findViewById(R.id.publisher)
           description = itemView.findViewById(R.id.description)
          // time = itemView.findViewById(R.id.time_needed)
           //methode = itemView.findViewById(R.id.methode)
           //ingredients = itemView.findViewById(R.id.ingredients)
           //country = itemView.findViewById(R.id.country)
           title = itemView.findViewById(R.id.title_post)

       }
   }










    private fun publisherInfo(profileImage: CircleImageView, userName: TextView, publisher: TextView, publisherID: String) {

        val usersRef = FirebaseDatabase.getInstance().reference.child("users").child(publisherID)
        usersRef.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {

                if(p0.exists()){
                    val user = p0.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(profileImage)
                    //val post = p0.getValue<Post>(Post::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.logo).into(profileImage)


                    userName.text = user!!.getUserName()
                    publisher.text = user!!.getFullName()


                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }




}