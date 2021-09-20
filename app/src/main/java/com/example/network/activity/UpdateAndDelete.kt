package com.example.network.activity

interface  UpdateAndDelete{

    fun modifyItem(itemUID : String,isDone : Boolean)
    fun onItemDelete(itemUID: String)
}