package com.example.atyabtabkha

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.atyabtabkha.fragments.*
import kotlinx.android.synthetic.main.activity_guest.*
import kotlinx.android.synthetic.main.posts_activity.*
import kotlinx.android.synthetic.main.posts_activity.bottom_nav

class GuestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest)


        val homeFragment = HomeGuestFragment()
        val recipesFragment = RecipesFragment()
        val categoriesFragment = CategoriesFragment()

        makeCurrentFragment(homeFragment)

        bottom_nav_guest.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> makeCurrentFragment(homeFragment)
                R.id.categories -> makeCurrentFragment(categoriesFragment)
                R.id.recipes -> makeCurrentFragment(recipesFragment)
            }
            true
        }


    }


    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper1, fragment)
            commit()
        }

}