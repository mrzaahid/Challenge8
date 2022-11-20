package com.zaahid.challenge8.ui.homefragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.zaahid.challenge8.ui.MainActivity
import com.zaahid.challenge8.ui.UserViewModel
import com.zaahid.challenge8.wrapper.Resource
import com.zaahid.challenge8.R
import com.zaahid.challenge8.databinding.FragmentPopularBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularFragment : Fragment() {
    private var _binding: FragmentPopularBinding?=null
    private val binding get() =_binding!!
    private val userViewModel : UserViewModel by viewModels()
    private lateinit var auth : FirebaseAuth

    private val adapter: ItemAdapter by lazy {
        ItemAdapter{
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, ItemFragment(it))
                ?.addToBackStack(null)
                ?.commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding  = FragmentPopularBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList(view)
        auth = Firebase.auth
        observeData()
            val a = getString(R.string.welcome)+" "+auth.currentUser?.email
            binding.textWelcomeUser.text = a
        userViewModel.getLang().observe(viewLifecycleOwner){
            userViewModel.popularMovie(it)
        }
        cekstatus()
    }

    override fun onResume() {
        super.onResume()
            val email =auth.currentUser?.email.toString()
            val welcome = getString(R.string.welcome)
            val x = "$welcome $email"
            binding.textWelcomeUser.text = x
    }
    private fun cekstatus(){
        if (auth.currentUser == null){
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeData() {
        userViewModel.listMovieResult.observe(viewLifecycleOwner){
            when(it){
                is Resource.Empty -> {}
                is Resource.Error -> {
                    Toast.makeText(context, "error", Toast.LENGTH_LONG).show()
                }
                is Resource.ErrorTrow -> {}
                is Resource.Loading -> {
                    binding.rvPopular.visibility = View.GONE
                    binding.pbPopular.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.rvPopular.visibility = View.VISIBLE
                    binding.pbPopular.visibility = View.GONE
                    it.payload?.let { it1 -> adapter.submitData(it1) }
                }
            }
        }
    }
    private fun initList(view: View) {
        binding.rvPopular.apply {
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL,false)
            adapter = this@PopularFragment.adapter
        }
    }

}