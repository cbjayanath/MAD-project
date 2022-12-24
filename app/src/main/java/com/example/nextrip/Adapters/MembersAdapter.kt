package com.example.nextrip.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
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

//        holder.realtiveLayout.visibility=if (isExpandable)View.VISIBLE else View.GONE
//        holder.linearLayout.setOnClickListener{
//            val moreDetails=memberList[position]
//            notifyItemChanged(position)
//        }

    }

    override fun getItemCount(): Int {
        return memberList.size
    }

    class ViewHolder(view: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(view) {

        val name : TextView = itemView.findViewById(R.id.list_member_txt_show_name)
        val phonenumber : TextView = itemView.findViewById(R.id.list_member_txt_show_phonenumber)

        init {
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}