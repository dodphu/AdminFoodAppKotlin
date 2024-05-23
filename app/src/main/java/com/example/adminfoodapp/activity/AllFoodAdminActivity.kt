package com.example.adminfoodapp.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodapp.R
import com.example.adminfoodapp.adapterAdmin.AllItemAdminAdapter
import com.example.adminfoodapp.databinding.ActivityAllFoodAdminBinding
import com.example.adminfoodapp.model.FoodInfor
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllFoodAdminActivity() : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private var menuItems: ArrayList<FoodInfor> = ArrayList()
    private lateinit var binding: ActivityAllFoodAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAllFoodAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        databaseReference = FirebaseDatabase.getInstance().reference
        retriveMenuItems()

    }

    private fun retriveMenuItems() {
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("menu")
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                menuItems.clear()
                for (foodSnapshot in snapshot.children) {
                    val itemmenu = foodSnapshot.getValue(FoodInfor::class.java)
                    itemmenu?.let {
                        menuItems.add(it)
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("phu", "onCancelled: ${error.message}")
            }

        })
    }

    private fun setAdapter() {
//        val adapter = AllItemAdminAdapter(this@AllFoodAdminActivity, menuItems,databaseReference)

        val adapter = AllItemAdminAdapter(this, menuItems, databaseReference) { position ->
            deleteMenuItems(position)
        }
        binding.rycvAllfoodAdmin.layoutManager = LinearLayoutManager(this)
        binding.rycvAllfoodAdmin.adapter = adapter

    }

    private fun deleteMenuItems(position: Int) {
        val menuItemToDelete = menuItems[position]
        val menuItemKey = menuItemToDelete.key
        val foodMenuReference = database.reference.child("menu").child(menuItemKey!!)
        foodMenuReference.removeValue().addOnCompleteListener { task ->
            if(task.isSuccessful){
                menuItems.removeAt(position)
                binding.rycvAllfoodAdmin.adapter?.notifyItemRemoved(position)
            }else{
                Toast.makeText(this,"Xóa item không thành công !", Toast.LENGTH_SHORT).show()
            }
        }
    }
}