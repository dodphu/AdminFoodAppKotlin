package com.example.adminfoodapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodapp.R
import com.example.adminfoodapp.adapterAdmin.PendingOrderAdminAdapter
import com.example.adminfoodapp.databinding.ActivityPendingOrderAdminBinding
import com.example.adminfoodapp.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PendingOrderAdminActivity : AppCompatActivity(), PendingOrderAdminAdapter.OnItemCLicked {
    private lateinit var binding: ActivityPendingOrderAdminBinding
    private var listOfName: MutableList<String> = mutableListOf()
    private var listOfTotalPrice: MutableList<String> = mutableListOf()
    private var listOfImageFirstFoodOrder: MutableList<String> = mutableListOf()
    private var listOfOrderItem: ArrayList<OrderDetails> = arrayListOf()
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseOrderDetails: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPendingOrderAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        database = FirebaseDatabase.getInstance()
        databaseOrderDetails = database.reference.child("OrderDetails")
        getOrderDetails()

    }

    private fun getOrderDetails() {
        databaseOrderDetails.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot in snapshot.children) {
                    val orderDetails = orderSnapshot.getValue(OrderDetails::class.java)
                    orderDetails?.let {
                        listOfOrderItem.add(it)
                    }
                }
                addDataToListForRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun addDataToListForRecyclerView() {
        for (orderItem in listOfOrderItem) {
            orderItem.userName?.let { listOfName.add(it) }
            orderItem.totalPrice?.let { listOfTotalPrice.add(it) }
            orderItem.foodImages?.filterNot { it.isEmpty() }?.forEach {
                listOfImageFirstFoodOrder.add(it)
            }
        }
        setAdapter()
    }

    private fun setAdapter() {
        binding.rycvPendingorderAdmin.layoutManager = LinearLayoutManager(this)
        val adapter = PendingOrderAdminAdapter(
            this,
            listOfName,
            listOfTotalPrice,
            listOfImageFirstFoodOrder,
            this
        )
        binding.rycvPendingorderAdmin.adapter = adapter
    }

    override fun onItemCLickListener(position: Int) {
        val intent = Intent(this, OrderDetailAdminActivity::class.java)
        val userOrderDetails = listOfOrderItem[position]
        intent.putExtra("userOrderDetails", userOrderDetails)
        startActivity(intent)
    }

    override fun onItemAcceptClickListener(position: Int) {
        val childItemPushKey = listOfOrderItem[position].itemPushKey
        val clickItemOrderRefrence = childItemPushKey?.let {
            database.reference.child("OrderDetails").child(it)
        }
        clickItemOrderRefrence?.child("orderAccepted")?.setValue(true)
        updateOrderAcceptStatus(position)

    }

    private fun updateOrderAcceptStatus(position: Int) {
        val userIdOfClickItem = listOfOrderItem[position].userUid
        val pushKeyOfClickItem = listOfOrderItem[position].itemPushKey
        val buyHistoryReference =
            database.reference.child("user").child(userIdOfClickItem!!).child("BuyHistory")
                .child(pushKeyOfClickItem!!)
        buyHistoryReference.child("orderAccepted").setValue(true)
        databaseOrderDetails.child(pushKeyOfClickItem).child("orderAccepted").setValue(true)
    }

    override fun onItemDispatchCLickListener(position: Int) {
        val dispatchItemPushKey = listOfOrderItem[position].itemPushKey
        val dispatchItemOrderReference =
            database.reference.child("CompleteOrder").child(dispatchItemPushKey!!)
        dispatchItemOrderReference.setValue(listOfOrderItem[position])
            .addOnSuccessListener {
                deleteThisItemFromOrderDetail(dispatchItemPushKey)
            }
    }

    private fun deleteThisItemFromOrderDetail(dispatchItemPushKey: String) {
        val orderDetaiItemsReference =
            database.reference.child("OrderDetails").child(dispatchItemPushKey)
        orderDetaiItemsReference.removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Order is dispatched", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Order is not dispatched", Toast.LENGTH_SHORT).show()

            }
    }

}