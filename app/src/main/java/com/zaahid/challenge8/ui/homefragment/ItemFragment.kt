package com.zaahid.challenge8.ui.homefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zaahid.challenge8.R
import com.zaahid.challenge8.databinding.FragmentItemBinding
import com.zaahid.challenge8.model.Hasil


class ItemFragment(private val item : Hasil) : Fragment() {

    private var _binding: FragmentItemBinding?=null
    private val binding get() =_binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding  = FragmentItemBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val item = item
        val a :String = "https://image.tmdb.org/t/p/w500"+item.backdropPath.toString()
        val b = a.toUri()
        Glide.with(binding.imageView2)
            .load(b)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_the_movie_db_square)
                    .error(R.drawable.ic_launcher_background))
            .into(binding.imageView2)
        if (item.originalTitle.isNullOrEmpty()){
            binding.originTitle.text = getString(R.string.empty)
        }else binding.originTitle.text = item.originalTitle
        if (item.title.isNullOrEmpty()){
            binding.textTitle2.text = getString(R.string.empty)
        }else binding.textTitle2.text = item.title
        if (item.releaseDate.isNullOrEmpty()){
            binding.releaseDate.text =getString(R.string.empty)
        }else binding.releaseDate.text = item.releaseDate
        if (item.voteAverage != null){
            binding.score.text = item.voteAverage.toString()
        }else binding.score.text = item.voteAverage.toString()
        if (item.popularity != null){
            binding.popularity.text = getString(R.string.empty)
        }else binding.popularity.text = item.popularity.toString()
        if (item.overview.isNullOrEmpty()){
            binding.popularity.text = getString(R.string.empty)
        }else binding.overview.text = item.overview
    }
}