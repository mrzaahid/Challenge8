package com.zaahid.challenge8.ui.homefragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zaahid.challenge8.R
import com.zaahid.challenge8.databinding.ItemListBinding
import com.zaahid.challenge8.model.Hasil


class ItemAdapter(private val itemClick : (Hasil)->Unit) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    private val diffCallback = object : DiffUtil.ItemCallback<Hasil>(){
        override fun areItemsTheSame(oldItem: Hasil, newItem: Hasil): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Hasil, newItem: Hasil): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
    private val differ = AsyncListDiffer(this,diffCallback)

    fun submitData (value: List<Hasil>)= differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType : Int): ItemViewHolder {
        val binding =
            ItemListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  ItemViewHolder(binding)
    }
    class ItemViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root)

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        with(holder){
            with(differ.currentList[position]){
                itemView.setOnClickListener { itemClick(this) }
                val a :String = "https://image.tmdb.org/t/p/w500"+this.posterPath.toString()
                val baseimgurl = a.toUri()
                binding.TitleMovie.text = this.title
                if (this.overview.isNullOrEmpty()){
                    binding.overviewMovie.text = "not yet available in this language"
                }else binding.overviewMovie.text = this.overview
                Glide.with(binding.imagePosterMovie)
                    .load(baseimgurl)
                    .apply(RequestOptions()
                        .placeholder(R.drawable.ic_the_movie_db_square)
                        .error(R.drawable.ic_launcher_background))
                    .into(binding.imagePosterMovie)
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

}