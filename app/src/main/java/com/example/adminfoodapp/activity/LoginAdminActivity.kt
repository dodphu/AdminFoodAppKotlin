package com.example.adminfoodapp.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminfoodapp.R
import com.example.adminfoodapp.databinding.ActivityLoginAdminBinding
import com.example.adminfoodapp.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginAdminActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityLoginAdminBinding
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth
        database = Firebase.database.reference
        //google signin
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOption)

        binding.btnLoginLoginAdmin.setOnClickListener {
            email = binding.edtEmailLoginAdmin.text.toString().trim()
            password = binding.edtPassLoginAdmin.text.toString().trim()
            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Hãy điền đầy đủ thông tin !", Toast.LENGTH_SHORT).show()
            } else {
                createUserAccount(email, password)
            }
        }

        binding.btnGoogleLoginAdmin.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }

        binding.txtCreateAccLoginAdmin.setOnClickListener {
            val intent = Intent(this, SignupAdminActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createUserAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                Toast.makeText(this, "Login thành công !", Toast.LENGTH_SHORT).show()
                updateUI(user)
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Toast.makeText(
                            this,
                            "Tạo tài khoản và Login thành công !",
                            Toast.LENGTH_SHORT
                        ).show()
                        saveUserData(user)
                        updateUI(user)
                    } else {
                        Toast.makeText(
                            this,
                            "Tài khoản hoặc mật khẩu không đúng!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun saveUserData(user: FirebaseUser?) {
        email = binding.edtEmailLoginAdmin.text.toString().trim()
        password = binding.edtPassLoginAdmin.text.toString().trim()
        val user = UserModel(email = email, password = password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        database.child("user").child(userId).setValue(user)
    }

    private fun updateUI(user: FirebaseUser?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount = task.result
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Đăng nhập bằng tài khoản Google thành công!",
                                Toast.LENGTH_SHORT
                            ).show()
                            updateUI(authTask.result?.user)
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "Đăng nhập bằng tài khoản Google không thành công!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Đăng nhập bằng tài khoản Google không thành công!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    //kiểm tra đã login chưa
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}