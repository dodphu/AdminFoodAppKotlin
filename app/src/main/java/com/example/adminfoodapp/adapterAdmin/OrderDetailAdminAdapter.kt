package com.example.adminfoodapp.adapterAdmin

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminfoodapp.databinding.OrderDetailItemBinding

class OrderDetailAdminAdapter(
    private var context: Context,
    private var foodName: ArrayList<String>,
    private var foodImage: ArrayList<String>,
    private var foodQuantity: ArrayList<Int>,
    private var foodPrice: ArrayList<String>
) : RecyclerView.Adapter<OrderDetailAdminAdapter.OrderDetailsViewHolder>() {
    inner class OrderDetailsViewHolder(private val binding: OrderDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                txtNameOrderdetail.text = foodName[position]
                txtSoluongOrderdetail.text = foodQuantity[position].toString()
                val uriString = foodImage[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(imgvOrderDetail)
                txtTongtienOrderdetail.text = foodPrice[position]
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
        val binding =
            OrderDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderDetailsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return foodName.size
    }

    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {
        holder.bind(position)
    }
}