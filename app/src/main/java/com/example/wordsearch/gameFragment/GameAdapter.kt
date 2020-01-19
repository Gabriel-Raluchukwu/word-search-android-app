package com.example.wordsearch.gameFragment

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.wordsearch.R
import com.example.wordsearch.util.setPaddingCustom

class GameAdapter constructor(val mContext: Context, val words:List<Char>): BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val textView = TextView(mContext)

        textView.text = words[position].toString()
        textView.id = position
        textView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.whiteColor))
        textView.setPaddingCustom(right = 8,left = 8,top = 8,bottom = 8)
        return textView
    }

    override fun getItem(position: Int): Any {
        return words[position]
    }

    override fun getItemId(position: Int): Long {
       return 0
    }

    override fun getCount() = words.size
}

