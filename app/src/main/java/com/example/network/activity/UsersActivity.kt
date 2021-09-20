package com.example.network.activity

import android.app.Person
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.network.adapter.UserAdapter
import com.example.network.databinding.ActivityUsersBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class UsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsersBinding
    var userList = ArrayList<com.example.network.model.Person>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userRecyerView.layoutManager =LinearLayoutManager(this,LinearLayout.VERTICAL,false)

        binding.imgBack.setOnClickListener{
            onBackPressed()
        }
        binding.imgProfile.setOnClickListener{
            val intent = Intent(this@UsersActivity,
                ProfileActivity::class.java)
            startActivity(intent)
        }

        getUserList()
    }


    fun getUserList(){

        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()

                val currentUser = snapshot.getValue(com.example.network.model.Person::class.java)
                if (currentUser!!.userImage ==""){
                    binding.imgProfile.setImageResource(com.example.network.R.drawable.profilepic)
                }else{
                    Glide.with(this@UsersActivity).load(currentUser.userImage).into(binding.imgProfile)
                }

                for(dataSnapShot:DataSnapshot in snapshot.children){
                    val user = dataSnapShot.getValue(com.example.network.model.Person::class.java)

                    if (!user!!.userId.equals(firebase.uid)){
                        userList.add(user)
                    }
                }

                val userAdapter = UserAdapter (this@UsersActivity, userList)

                binding.userRecyerView.adapter =userAdapter
            }

            override fun onCancelled(error: DatabaseError) {
               Toast.makeText(this@UsersActivity,error.message,Toast.LENGTH_SHORT).show()
            }

        })
    }
}