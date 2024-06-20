package com.bangkit.capstone.gestura.adapter
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.capstone.gestura.R
import com.bangkit.capstone.gestura.data.pref.LearningMediaModel
import com.bumptech.glide.Glide

class LearningMediaAdapter(private val learningMediaList: List<LearningMediaModel>) :
    RecyclerView.Adapter<LearningMediaAdapter.LearningMediaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LearningMediaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false)
        return LearningMediaViewHolder(view)
    }

    override fun onBindViewHolder(holder: LearningMediaViewHolder, position: Int) {
        val learningMedia = learningMediaList[position]
        holder.bind(learningMedia)
    }

    override fun getItemCount() = learningMediaList.size

    inner class LearningMediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val thumbnail: ImageView = itemView.findViewById(R.id.iv_thumbnail)
        private val title: TextView = itemView.findViewById(R.id.tv_title)
        private val description: TextView = itemView.findViewById(R.id.tv_desc)

        fun bind(learningMedia: LearningMediaModel) {
            title.text = learningMedia.video_title
            description.text = learningMedia.video_desc
            Glide.with(itemView.context)
                .load(learningMedia.thumbnail_link)
                .into(thumbnail)

            itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = android.net.Uri.parse(learningMedia.video_link)
                itemView.context.startActivity(intent)
            }
        }
    }
}
