package com.example.adminfoodapp.adapterAdmin

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adminfoodapp.databinding.DeliveryAdminItemBinding

class DeliveryAdminAdapter(
    private val customerName: MutableList<String>,
    private val moneyStatus: MutableList<Boolean>
) : RecyclerView.Adapter<DeliveryAdminAdapter.DeliveryViewHolder>() {
    inner class DeliveryViewHolder(private val binding: DeliveryAdminItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                txtCusNameAdmin.text = customerName[position]
                if (moneyStatus[position] == true) {
                    txtMoneystatusAdmin.text = "Received"
                } else {
                    txtMoneystatusAdmin.text = "NotReceived"
                }

                val colorMap = mapOf(
                    true to Color.GREEN,
                    false to Color.RED
                )
                txtMoneystatusAdmin.setTextColor(colorMap[moneyStatus[position]] ?: Color.BLACK)
                cardvStatusIDAdmin.backgroundTintList =
                    ColorStateList.valueOf(colorMap[moneyStatus[position]] ?: Color.BLACK)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val binding =
            DeliveryAdminItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeliveryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return customerName.size
    }

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        holder.bind(position)
    }
}