package com.zaahid.challenge8.ui.homefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaahid.challenge8.ui.UserViewModel
import com.zaahid.challenge8.wrapper.Resource
import com.zaahid.challenge8.R
import com.zaahid.challenge8.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding?=null
    private val binding get() =_binding!!
    private val userViewModel : UserViewModel by viewModels()
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
        _binding  = FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        initList(view)
        val searchview = binding.searchMovie
        searchview.queryHint = getString(R.string.search)
        searchview.isIconified = false
//        searchview.isFocusable = true
        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    userViewModel.searchMovie(it,userViewModel.getLang().toString())
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
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
                    binding.pbPopular.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.pbPopular.visibility = View.GONE
                    it.payload?.let { it1 -> adapter.submitData(it1) }
                }
            }
        }
    }
    private fun initList(view: View) {
        binding.rvMovie.apply {
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL,false)
            adapter = this@SearchFragment.adapter
        }
    }
}