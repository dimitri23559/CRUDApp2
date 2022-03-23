package com.example.crudapp

data class Mahasiswa(
    val id:String,
    val nama:String,
    val absen:String
){
    constructor():this("","",""){

    }
}