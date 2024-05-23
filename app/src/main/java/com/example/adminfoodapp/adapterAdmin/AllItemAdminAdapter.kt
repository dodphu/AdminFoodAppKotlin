package com.example.adminfoodapp.adapterAdmin

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminfoodapp.databinding.AllfoodAdminItemBinding
import com.example.adminfoodapp.model.FoodInfor
import com.google.firebase.database.DatabaseReference

class AllItemAdminAdapter(
    private val context: Context,
    private val menuList: ArrayList<FoodInfor>,
    databaseReference: DatabaseReference,
    private val onDeleteClickListener:(position: Int) -> Unit

) : RecyclerView.Adapter<AllItemAdminAdapter.ViewHolder>() {
    private val soluongItem = IntArray(menuList.size) { 1 }

    inner class ViewHolder(private val binding: AllfoodAdminItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val soluong = soluongItem[position]
                val menuItem = menuList[position]
                val uriString = menuItem.foodImage
                val uri = Uri.parse(uriString)
                txtCartItemNameAdmin.text = menuItem.foodName
                txtCartItemPriceAdmin.text = menuItem.foodPrice
                Glide.with(context)
                    .load(uri)
                    .into(imgvCartitemAdmin)
//                imgvCartitemAdmin.setImageResource(menuList[position])
                btnGiamCartItemAdmin.setOnClickListener {
                    giamSoluong(position)
                }
                btnTangCartItemAdmin.setOnClickListener {
                    tangSoluong(position)
                }

                btnRemoveCartItemAdmin.setOnClickListener {
                    onDeleteClickListener(position)
                }

            }
        }

        private fun tangSoluong(position: Int) {
            if (soluongItem[position] < 10) {
                soluongItem[position]++
                binding.txtSoluongCartItemAdmin.text = soluongItem[position].toString()
            }
        }

        private fun giamSoluong(position: Int) {
            if (soluongItem[position] > 1) {
                soluongItem[position]--
                binding.txtSoluongCartItemAdmin.text = soluongItem[position].toString()
            }
        }

        private fun xoaItem(position: Int) {
            menuList.removeAt(position)
            menuList.removeAt(position)
            menuList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, menuList.size)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            AllfoodAdminItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }
}