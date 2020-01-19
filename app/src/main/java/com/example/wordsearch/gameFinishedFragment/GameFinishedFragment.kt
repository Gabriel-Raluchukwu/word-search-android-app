package com.example.wordsearch.gameFinishedFragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.wordsearch.R
import com.example.wordsearch.databinding.GameFinishedFragmentBinding


class GameFinishedFragment : Fragment() {

    private lateinit var viewModel: GameFinishedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding:GameFinishedFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.game_finished_fragment,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(GameFinishedViewModel::class.java)
    }

}
