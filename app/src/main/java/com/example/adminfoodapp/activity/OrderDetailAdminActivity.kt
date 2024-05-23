package com.example.adminfoodapp.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodapp.R
import com.example.adminfoodapp.adapterAdmin.OrderDetailAdminAdapter
import com.example.adminfoodapp.databinding.ActivityOrderDetailAdminBinding
import com.example.adminfoodapp.model.OrderDetails

class OrderDetailAdminActivity : AppCompatActivity() {
    private val binding: ActivityOrderDetailAdminBinding by lazy {
        ActivityOrderDetailAdminBinding.inflate(layoutInflater)
    }
    private var userName: String? = null
    private var address: String? = null
    private var phoneNumber: String? = null
    private var totalPrice: String? = null
    private var foodName: ArrayList<String> = arrayListOf()
    private var foodImages: ArrayList<String> = arrayListOf()
    private var foodQuantity: ArrayList<Int> = arrayListOf()
    private var foodPrices: ArrayList<String> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getDataFromIntent()
    }

    private fun getDataFromIntent() {
        val receiveOrderDetails = intent.getSerializableExtra("userOrderDetails") as OrderDetails
        receiveOrderDetails?.let { orderDetails ->

            userName = receiveOrderDetails.userName
            foodName = receiveOrderDetails.foodNames as ArrayList<String>
            foodImages = receiveOrderDetails.foodImages  as ArrayList<String>
            foodQuantity = receiveOrderDetails.foodQuantities  as ArrayList<Int>
            address = receiveOrderDetails.address
            phoneNumber = receiveOrderDetails.phoneNumber
            foodPrices = receiveOrderDetails.foodPrices  as ArrayList<String>
            totalPrice = receiveOrderDetails.totalPrice

            setUserDetail()
            setAdapter()

        }

    }

    private fun setAdapter() {
        binding.rycvTtdonhangAdmin.layoutManager = LinearLayoutManager(this)
        val adapter = OrderDetailAdminAdapter(this, foodName, foodImages, foodQuantity, foodPrices)
        binding.rycvTtdonhangAdmin.adapter = adapter
    }

    private fun setUserDetail() {
        binding.nameAdminTt.text = userName
        binding.addressAdminTt.text = address
        binding.phoneAdminTt.text = phoneNumber
        binding.tongtienAdminTt.text = totalPrice


    }
}