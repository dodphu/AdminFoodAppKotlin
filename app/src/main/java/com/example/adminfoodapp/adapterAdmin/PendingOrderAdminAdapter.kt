package com.example.adminfoodapp.adapterAdmin

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminfoodapp.databinding.PendingOrderAdminItemBinding

class PendingOrderAdminAdapter(
    private val context: Context,
    private val customerName: MutableList<String>,
    private val soluong: MutableList<String>,
    private val foodImage: MutableList<String>,
    private val itemClicked: OnItemCLicked,
) : RecyclerView.Adapter<PendingOrderAdminAdapter.PendingHolderViewHolder>() {

    interface OnItemCLicked{
        fun onItemCLickListener(position: Int)
        fun onItemAcceptClickListener(position: Int)
        fun onItemDispatchCLickListener(position: Int)

    }

    inner class PendingHolderViewHolder(private val binding: PendingOrderAdminItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isAccepted = false
        fun bind(position: Int) {
            binding.apply {
                txtNamecusPendingorder.text = customerName[position]
                txtSoluongPendingorder.text = soluong[position]
//                imgvPendingorder.setImageResource(foodImage[position])
                var uriString = foodImage[position]
                var uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(imgvPendingorder)
                btnAppectPendingorder.apply {
                    if (!isAccepted) {
                        text = "Accepted"
                    } else {
                        text = "Dispatch"
                    }
                    setOnClickListener {
                        if (!isAccepted) {
                            text = "Dispatch"
                            isAccepted = true
                            showToast("Order is accepted")
                            itemClicked.onItemAcceptClickListener(position)
                        } else {
                            customerName.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            showToast("Order is dispatched")
                            itemClicked.onItemDispatchCLickListener(position)
                        }
                    }
                }
                itemView.setOnClickListener {
                    itemClicked.onItemCLickListener(position)
                }
            }
        }

        private fun showToast(message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingHolderViewHolder {
        val binding =
            PendingOrderAdminItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PendingHolderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return customerName.size
    }

    override fun onBindViewHolder(holder: PendingHolderViewHolder, position: Int) {
        holder.bind(position)
    }
}