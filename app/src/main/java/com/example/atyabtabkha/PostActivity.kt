package com.example.atyabtabkha

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.atyabtabkha.fragments.*
import kotlinx.android.synthetic.main.posts_activity.*

class PostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.posts_activity)


        val homeFragment = HomeFragment()
        val searchFragment = SearchFragment()

        val allPostsFragment = AllPostsFragment()
        val profileFragment = ProfileFragment()

        makeCurrentFragment(homeFragment)

        bottom_nav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> makeCurrentFragment(homeFragment)
                R.id.allposts -> makeCurrentFragment(allPostsFragment)
                R.id.search -> makeCurrentFragment(searchFragment)
                R.id.profile -> makeCurrentFragment(profileFragment)
                R.id.add_post -> startActivity(Intent(this@PostActivity, AddNewPostActivity::class.java))
            }
            true
        }



    }






    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }




}