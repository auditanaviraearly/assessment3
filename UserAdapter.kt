package org.d3if3077.githubapps.ui.main

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.d3if3077.githubapps.data.response.ItemsItem
import org.d3if3077.githubapps.databinding.ListUserBinding
import org.d3if3077.githubapps.ui.description.DetailActivity

class UserAdapter (private var listUser : List<ItemsItem>) : RecyclerView.Adapter<UserAdapter.ListViewHolder>(){
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: ItemsItem)
    }

    fun updateData(newList: List<ItemsItem>) {
        listUser = newList
        notifyDataSetChanged() // Or use more specific notifyItem...() methods
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    class ListViewHolder (val binding: ListUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(viewHolder: ListViewHolder, position: Int) {
        Glide.with(viewHolder.itemView.context)
            .load(listUser[position].avatarUrl)
            .into(viewHolder.binding.imgItemPhoto)
        viewHolder.binding.tvItemName.text = listUser[position].login

        viewHolder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listUser[position])
        }

        Log.d("UserAdapter", "Binding item: ${listUser[position]}")

        viewHolder.binding.root.setOnClickListener {
            val intentDetail = Intent(viewHolder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra("ID", listUser[position].id)
            intentDetail.putExtra("USERNAME", listUser[position].login)
            intentDetail.putExtra("AVATAR", listUser[position].avatarUrl)
            viewHolder.itemView.context.startActivity(intentDetail)
        }
    }


}