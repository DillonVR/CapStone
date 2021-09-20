package com.example.network.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.network.data.User
import com.example.network.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SignUp : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnSignUp.setOnClickListener {


            val userName = binding.etRegName.text.toString().trim { it <= ' ' }
            val email = binding.etRegEmail.text.toString().trim { it <= ' ' }
            val password = binding.etRegPassword.text.toString().trim { it <= ' ' }
            registerUser(userName, email, password)
            /*when {
                TextUtils.isEmpty(binding.etRegName.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@SignUp,
                        "username is required",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(binding.etRegEmail.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this@SignUp, "email is required", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(binding.etRegPassword.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this@SignUp, "password is required", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(binding.etConfirmPassword.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this@SignUp, "confirm password is required", Toast.LENGTH_SHORT)
                        .show()
                }
                binding.etRegPassword != binding.etConfirmPassword -> {
                    Toast.makeText(this@SignUp, "password not match", Toast.LENGTH_SHORT).show()
                }
                else -> {



                }
            }*/

        }

        binding.btnLogin.setOnClickListener {
            onBackPressed()
        }

    }

    private fun registerUser(userName: String, email: String, password: String) {
         auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{ task ->
            if (task.isSuccessful){
                //get the current user firebase ID
                val firebaseUser = task.result!!.user!!
                //get path to the firbase database
                databaseReference = FirebaseDatabase.getInstance().getReference("Users")

                val userId = firebaseUser.uid

                val user = User(userId,userName,"")

                databaseReference.child(userId).setValue(user).addOnSuccessListener {

                    //open home activity
                    binding.etRegName.text.clear()
                    binding.etRegEmail.text.clear()
                    binding.etRegPassword.text.clear()
                    binding.etConfirmPassword.text.clear()

                    val intent = Intent(this@SignUp,
                        HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                //let the user they were not added to the data base
                }.addOnFailureListener {

                    Toast.makeText(
                        this@SignUp,
                        "Did not get added to database",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else
                Toast.makeText(applicationContext,"Unsuccessful", Toast.LENGTH_SHORT).show()
            }
        }
        /*Toast.makeText(
            this@SignUp,
            "Did not get added to database",
            Toast.LENGTH_SHORT
        ).show()
    }*/
}