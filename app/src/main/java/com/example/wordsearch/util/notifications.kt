package com.example.wordsearch.util

import android.content.Context
import android.widget.Toast

fun showToast(context:Context?, message:String){
    val duration = Toast.LENGTH_SHORT
    Toast.makeText(context,message,duration).show()
}