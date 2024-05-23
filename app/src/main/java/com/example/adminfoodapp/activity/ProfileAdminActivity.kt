package com.example.adminfoodapp.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminfoodapp.R
import com.example.adminfoodapp.databinding.ActivityProfileAdminBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileAdminBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var adminReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        adminReference = database.reference.child("user")

        binding.edtNameProfileAdmin.isEnabled = false
        binding.edtAddressProfileAdmin.isEnabled = false
        binding.edtEmailProfileAdmin.isEnabled = false
        binding.edtSdtProfileAdmin.isEnabled = false
        binding.edtPassProfileAdmin.isEnabled = false
        binding.btnSavettProfileAdmin.isEnabled = false

        var checkIsEnabled = false
        binding.txtEditprofileAdmin.setOnClickListener {
            checkIsEnabled = !checkIsEnabled
            binding.edtNameProfileAdmin.isEnabled = checkIsEnabled
            binding.edtAddressProfileAdmin.isEnabled = checkIsEnabled
            binding.edtEmailProfileAdmin.isEnabled = checkIsEnabled
            binding.edtSdtProfileAdmin.isEnabled = checkIsEnabled
            binding.edtPassProfileAdmin.isEnabled = checkIsEnabled
            binding.btnSavettProfileAdmin.isEnabled = checkIsEnabled
            if (checkIsEnabled) {
                binding.edtNameProfileAdmin.requestFocus()
            }
        }

        binding.btnSavettProfileAdmin.setOnClickListener {
            updateUserData()
        }

        retrieveUserData()

    }

    private fun updateUserData() {
        var updateName = binding.edtNameProfileAdmin.text.toString()
        var updateEmail = binding.edtEmailProfileAdmin.text.toString()
        var updatePass = binding.edtPassProfileAdmin.text.toString()
        var updatePhone = binding.edtSdtProfileAdmin.text.toString()
        var updateAddress = binding.edtAddressProfileAdmin.text.toString()

        val currentUserUid = auth.currentUser?.uid
        if (currentUserUid != null) {
            val userReference = adminReference.child(currentUserUid)
            userReference.child("name").setValue(updateName)
            userReference.child("email").setValue(updateEmail)
            userReference.child("password").setValue(updatePass)
            userReference.child("phone").setValue(updatePhone)
            userReference.child("address").setValue(updateAddress)


            Toast.makeText(this, "Profile updated thành công!", Toast.LENGTH_SHORT).show()
            auth.currentUser?.updateEmail(updateEmail)
            auth.currentUser?.updatePassword(updatePass)
        } else {
            Toast.makeText(this, "Profile updated không thành công!", Toast.LENGTH_SHORT).show()

        }
    }

    private fun retrieveUserData() {
        val currentUserUid = auth.currentUser?.uid
        if (currentUserUid != null) {
            val userReference = adminReference.child(currentUserUid)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var ownerName = snapshot.child("name").getValue()
                        var email = snapshot.child("email").getValue()
                        var password = snapshot.child("password").getValue()
                        var address = snapshot.child("address").getValue()
                        var phone = snapshot.child("phone").getValue()
                        setDataToTextView(ownerName, email, password, address, phone)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

    }

    private fun setDataToTextView(
        ownerName: Any?,
        email: Any?,
        password: Any?,
        address: Any?,
        phone: Any?
    ) {
        binding.edtNameProfileAdmin.setText(ownerName.toString())
        binding.edtEmailProfileAdmin.setText(email.toString())
        binding.edtPassProfileAdmin.setText(password.toString())
        binding.edtSdtProfileAdmin.setText(phone.toString())
        binding.edtAddressProfileAdmin.setText(address.toString())

    }
}