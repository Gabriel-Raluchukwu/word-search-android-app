package com.example.wordsearch.util

import android.view.View

fun View.setPaddingCustom(left:Int = 0,top:Int = 0,right:Int = 0,bottom:Int = 0){
   val scale = resources.displayMetrics.density

    val leftPx = ((left * scale) + 0.5f).toInt()
    val topPx = ((top * scale) + 0.5f).toInt()
    val rightPx = ((right * scale) + 0.5f).toInt()
    val bottomPx = ((bottom * scale) + 0.5f).toInt()

    this.setPadding(leftPx,topPx,rightPx,bottomPx)
}

//fun generateRandom():Int{
//    return  Random.nextInt(50) % 2
//}