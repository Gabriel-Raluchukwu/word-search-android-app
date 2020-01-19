package com.example.wordsearch.gameFragment


import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI

import com.example.wordsearch.R
import com.example.wordsearch.databinding.FragmentGameBinding
import com.example.wordsearch.util.showToast
import com.google.android.material.chip.Chip
import timber.log.Timber

class GameFragment : Fragment() {
    private lateinit var gameViewModel : GameViewModel
    private val selectedViews = mutableListOf<View>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val gameFragmentBinding: FragmentGameBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_game,container,false)
        setHasOptionsMenu(true)
        //AttachViewModel
        Timber.i("Game ViewModelProviders called")
         gameViewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)

        gameFragmentBinding.setLifecycleOwner(this)
        gameFragmentBinding.gameViewModel = gameViewModel

        val wordList = gameViewModel.searchWords.value ?: emptyList()
        populateSearchWords(wordList,gameFragmentBinding)
        populateGrid(gameFragmentBinding)

        gameViewModel.numberOfFoundWords.observe(this, Observer {num ->
            if(num == 3){
                onGameFinished()
            }
        })

        return gameFragmentBinding.root
    }


    private fun populateSearchWords(words:List<String>, binding:FragmentGameBinding){
        for(word in words){
            val chip:Chip = Chip(binding.gameChipGroup.context)
            chip.apply {
                text = word
                isClickable = false
                isCheckable = false
            }
            binding.gameChipGroup.addView(chip)
        }
    }

    private fun populateGrid(binding: FragmentGameBinding){
        var number = 0
        val dummyData = mutableListOf<Char>()
        for(i in 1..144){
            dummyData.add('d')
            number = i
        }

        Toast.makeText(context,"$number grid cells displayed",Toast.LENGTH_SHORT).show()
        gameViewModel.gridCellList.observe(this, Observer{gridList ->
            val adapter = GameAdapter(this.context!!,gridList)
            binding.gameGridView?.adapter = adapter
        })


        binding.gameGridView?.setOnItemClickListener{_, view, position, id ->
            selectedViews.add(view)
            gameViewModel.buildSelectedCharacters((view as? TextView)?.text?.toString() ?: "0",position)
            view.setBackgroundColor(ContextCompat.getColor(context!!,R.color.lightGreen))
           // showToast(context!!,"${(view as TextView)?.text  ?: "Not Found"} clicked, position: ${position}, id: $id")
        }

       gameViewModel.isWord.observe(this, Observer { isWord ->
            if(!isWord){
                Timber.i("IS NOT A WORD SO RESET")
                selectedViews.forEach {
                    Timber.d("Changed background of View ${(it as? TextView)?.text}")
                    it.setBackgroundColor(ContextCompat.getColor(context!!,R.color.whiteColor))
                }
                selectedViews.clear()
            }
        })

        gameViewModel.isCompleteWord.observe(this, Observer {isComplete ->
            if(isComplete){
                selectedViews.clear()
                gameViewModel.onWordCompleted()
            }
        })
    }

    private fun onGameFinished(){
        view!!.findNavController().navigate(R.id.action_gameFragment_to_gameFinishedFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.reload_menu_item -> {
                Timber.i("Refresh Icon menu item clicked")
                selectedViews.forEach {
                    it.setBackgroundColor(ContextCompat.getColor(context!!,R.color.whiteColor))
                }
                selectedViews.clear()
                gameViewModel.refreshGame()

                true
            }
            else -> { NavigationUI.onNavDestinationSelected(item, view!!.findNavController())
                        || super.onOptionsItemSelected(item)
            }
        }
    }
}
