package com.example.adminfoodapp.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminfoodapp.R
import com.example.adminfoodapp.databinding.ActivityMainBinding
import com.example.adminfoodapp.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var completedOrderReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.cardvAddfoodAdmin.setOnClickListener {
            val intent = Intent(this, AddItemAdminActivity::class.java)
            startActivity(intent)
        }

        binding.cardvAllfoodAdmin.setOnClickListener {
            val intent = Intent(this, AllFoodAdminActivity::class.java)
            startActivity(intent)
        }

        binding.cardvDeliveryAdmin.setOnClickListener {
            val intent = Intent(this, DeliveryAdminActivity::class.java)
            startActivity(intent)
        }

        binding.cardvProfileAdmin.setOnClickListener {
            val intent = Intent(this, ProfileAdminActivity::class.java)
            startActivity(intent)
        }

        binding.cardvAdduserAdmin.setOnClickListener {
            val intent = Intent(this, CreateUserAdminActivity::class.java)
            startActivity(intent)
        }

        binding.txtPendingorderMainAdmin.setOnClickListener {
            val intent = Intent(this, PendingOrderAdminActivity::class.java)
            startActivity(intent)
        }

        binding.logoutMainAdmin.setOnClickListener{
            auth = FirebaseAuth.getInstance()
            auth.signOut()
            startActivity(Intent(this, LoginAdminActivity::class.java))
            finish()
        }

        pendingOrder()

        completeOrder()

        moneyEarning()

    }

    private fun moneyEarning() {
        var listTotalPay = mutableListOf<Int>()
        completedOrderReference = FirebaseDatabase.getInstance().reference.child("CompleteOrder")
        completedOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot in snapshot.children) {
                    var completeOrder = orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.totalPrice?.replace("đ", "")?.toIntOrNull()?.let { i ->
                        listTotalPay.add(i)
                    }
                }
                binding.moneyEarnngMain.text = listTotalPay.sum().toString() + "đ"
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun completeOrder() {
        val completeOrderReference = database.reference.child("CompleteOrder")
        var completeOrderItemCount = 0
        completeOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                completeOrderItemCount = snapshot.childrenCount.toInt()
                binding.completeCountMain.text = completeOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun pendingOrder() {
        database = FirebaseDatabase.getInstance()
        val pendingOrderReference = database.reference.child("OrderDetails")
        var pendingOrderItemCount = 0
        pendingOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pendingOrderItemCount = snapshot.childrenCount.toInt()
                binding.pendingCountMain.text = pendingOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}