package com.example.wordsearch.gameFinishedFragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wordsearch.R


class GameFinishedFragment : Fragment() {

    companion object {
        fun newInstance() =
            GameFinishedFragment()
    }

    private lateinit var viewModel: GameFinishedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.game_finished_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GameFinishedViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
