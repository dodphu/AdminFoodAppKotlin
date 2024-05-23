package com.example.adminfoodapp.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodapp.R
import com.example.adminfoodapp.adapterAdmin.DeliveryAdminAdapter
import com.example.adminfoodapp.databinding.ActivityDeliveryAdminBinding
import com.example.adminfoodapp.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DeliveryAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeliveryAdminBinding

    private lateinit var database: FirebaseDatabase
    private var listOfCompleteOrderList: ArrayList<OrderDetails> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDeliveryAdminBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        retriveCompleteOrderDetail()

    }

    private fun retriveCompleteOrderDetail() {
        database = FirebaseDatabase.getInstance()
        val completeOrderReference =
            database.reference.child("CompleteOrder").orderByChild("currentTime")
        completeOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfCompleteOrderList.clear()

                for (orderSnapshot in snapshot.children) {
                    val completeOrder = orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.let { listOfCompleteOrderList.add(it) }
                }

                listOfCompleteOrderList.reverse()
                setDataIntoRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setDataIntoRecyclerView() {
        val customerName = mutableListOf<String>()
        val moneyStatus = mutableListOf<Boolean>()

        for(order in listOfCompleteOrderList){
            order.userName?.let {
                customerName.add(it)
            }
            moneyStatus.add(order.paymentReceived)
        }

        val adapter = DeliveryAdminAdapter(customerName,moneyStatus)
        binding.rycvDeliveryAdmin.adapter = adapter
        binding.rycvDeliveryAdmin.layoutManager = LinearLayoutManager(this)
    }
}