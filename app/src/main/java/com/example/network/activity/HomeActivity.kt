package com.example.network.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.network.adapter.ChatAdapter
import com.example.network.adapter.UserAdapter
import com.example.network.data.Messages
import com.example.network.data.Points
import com.example.network.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private  lateinit var databaseReference: DatabaseReference
    // count pionts
    var count =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Users")

        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!


        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                val currentUser = snapshot.getValue(com.example.network.model.Person::class.java)
                if (currentUser!!.userImage == "") {
                    binding.imgProfile.setImageResource(com.example.network.R.drawable.profilepic)
                } else {
                    Glide.with(this@HomeActivity).load(currentUser.userImage)
                        .into(binding.imgProfile)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HomeActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })



        binding.imgProfile.setOnClickListener {
            val intent = Intent(
                this@HomeActivity,
                ProfileActivity::class.java
            )
            startActivity(intent)
        }

        binding.imgBack.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@HomeActivity, MainActivity::class.java))
            finish()
        }


        binding.btnChat.setOnClickListener {
            val intent = Intent(
                this@HomeActivity,
                UsersActivity::class.java
            )
            startActivity(intent)

        }

        binding.btnList.setOnClickListener {
            val intent = Intent(
                this@HomeActivity,
                ListHomeActivity::class.java
            )
            startActivity(intent)

        }




    }

    fun OnClick(view: View) {
        count++
        val pCount = binding.showPoints
        pCount.text = count.toString()
    }


}
