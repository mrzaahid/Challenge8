package com.zaahid.challenge8.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.zaahid.challenge8.ui.MainActivity
import com.zaahid.challenge8.ui.homefragment.PopularFragment
import com.zaahid.challenge8.ui.homefragment.SearchFragment
import com.zaahid.challenge8.R
import com.zaahid.challenge8.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val fragmentManager = supportFragmentManager
    private lateinit var analityc : FirebaseAnalytics
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        analityc = Firebase.analytics
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView2.setOnClickListener {
            val intent = Intent(baseContext, ProfileActivity::class.java)
            startActivity(intent)
        }
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.popular_movie-> {
                    fragmentManager.commit {
                        replace<PopularFragment>(R.id.fragment_container)
                    }
                    true
                }
                R.id.search_movie->{

                    fragmentManager.commit {
                        replace<SearchFragment>(R.id.fragment_container)
                    }
                    true
                }
                else->false
            }
        }
        cekstatus()
    }
    private fun cekstatus(){
        if (auth.currentUser == null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}