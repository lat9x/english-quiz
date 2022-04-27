package self.tuan.hocmaians.ui.fragments.manage.topics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import self.tuan.hocmaians.databinding.EachCourseAndTopicBinding
import self.tuan.hocmaians.data.entities.Topic
import self.tuan.hocmaians.util.Constants
import self.tuan.hocmaians.util.Constants.ADMIN_ADDED

class ManageTopicsAdapter : RecyclerView.Adapter<ManageTopicsAdapter.ManageTopicsViewHolder>() {

    // when user click edit topic
    private var onEditTopicListener: ((Topic) -> Unit)? = null

    fun setOnEditTopicListener(listener: (Topic) -> Unit) {
        onEditTopicListener = listener
    }

    // when user click an itemView (whole topic), then pass topicId and topicName
    private var onTopicClickListener: ((Int, String) -> Unit)? = null

    fun setOnTopicClickListener(listener: (Int, String) -> Unit) {
        onTopicClickListener = listener
    }

    inner class ManageTopicsViewHolder(
        private val binding: EachCourseAndTopicBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(topic: Topic) {
            binding.apply {
                tvCourseTopicName.text = topic.name

                if (topic.isUserAdded == ADMIN_ADDED) {
                    // TODO: hide ivEditCourseTopic if the topic is added by admin, delete click listener on ImageView
//                    ivEditCourseTopic.setOnClickListener {
//                        onEditTopicListener?.let {
//                            it(topic)
//                        }
//                    }

                    // production code
                    ivEditCourseTopic.visibility = View.GONE
                } else {
                    ivEditCourseTopic.setOnClickListener {
                        onEditTopicListener?.let {
                            it(topic)
                        }
                    }
                }
            }
            itemView.setOnClickListener {
                onTopicClickListener?.let {
                    it(topic.id, topic.name)
                }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Topic>() {
        override fun areItemsTheSame(oldItem: Topic, newItem: Topic): Boolean =
            (oldItem.id == newItem.id)

        override fun areContentsTheSame(oldItem: Topic, newItem: Topic): Boolean =
            (oldItem == newItem)
    }

    val differ: AsyncListDiffer<Topic> = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageTopicsViewHolder {
        val binding = EachCourseAndTopicBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ManageTopicsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ManageTopicsViewHolder, position: Int) {
        val topic: Topic = differ.currentList[position]
        holder.bind(topic)
    }

    override fun getItemCount(): Int = differ.currentList.size
}