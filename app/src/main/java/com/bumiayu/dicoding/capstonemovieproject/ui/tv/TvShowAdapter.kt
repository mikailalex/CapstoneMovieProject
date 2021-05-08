package com.bumiayu.dicoding.capstonemovieproject.ui.tv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumiayu.dicoding.capstonemovieproject.R
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.tvshow.TvShow
import com.bumiayu.dicoding.capstonemovieproject.core.utils.ImageUrl
import com.bumiayu.dicoding.capstonemovieproject.databinding.ItemGridBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class TvShowAdapter(private val onClickListener: IOnclickListener) : PagingDataAdapter<TvShow, TvShowAdapter.TvShowViewHolder>(TV_COMPARATOR) {

    override fun onBindViewHolder(holder: TvShowViewHolder, position: Int) {
        val tvShow = getItem(position)
        if(tvShow != null) {
            holder.bind(tvShow)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowViewHolder {
        val binding = ItemGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TvShowViewHolder(binding)
    }

    inner class TvShowViewHolder(private val binding: ItemGridBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tvShow: TvShow) {
            with(binding) {
                tvTitle.text = tvShow.title
                tvScore.text = tvShow.score.toString()

                Glide.with(itemView.context)
                    .load(ImageUrl.poster(tvShow.imgPoster))
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    )
                    .into(imgItemPhoto)

                itemView.setOnClickListener { onClickListener.onClick(tvShow) }
            }
        }
    }

    companion object {
        private val TV_COMPARATOR = object : DiffUtil.ItemCallback<TvShow>() {
            override fun areItemsTheSame(oldItem: TvShow, newItem: TvShow): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: TvShow, newItem: TvShow): Boolean =
                oldItem == newItem
        }
    }

    interface IOnclickListener {
        fun onClick(tvShow: TvShow)
    }
}