package com.example.network.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(val userId: String? = null, val userName: String? = null,val profileImage: String? = null,){
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}
