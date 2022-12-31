package com.example.nextrip.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nextrip.R
import com.example.nextrip.model.MemberData

class MemberCompleteAdapter(private val memberList: ArrayList<MemberData>): RecyclerView.Adapter<MemberCompleteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_completed_members, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentMember = memberList[position]
        holder.name.text = currentMember.memberName
        holder.phonenumber.text = currentMember.memberMobile
        holder.emphonenumber.text = currentMember.memberEmergencyNumber
        holder.address.text = currentMember.memberAddress
    }

    override fun getItemCount(): Int {
        return memberList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val name : TextView = itemView.findViewById(R.id.list_member_txt_show_name)
        val phonenumber : TextView = itemView.findViewById(R.id.list_member_txt_show_phonenumber)
        val emphonenumber : TextView = itemView.findViewById(R.id.list_member_txt_show_emphonenumber)
        val address : TextView = itemView.findViewById(R.id.list_member_txt_show_address)
        }
    }