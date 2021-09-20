package com.example.network.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.network.R
import com.example.network.adapter.ListAdapter
import com.example.network.data.List
import com.example.network.data.User
import com.example.network.databinding.ActivityListHomeBinding
import com.example.network.model.ToDoModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.item_list.*
import kotlinx.android.synthetic.main.item_list.view.*


class ListHomeActivity : AppCompatActivity(), UpdateAndDelete {

    private lateinit var binding: ActivityListHomeBinding
    var firebaseUser: FirebaseUser? = null

    lateinit var reference: DatabaseReference
    var toDOList : MutableList<ToDoModel>? = null
    private lateinit var adapter: ListAdapter
    private  var listViewItem : ListView? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val  fab = findViewById<View>(R.id.fab) as FloatingActionButton

        listViewItem = findViewById<ListView>(R.id.item_listView)

        reference = FirebaseDatabase.getInstance().getReference("List")

        fab.setOnClickListener{view ->
            val alertDialog = AlertDialog.Builder(this)
            val textEditText = EditText(this)
            alertDialog.setMessage("Add TODO item")
            alertDialog.setTitle("Enter To Do Item")
            alertDialog.setView(textEditText)
            alertDialog.setPositiveButton("Add"){ dialog, i ->

                val todoItemData = ToDoModel.createList()
                todoItemData.itemDataText = textEditText.text.toString()
                todoItemData.done = false

                val newItemData = reference.child("todo").push()
                todoItemData.UID = newItemData.key

                newItemData.setValue(todoItemData)

                dialog.dismiss()
                Toast.makeText(this," item is saved",Toast.LENGTH_SHORT).show()
            }
            alertDialog.show()

        }

        toDOList = mutableListOf<ToDoModel>()
        adapter = ListAdapter(this,toDOList!! )
        listViewItem!!.adapter=adapter
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                toDOList!!.clear()

                addItemToList(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,"No item Added",Toast.LENGTH_SHORT).show()
            }


        })





        binding.imgBack.setOnClickListener{

            onBackPressed()

        }


    }

    private fun addItemToList(snapshot: DataSnapshot) {

        val items = snapshot.children.iterator()

        if(items.hasNext()){

            val toDoIndexValue =  items.next()
            val itemsIterator = toDoIndexValue.children.iterator()

            while(itemsIterator.hasNext()){

                val currentItem = itemsIterator.next()
                val toDoItemDate = ToDoModel.createList()
                val map = currentItem.getValue() as HashMap<*, *>

                toDoItemDate.UID = currentItem.key
                toDoItemDate.done = map.get("done") as Boolean
                toDoItemDate.itemDataText = map.get("itemDataText") as String?
                toDOList!!.add(toDoItemDate)
            }

        }

        adapter.notifyDataSetChanged()

    }

    override fun modifyItem(itemUID: String, isDone: Boolean) {
        val itemReference = reference.child("todo").child(itemUID)
        itemReference.child("done").setValue(isDone)
    }

    override fun onItemDelete(itemUID: String) {
        val itemReference = reference.child("todo").child(itemUID)
        itemReference.removeValue()
        adapter.notifyDataSetChanged()
    }


}