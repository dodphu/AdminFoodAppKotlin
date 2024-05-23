package com.example.adminfoodapp.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminfoodapp.R
import com.example.adminfoodapp.databinding.ActivityAddItemAdminBinding
import com.example.adminfoodapp.model.FoodInfor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddItemAdminActivity : AppCompatActivity() {
    private lateinit var foodName: String
    private lateinit var foodPrice: String
    private lateinit var foodDescription: String
    private lateinit var foodThanhphan: String
    private var foodImageUri: Uri? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private lateinit var binding: ActivityAddItemAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddItemAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.txtSelectimageAdditemAdmin.setOnClickListener {
            imageSelect.launch("image/*")
        }
//        binding.txtSelectimageAdditemAdmin.setOnClickListener {
//            imageSelect.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//        }

        binding.btnBackAdditemAdmin.setOnClickListener {
            finish()
        }

        binding.btnAdditemAdmin.setOnClickListener {
            foodName = binding.edtAddfoodNameAdmin.text.toString().trim()
            foodPrice = binding.edtAddfoodPriceAdmin.text.toString().trim()
            foodDescription = binding.edtAddfoodDescriptionAdmin.text.toString().trim()
            foodThanhphan = binding.edtAddfoodThanhphanAdmin.text.toString().trim()
            if (!foodName.isBlank() || !foodPrice.isBlank() || !foodDescription.isBlank() || !foodThanhphan.isBlank()) {
                uploadData()
                Toast.makeText(this, "Thêm món ăn thành công !", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Hãy điền đầy đủ thông tin !", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun uploadData() {
        val menuRef = database.getReference("menu")
        val newItemKey = menuRef.push().key

        if (foodImageUri != null) {
            val storeRef = FirebaseStorage.getInstance().reference
            val imageRef = storeRef.child("menu_images/${newItemKey}.jpg")
            val uploadTask = imageRef.putFile(foodImageUri!!)

            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUrl = task.result
                        val newItem = FoodInfor(
                            newItemKey,
                            foodName = foodName,
                            foodPrice = foodPrice,
                            foodDescription = foodDescription,
                            foodImage = downloadUrl.toString(), // Lưu URL của ảnh vào đối tượng FoodInfor
                            foodThanhPhan = foodThanhphan
                        )
                      //  Log.d("phu", "uploadData: ${downloadUrl.toString()}")
                        newItemKey?.let { key ->
                            menuRef.child(key).setValue(newItem)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this, "Data uploaded successfully", Toast.LENGTH_SHORT
                                    ).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        this,
                                        "Data uploaded failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    } else {
                        Toast.makeText(this, "Failed to get image URL", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
        }
    }

    private val imageSelect =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                binding.imgvImageSelectedAdditemAdmin.setImageURI(uri)
                foodImageUri = uri
                Log.d("phu", ": $uri")
            }
        }
}