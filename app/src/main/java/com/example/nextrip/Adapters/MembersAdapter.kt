package com.example.nextrip.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nextrip.R
import com.example.nextrip.model.MemberData

class MembersAdapter(private val memberList: ArrayList<MemberData>): RecyclerView.Adapter<MembersAdapter.ViewHolder>() {

    private lateinit var memberListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setonItemClickListener(clickListener: onItemClickListener){
        memberListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_members, parent, false)
        return ViewHolder(itemView, memberListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentMember = memberList[position]
        holder.name.text = currentMember.memberName
        holder.phonenumber.text = currentMember.memberMobile
    }

    override fun getItemCount(): Int {
        return memberList.size
    }

    class ViewHolder(view: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(view) {

        val name : TextView = itemView.findViewById(R.id.mtitle)
        val phonenumber : TextView = itemView.findViewById(R.id.msubtitle)

        init {
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}