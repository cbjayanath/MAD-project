package com.example.nextrip.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.nextrip.R
import com.example.nextrip.model.MemberData


class MemberAdapter (val c:Context,val memberList: ArrayList<MemberData>):RecyclerView.Adapter<MemberAdapter.memberViewHolder>()
{
    inner class memberViewHolder(val v: View) :RecyclerView.ViewHolder(v){
        var name:TextView
        var mbNum:TextView
        var mMenus: ImageView
        init {
            name = v.findViewById<TextView>(R.id.mtitle)
            mbNum = v.findViewById<TextView>(R.id.msubtitle)
            mMenus = v.findViewById(R.id.moreIcon)
            mMenus.setOnClickListener { popupMenus(it) }
        }

        private fun popupMenus(v:View) {
            val position = memberList[adapterPosition]
            val popupMenus = PopupMenu(c,v)
            popupMenus.inflate(R.menu.more_menu)
            popupMenus.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.editText->{
                        val v = LayoutInflater.from(c).inflate(R.layout.add_member,null)
                        val name = v.findViewById<EditText>(R.id.mName)
                        val number = v.findViewById<EditText>(R.id.mTelephone)
                        AlertDialog.Builder(c)
                            .setView(v)
                            .setPositiveButton("Ok"){
                                    dialog,_->
                                position.memberName = name.text.toString()
                                position.memberMobile = number.text.toString()
                                notifyDataSetChanged()
                                Toast.makeText(c,"Member is Edited",Toast.LENGTH_SHORT).show()
                                dialog.dismiss()

                            }
                            .setNegativeButton("Cancel"){
                                    dialog,_->
                                dialog.dismiss()

                            }
                            .create()
                            .show()

                        true
                    }
                    R.id.delete->{
                        /**set delete*/
                        AlertDialog.Builder(c)
                            .setTitle("Delete")
                            .setIcon(R.drawable.ic_warning)
                            .setMessage("Are you sure delete this Information")
                            .setPositiveButton("Yes"){
                                    dialog,_->
                                memberList.removeAt(adapterPosition)
                                notifyDataSetChanged()
                                Toast.makeText(c,"Deleted this Information",Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                            .setNegativeButton("No"){
                                    dialog,_->
                                dialog.dismiss()
                            }
                            .create()
                            .show()

                        true
                    }
                    else-> true
                }

            }
            popupMenus.show()
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popupMenus)
            menu.javaClass.getDeclaredMethod("setForceShowIcon",Boolean::class.java)
                .invoke(menu,true)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): memberViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        val v=inflater.inflate(R.layout.list_members,parent,false)
        return memberViewHolder(v)
    }

    override fun onBindViewHolder(holder: memberViewHolder, position: Int) {
        val newList=memberList[position]
        holder.name.text=newList.memberName
        holder.mbNum.text=newList.memberMobile
    }

    override fun getItemCount(): Int {
        return memberList.size
    }
}