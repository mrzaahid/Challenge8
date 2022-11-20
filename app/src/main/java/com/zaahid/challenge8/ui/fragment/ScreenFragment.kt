package com.zaahid.challenge8.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.zaahid.challenge8.R

class ScreenFragment : Fragment() {
    private lateinit var auth : FirebaseAuth
    private lateinit var firebaseanalitic : FirebaseAnalytics

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        firebaseanalitic = Firebase.analytics
        cekStatus(auth.currentUser)
    }

    override fun onResume() {
        super.onResume()
        cekStatus(auth.currentUser)
    }
    private fun cekStatus(user:FirebaseUser?){
        if(user!=null){
                findNavController().navigate(R.id.action_screenFragment_to_homeActivity)
                Toast.makeText(requireContext(), getString(R.string.welcome_back)+" "+ user.email, Toast.LENGTH_SHORT).show()
        }else{
            findNavController().navigate(R.id.action_screenFragment_to_loginFragment)
        }
    }
}