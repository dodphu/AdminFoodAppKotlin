package com.example.adminfoodapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminfoodapp.R
import com.example.adminfoodapp.databinding.ActivitySignupAdminBinding
import com.example.adminfoodapp.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SignupAdminActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var userName: String
    private lateinit var nameofRes: String
    private lateinit var database: DatabaseReference


    private lateinit var binding: ActivitySignupAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth
        database = Firebase.database.reference


        binding.btnSignupSignupAdmin.setOnClickListener {
            userName = binding.edtNameSignupAdmin.text.toString().trim()
            nameofRes = binding.edtNameResSignupAdmin.text.toString().trim()
            email = binding.edtEmailorphoneSignupAdmin.text.toString().trim()
            password = binding.edtPassSignupAdmin.text.toString().trim()
            if (userName.isBlank() || nameofRes.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Điền đầy đủ thông tin !", Toast.LENGTH_SHORT).show()
            } else {
                createAccount(email, password)
            }

        }

        binding.txtLoginnowSignupAdmin.setOnClickListener {
            val intent = Intent(this, LoginAdminActivity::class.java)
            startActivity(intent)
        }

    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { tasks ->
            if (tasks.isSuccessful) {
                saveUserData()
                Toast.makeText(this, "Tạo tài khoản thành công !", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginAdminActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Tạo tài khoản không thành công !", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserData() {
        userName = binding.edtNameSignupAdmin.text.toString().trim()
        nameofRes = binding.edtNameResSignupAdmin.text.toString().trim()
        email = binding.edtEmailorphoneSignupAdmin.text.toString().trim()
        password = binding.edtPassSignupAdmin.text.toString().trim()

        val user = UserModel(userName, nameofRes, email, password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        database.child("user").child(userId).setValue(user)

    }
}