package com.example.atyabtabkha.Model

class User {

    private var username: String = ""
    private var bio: String = ""
    private var image: String = ""
    private var uid: String = ""
    private var fullname: String = ""

    constructor()

    constructor(username: String, fullname: String, bio: String, image: String, uid: String){
        this.bio = bio
        this.fullname = fullname
        this.image = image
        this.uid= uid
        this.username = username
    }

    fun getUserName(): String{
        return username
    }

    fun setUserName(username: String){
        this.username = username
    }

    fun getFullName(): String{
        return fullname
    }

    fun setFullName(fullname: String){
        this.fullname = fullname
    }

    fun getBio(): String{
        return bio
    }

    fun setBio(bio: String){
        this.bio = bio
    }

    fun getUid(): String{
        return uid
    }

    fun setUid(uid: String){
        this.uid = uid
    }

    fun getImage(): String{
        return image
    }

    fun setImage(image: String){
        this.image = image
    }



}