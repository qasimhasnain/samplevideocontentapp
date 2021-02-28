package com.challenge.starzplay.Home.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.challenge.datamanager.Model.MediaItem
import com.challenge.starzplay.Interface.ViewInterface
import com.challenge.starzplay.R
import com.challenge.starzplay.Utils.Config
import kotlinx.android.synthetic.main.item_media.view.*

class MediaItemAdapter (private val mContext: Context, private val viewInterface:ViewInterface,private var items : ArrayList<MediaItem>) : RecyclerView.Adapter<MediaItemAdapter.MediaItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaItemViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_media, parent, false)
        return MediaItemViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MediaItemViewHolder, position: Int) {
        val item = items[position]
        var imageUrl : String? = item.poster_path

        when(item.media_type){
            Config.Person -> imageUrl = item.profile_path
        }
        holder.imageView.setImageResource(R.drawable.search_bg)
        try {
            imageUrl?.let {
                Glide.with(mContext)
                        .load(Config.ImageBaseUrl + Config.ImageSizeSmall + imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.drawable.search_bg)
                        .into(holder.imageView)
            }
        }catch (e : java.lang.Exception){
            e.printStackTrace()
        }

        holder.imageView.setOnClickListener {
            viewInterface.onMediaItemClick(item)}
    }

    fun updateAdapter(newList : List<MediaItem>){
        items.addAll(newList)
        notifyDataSetChanged()
    }

    class MediaItemViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.child_imageView
    }
}