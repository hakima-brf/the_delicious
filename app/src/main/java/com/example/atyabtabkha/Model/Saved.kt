package com.example.atyabtabkha.Model

class Saved {

    private var postid: String = ""
    private var publisher: String = ""
    private var image: String = ""
    private var description: String = ""
    private var title: String = ""
    private var time: String = ""
    private var methode: String = ""
    private var ingredients: String = ""
    private var country: String = ""


    constructor()
    constructor(
        postid: String,
        publisher: String,
        image: String,
        description: String,
        title: String,
        time: String,
        methode: String,
        ingredients: String,
        country: String
    ) {
        this.postid = postid
        this.publisher = publisher
        this.image = image
        this.description = description
        this.title = title
        this.time = time
        this.methode = methode
        this.ingredients = ingredients
        this.country = country
    }

    fun getPostId(): String{
        return postid
    }

    fun setPostId(postid: String){
        this.postid = postid
    }

    fun getPublisher(): String{
        return publisher
    }

    fun setPublisher(publisher: String){
        this.publisher = publisher
    }

    fun getImage(): String{
        return image
    }

    fun setImage(image: String){
        this.image = image
    }

    fun getDescription(): String{
        return description
    }

    fun setDescription(description: String){
        this.description = description
    }

    fun getTime(): String{
        return time
    }

    fun setTime(time: String){
        this.time = time
    }

    fun getMethode(): String{
        return methode
    }

    fun setMethode(methode: String){
        this.methode = methode
    }
    fun getIngredients(): String{
        return ingredients
    }

    fun setIngredients(ingredients: String){
        this.ingredients = ingredients
    }
    fun getCountry(): String{
        return country
    }

    fun setCountry(country: String){
        this.country = country
    }
    fun getTitle(): String{
        return title
    }

    fun setTitle(title: String){
        this.title = title
    }




}