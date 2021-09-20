package com.example.network.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.network.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private lateinit var  binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        //firebaseUser = auth.currentUser!!

        /* checks if user login then naviagte to user screen
        if (firebaseUser != null){

            val intent = Intent(this@MainActivity,
                HomeActivity::class.java)
            startActivity(intent)
            finish()

        }*/

        binding.btnLogin.setOnClickListener {
            val email = binding.etLoginEmail.text.toString()
            val password = binding.etLoginPassword.text.toString()

            if (TextUtils.isEmpty(email)&& TextUtils.isEmpty(password)){
                Toast.makeText(this@MainActivity,"email and password are required", Toast.LENGTH_SHORT).show()
            }else{
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){

                            binding.etLoginEmail.text.clear()
                            binding.etLoginPassword.text.clear()

                            val intent = Intent(this@MainActivity,
                                HomeActivity::class.java)
                            startActivity(intent)
                            finish()

                        }else {
                            Toast.makeText(
                                this@MainActivity,
                                "email or password are invalid",
                                Toast.LENGTH_SHORT
                            ).show()

                            binding.etLoginEmail.text.clear()
                            binding.etLoginPassword.text.clear()
                        }
                }
            }
        }

        binding.btnReg.setOnClickListener {
            val intent = Intent(this@MainActivity,
                SignUp::class.java)
            startActivity(intent)
        }
    }

}