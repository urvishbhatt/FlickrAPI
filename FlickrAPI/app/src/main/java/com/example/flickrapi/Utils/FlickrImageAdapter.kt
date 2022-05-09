package com.example.flickrapi.Utils

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flickrapi.Model.FlickrPhotoProperties
import com.example.flickrapi.R

class FlickrImageAdapter : RecyclerView.Adapter<FlickrImageAdapter.ViewHolder>() {

    var data = listOf<FlickrPhotoProperties>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.image_holder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.textView.text = data[position].title

        Glide
            .with(holder.imageView.context)
            .load(ImageURLCreator(data[position]))
            .centerCrop()
            .into(holder.imageView);
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val textView : TextView = itemView.findViewById(R.id.image_text)
        val imageView : ImageView = itemView.findViewById(R.id.flickr_image)
    }
}
