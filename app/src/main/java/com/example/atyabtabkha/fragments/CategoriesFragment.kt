package com.example.atyabtabkha.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.atyabtabkha.R
import kotlinx.android.synthetic.main.fragment_categories.*


class CategoriesFragment : Fragment() {




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_categories, container, false)


        val btnpop= view.findViewById<androidx.cardview.widget.CardView>(R.id.popular)
        @Suppress("DEPRECATION")

        btnpop.setOnClickListener {


            requireFragmentManager().beginTransaction()
                .replace(R.id.fl_wrapper1, RecipesFragment())
                .commit()

        }

        val btnbreakfast= view.findViewById<androidx.cardview.widget.CardView>(R.id.breakfast)
        @Suppress("DEPRECATION")

        btnbreakfast.setOnClickListener {


            requireFragmentManager().beginTransaction()
                .replace(R.id.fl_wrapper1, BreakfastFragment())
                .commit()

        }
        val btnlunch= view.findViewById<androidx.cardview.widget.CardView>(R.id.lunch)
        @Suppress("DEPRECATION")

        btnlunch.setOnClickListener {


            requireFragmentManager().beginTransaction()
                .replace(R.id.fl_wrapper1, LunchFragment())
                .commit()

        }
        val btndinner= view.findViewById<androidx.cardview.widget.CardView>(R.id.dinner)
        @Suppress("DEPRECATION")

        btndinner.setOnClickListener {


            requireFragmentManager().beginTransaction()
                .replace(R.id.fl_wrapper1, DinnerFragment())
                .commit()

        }
        val btndessert= view.findViewById<androidx.cardview.widget.CardView>(R.id.dessert)
        @Suppress("DEPRECATION")

        btndessert.setOnClickListener {


            requireFragmentManager().beginTransaction()
                .replace(R.id.fl_wrapper1, DessertFragment())
                .commit()

        }

        val btncakes= view.findViewById<androidx.cardview.widget.CardView>(R.id.cakes)
        @Suppress("DEPRECATION")

        btncakes.setOnClickListener {


            requireFragmentManager().beginTransaction()
                .replace(R.id.fl_wrapper1, CakesFragment())
                .commit()

        }
        return view
    }

}