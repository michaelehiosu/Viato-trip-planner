package com.michael.viatoapp.classes

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.michael.viatoapp.R
import com.michael.viatoapp.activities.NearbyDetailsActivity

class ActivityAdapter(private var activities: MutableList<Activities>) :
    RecyclerView.Adapter<ActivityAdapter.viewHolder>() {

    override fun getItemCount(): Int {
        return activities.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.nearby_activity_recycler, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val activity = activities[position]
//        Glide.with(holder.itemView)
//            .load(activity.imageUrl)
//            .placeholder(R.drawable.map)
//            .into(holder.image) // To be used later on

        holder.image.setImageResource(activity.imageUrl)
        holder.description.text = activity.description
        holder.title.text = activity.name
        holder.cardView.setOnClickListener{
            val context = holder.itemView.context
            val intent = Intent(context, NearbyDetailsActivity::class.java)
            context.startActivity(intent)
        }


    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardView : CardView = itemView.findViewById(R.id.cardview_activity)
        var image: ImageView = itemView.findViewById(R.id.activity_image)
        var title: TextView = itemView.findViewById(R.id.activity_title)
        var description : TextView = itemView.findViewById(R.id.activity_description)
    }
}